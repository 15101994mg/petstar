package br.com.develop.yustar.front.controller.usuario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.develop.yustar.front.rs.exception.ErrorHandlingInterceptor;
import br.com.develop.yustar.front.rs.servico.AgendaClient;
import br.com.develop.yustar.front.rs.servico.ConfiguracaoServicoFornecedorClient;
import br.com.develop.yustar.front.rs.servico.TipoServicoClient;
import br.com.develop.yustar.front.rs.usuario.UsuarioClient;
import br.com.develop.yustar.front.security.auth.LoginAuthenticator;
import br.com.develop.yustar.rs.model.AgendaDTO;
import br.com.develop.yustar.rs.model.BairroDTO;
import br.com.develop.yustar.rs.model.ConfiguracaoServicoFornecedorDTO;
import br.com.develop.yustar.rs.model.LinhaPesquisaPorFornecedorDTO;
import br.com.develop.yustar.rs.model.TipoServicoDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

@Named(value = PainelBuscaFornecedorControllerBean.CDI_NAME)
@ViewScoped
@Interceptors({ ErrorHandlingInterceptor.class })
public class PainelBuscaFornecedorControllerBean implements Serializable {

	private static final String AGENDAMENTO_PROFISSIONAL = "Agendamento de [%s] com profissional [%s] em [%s]";

	private static final String AGENDAMENTO = "Agendamento";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CDI_NAME = "painelBuscaFornecedorControllerBean";

	private static final int MODO_PESQUISA = 1;
	private static final int MODO_DETALHE = 2;
	private static final int MODO_AGENDAMENTO = 3;

	private static final String TIPO_PAGAMENTO_DINHEIRO = "1";
	private static final String TIPO_PAGAMENTO_CARTAO = "2";

	@Inject
	private UsuarioClient usuClient;

	@Inject
	private LoginAuthenticator loginAuth;

	@Inject
	private TipoServicoClient tipoServClient;

	@Inject
	private AgendaClient agendaClient;

	@Inject
	private ConfiguracaoServicoFornecedorClient confClient;

	private UsuarioDTO usuario;

	private BairroDTO bairro;

	private List<BairroDTO> bairros;

	private TipoServicoDTO tipoServico;

	private List<TipoServicoDTO> tiposServico;

	private String tipoPagamento;

	private LinhaPesquisaPorFornecedorDTO fornecedorSelecionado;

	private List<LinhaPesquisaPorFornecedorDTO> linhasFornecedor;

	private int visaoTela;

	private ScheduleModel eventModel;

	private ScheduleEvent event = new DefaultScheduleEvent();

	private ConfiguracaoServicoFornecedorDTO configuracaoFornecedor;

	private List<AgendaDTO> agendas;

	@PostConstruct
	public void inicializar() {
		inicializarListas();
		atualizarVisaoTelaPesquisa();
		inicializaAgenda();
		usuario = loginAuth.getUsuarioLogado();
	}

	private void inicializaAgenda() {
		eventModel = new DefaultScheduleModel();

	}

	private void atualizarVisaoTela(int visao) {
		this.visaoTela = visao;
	}

	public void atualizarVisaoTelaPesquisa() {
		atualizarVisaoTela(MODO_PESQUISA);
	}

	public void atualizarVisaoTelaDetalhe() {
		atualizarVisaoTela(MODO_DETALHE);
	}

	public void atualizarVisaoTelaAgendamento() {
		atualizarVisaoTela(MODO_AGENDAMENTO);
	}

	public List<BairroDTO> obterBairros(String valor) {
		return bairros.stream()
				.filter(b -> b.getNome().toUpperCase().contains(valor.toUpperCase())
						|| b.getZona().toUpperCase().contains(valor.toUpperCase())
						|| b.getMunicipio().toUpperCase().contains(valor.toUpperCase()))
				.collect(Collectors.toList());
	}

	public List<TipoServicoDTO> obterServicos(String valor) {
		return tiposServico.stream().filter(ts -> ts.getNome().toUpperCase().contains(valor.toUpperCase()))
				.collect(Collectors.toList());
	}

	private void inicializarListas() {
		this.bairros = usuClient.obterTodosBairros();
		this.tiposServico = tipoServClient.obterTiposServico();
	}

