package br.com.develop.yustar.front.controller.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import br.com.develop.yustar.front.rs.exception.BusinessFrontException;
import br.com.develop.yustar.front.rs.exception.ErrorHandlingInterceptor;
import br.com.develop.yustar.front.rs.security.FacesService;
import br.com.develop.yustar.front.rs.servico.ConfiguracaoServicoFornecedorClient;
import br.com.develop.yustar.front.rs.servico.TipoServicoClient;
import br.com.develop.yustar.front.security.auth.LoginAuthenticator;
import br.com.develop.yustar.rs.model.ConfiguracaoServicoFornecedorDTO;
import br.com.develop.yustar.rs.model.TipoServicoDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

@Named(value = CadastroServicoFornecedorControllerBean.CDI_NAME)
@ViewScoped
@Interceptors({ ErrorHandlingInterceptor.class })
public class CadastroServicoFornecedorControllerBean implements Serializable {

	private static final String CRUD_SERVICO_FORNECEDOR = "CRUD_SERVICO_FORNECEDOR";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CDI_NAME = "cadastroServicoFornecedorControllerBean";

	@Inject
	private TipoServicoClient tipoServClient;

	@Inject
	private ConfiguracaoServicoFornecedorClient confClient;

	@Inject
	private LoginAuthenticator loginAuth;
	
	private UsuarioDTO usuario;

	private String tempoServico;
	private String inicioAtendimento;
	private String fimAtendimento;

	private String tipoUsuario;

	private List<TipoServicoDTO> tiposServico;

	private TipoServicoDTO tipoServico;

	private TipoServicoDTO tipoServicoGravacao;

	private ConfiguracaoServicoFornecedorDTO configuracaoServicoGravacao;

	private List<TipoServicoDTO> servicos;

	private boolean visaoGrid;

	private List<ConfiguracaoServicoFornecedorDTO> configuracoes;

	public CadastroServicoFornecedorControllerBean() {
		super();

		this.tipoUsuario = "0";
		visaoGrid = true;
	}

	public boolean visualizarGrid() {
		return visaoGrid;
	}

	private void atualizarVisoes(boolean exibeGrid) {
		this.visaoGrid = exibeGrid;
	}

	@PostConstruct
	public void inicializar() {
		atualizarListasServico();
		this.usuario = loginAuth.getUsuarioLogado();
	}

	private void atualizarListasServico() {
		this.tiposServico = tipoServClient.obterTiposServico();
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void pesquisar() {
		this.configuracoes = confClient.obterConfiguracoesPorUsuarioETipo(usuario, tipoServico);
		atualizarListasServico();
	}

	public void novoServico() {
		this.configuracaoServicoGravacao = new ConfiguracaoServicoFornecedorDTO();
		
		this.tipoServicoGravacao = null;
		this.tempoServico = "";
		this.inicioAtendimento = "";
		this.fimAtendimento = "";
		atualizarVisoes(false);
	}

	public void editarServico() {
		List<ConfiguracaoServicoFornecedorDTO> selecionados = obterSelecionados();
		validarListaParaEdicao(selecionados);
		ConfiguracaoServicoFornecedorDTO conf = selecionados.get(0); 
		this.configuracaoServicoGravacao = conf;
		this.tipoServicoGravacao = obterServico(conf.getTipoServicoId());
		this.tempoServico = conf.getTempoServico();
		this.inicioAtendimento = conf.getHoraInicio();
		this.fimAtendimento = conf.getHoraFim();
		
		if ( this.tipoServicoGravacao == null ) {
			throw new BusinessFrontException("Tipo de serviço não encontrado");
		}
		atualizarVisoes(false);
	}
	
	private TipoServicoDTO obterServico(int tipoServicoId) {
		return tiposServico.stream().filter(t -> t.getId().intValue() == tipoServicoId).findFirst().orElse(null);
	}

	public void excluirServico() {
		List<ConfiguracaoServicoFornecedorDTO> selecionados = obterSelecionados();
		if ( selecionados.isEmpty() ) {
 			throw new BusinessFrontException("Selecione pelo menos um registro");
		}
		selecionados.forEach(c -> confClient.exluirConfiguracaoPorId(c.getId()));
		pesquisar();
		FacesService.info("Configuração excluída com sucesso");
	}
	
	

	private void validarListaParaEdicao(List<ConfiguracaoServicoFornecedorDTO> selecionados) {
		if ( selecionados.size() > 1 ) {
 			throw new BusinessFrontException("Selecione somente um registro");
		}
		if ( selecionados.isEmpty() ) {
 			throw new BusinessFrontException("Selecione pelo menos um registro");
		}
	}

	private List<ConfiguracaoServicoFornecedorDTO> obterSelecionados() {
		if ( configuracoes == null || configuracoes.isEmpty()) {
			throw new BusinessFrontException("Não existem configurações a serem manipuladas");
		}
		return configuracoes.stream().filter(c -> c.isSelecionado()).collect(Collectors.toList());
	}

	public String gravarServico() {
		if (getTipoServicoGravacao() == null) {
			throw new BusinessFrontException("Selecione um tipo de serviço válido");
		}

		configuracaoServicoGravacao.configuraTempoServico(tempoServico);
		configuracaoServicoGravacao.configuraInicioAtendimento(inicioAtendimento);
 		configuracaoServicoGravacao.configuraFimAtendimento(fimAtendimento);
		configuracaoServicoGravacao.setFornecedorId(this.usuario.getId());
		

		configuracaoServicoGravacao.setTipoServicoId(getTipoServicoGravacao().getId());
		confClient.gravarConfiguracaoServico(configuracaoServicoGravacao);
		pesquisar();
		atualizarVisoes(true);
		this.configuracaoServicoGravacao = null;
		FacesService.info("Configuração salva com sucesso");
		return CRUD_SERVICO_FORNECEDOR;
	}

	public List<TipoServicoDTO> obterServicos(String valor) {
		return tiposServico.stream().filter(s -> s.getNome().toUpperCase().contains(valor.toUpperCase()))
				.collect(Collectors.toList());
	}

	public List<TipoServicoDTO> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServicoDTO> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public TipoServicoDTO getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServicoDTO tipoServico) {
		this.tipoServico = tipoServico;
	}

	public List<TipoServicoDTO> getServicos() {
		return servicos;
	}

	public void setServicos(List<TipoServicoDTO> servicos) {
		this.servicos = servicos;
	}

	public boolean isVisaoGrid() {
		return visaoGrid;
	}

	public void limparServicoFiltro() {
		this.tipoServico = null;
	}

	public void cancelarServico() {
		this.configuracaoServicoGravacao = null;
		atualizarVisoes(true);
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public ConfiguracaoServicoFornecedorDTO getConfiguracaoServicoGravacao() {
		return configuracaoServicoGravacao;
	}

	public void setConfiguracaoServicoGravacao(ConfiguracaoServicoFornecedorDTO configuracaoServicoGravacao) {
		this.configuracaoServicoGravacao = configuracaoServicoGravacao;
	}

	public List<ConfiguracaoServicoFornecedorDTO> getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(List<ConfiguracaoServicoFornecedorDTO> configuracoes) {
		this.configuracoes = configuracoes;
	}

	public TipoServicoDTO getTipoServicoGravacao() {
		return tipoServicoGravacao;
	}

	public void setTipoServicoGravacao(TipoServicoDTO tipoServicoGravacao) {
		this.tipoServicoGravacao = tipoServicoGravacao;
	}

	public String getTempoServico() {
		return tempoServico;
	}

	public void setTempoServico(String tempoServico) {
		this.tempoServico = tempoServico;
	}

	public String getInicioAtendimento() {
		return inicioAtendimento;
	}

	public void setInicioAtendimento(String inicioAtendimento) {
		this.inicioAtendimento = inicioAtendimento;
	}

	public String getFimAtendimento() {
		return fimAtendimento;
	}

	public void setFimAtendimento(String fimAtendimento) {
		this.fimAtendimento = fimAtendimento;
	}

}