	private void inicializarUsuario() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		this.usuario = (UsuarioDTO) session.getAttribute("usuario");
	}

	public void pesquisar() {
		this.linhasFornecedor = usuClient
				.obterFornecedoresPorTipoServicoUFEMunicipio(this.tipoServico == null ? 0 : this.tipoServico.getId(), "RJ", "Rio de Janeiro").stream()
				.filter(l -> (this.bairro == null || l.getBairroId() == this.bairro.getId().intValue()))
				.collect(Collectors.toList());

	}

	public void agendar() {
		montarAgenda();

		atualizarVisaoTelaAgendamento();
	}

	private void montarAgenda() {
		configuracaoFornecedor = confClient
				.obterConfiguracaoPorId(fornecedorSelecionado.getConfiguracaoServicoFornecedorId());
		Date data = obterData();
		this.agendas = agendaClient.obterTodosAgendasPorFornecedorData(this.fornecedorSelecionado.getId(), data);
		for (AgendaDTO dto : agendas) {
			DefaultScheduleEvent evt = DefaultScheduleEvent.builder().title("Serviço Agendado")
					.startDate(dto.getDataInicioAgenda().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
					.endDate(dto.getDataFimAgenda().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
					.build();
			eventModel.addEvent(evt);

		}
	}

	public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
		this.tipoPagamento = TIPO_PAGAMENTO_DINHEIRO;
		event = DefaultScheduleEvent.builder().startDate(selectEvent.getObject())
				.endDate(selectEvent.getObject().plusHours(this.configuracaoFornecedor.getQuantidadeHoras())
						.plusMinutes(this.configuracaoFornecedor.getQuantidadeMinutos()))
				.build();
	}

	public void onEventSelect(SelectEvent<ScheduleEvent> selectEvent) {
		event = selectEvent.getObject();
	}

	public void onFromDateSelect(SelectEvent<Date> event) {
		((DefaultScheduleEvent) this.event).setEndDate(event.getObject().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(this.configuracaoFornecedor.getQuantidadeHoras())
				.plusMinutes(this.configuracaoFornecedor.getQuantidadeMinutos()));
	}

	public Date obterData() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2020);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public void exibirDetalhes(LinhaPesquisaPorFornecedorDTO fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
		atualizarVisaoTelaDetalhe();
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public UsuarioClient getUsuClient() {
		return usuClient;
	}

	public void setUsuClient(UsuarioClient usuClient) {
		this.usuClient = usuClient;
	}

	public BairroDTO getBairro() {
		return bairro;
	}

	public void setBairro(BairroDTO bairro) {
		this.bairro = bairro;
	}

	public List<BairroDTO> getBairros() {
		return bairros;
	}

	public void setBairros(List<BairroDTO> bairros) {
		this.bairros = bairros;
	}

	public TipoServicoDTO getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServicoDTO tipoServico) {
		this.tipoServico = tipoServico;
	}

	public List<TipoServicoDTO> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServicoDTO> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public List<LinhaPesquisaPorFornecedorDTO> getLinhasFornecedor() {
		return linhasFornecedor;
	}

	public void setLinhasFornecedor(List<LinhaPesquisaPorFornecedorDTO> linhasFornecedor) {
		this.linhasFornecedor = linhasFornecedor;
	}

	public LinhaPesquisaPorFornecedorDTO getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(LinhaPesquisaPorFornecedorDTO fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public boolean modoPesquisa() {
		return visaoTela == MODO_PESQUISA;
	}

	public boolean modoDetalhe() {
		return visaoTela == MODO_DETALHE;
	}

	public boolean modoAgendamento() {
		return visaoTela == MODO_AGENDAMENTO;
	}

	public String montarHeaderAgendamento() {
		if (fornecedorSelecionado == null) {
			return AGENDAMENTO;
		}
		return String.format(AGENDAMENTO_PROFISSIONAL, fornecedorSelecionado.getNomeServico(),
				fornecedorSelecionado.getNome(), fornecedorSelecionado.getBairro());
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void gravarAgendamento() {
		AgendaDTO agenda = agendaClient.agendar(configuracaoFornecedor.getId(), usuario.getId(),
				Date.from(this.event.getStartDate().atZone(ZoneId.systemDefault()).toInstant()), this.tipoPagamento);
		montarAgenda();
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
}
