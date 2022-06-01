package br.com.develop.yustar.front.controller.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.develop.yustar.front.rs.exception.BusinessFrontException;
import br.com.develop.yustar.front.rs.servico.TipoServicoClient;
import br.com.develop.yustar.front.security.auth.LoginAuthenticator;
import br.com.develop.yustar.rs.model.GrupoServicoDTO;
import br.com.develop.yustar.rs.model.TipoServicoDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

@Named(value = PainelServicoControllerBean.CDI_NAME)
@ViewScoped
public class PainelServicoControllerBean implements Serializable {

	private static final String PAINEL_SERVICO_ADMINISTRADOR = "PAINEL_SERVICO_ADMINISTRADOR";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CDI_NAME = "painelServicoControllerBean";

	@Inject
	private TipoServicoClient tipoServClient;

	@Inject
	private LoginAuthenticator loginAuth;
	
	private String tipoUsuario;

	private List<UsuarioDTO> usuarios;

	private List<GrupoServicoDTO> grupos;

	private List<TipoServicoDTO> tiposServico;

	private GrupoServicoDTO grupo;

	private TipoServicoDTO tipoServico;

	private GrupoServicoDTO grupoGravacao;

	private TipoServicoDTO tipoServicoGravacao;

	private List<TipoServicoDTO> servicos;

	private boolean visaoGrid;

	private boolean visaoFichaGrupo;

	private boolean visaoFichaTipo;

	public PainelServicoControllerBean() {
		super();
		this.tipoUsuario = "0";
		visaoGrid = true;
		visaoFichaGrupo = false;
		visaoFichaTipo = false;
	}

	public boolean visualizarGrid() {
		return visaoGrid && !visaoFichaGrupo && !visaoFichaTipo;
	}

	public boolean visualizarFichaGrupo() {
		return !visaoGrid && visaoFichaGrupo && !visaoFichaTipo;
	}

	public boolean visualizarFichaTipo() {
		return !visaoGrid && !visaoFichaGrupo && visaoFichaTipo;
	}

	private void atualizarVisoes(boolean exibeGrid, boolean exibeFichaGrupo, boolean exibeFichaTipo) {
		this.visaoGrid = exibeGrid;
		this.visaoFichaGrupo = exibeFichaGrupo;
		this.visaoFichaTipo = exibeFichaTipo;
	}

	@PostConstruct
	public void inicializar() {
		atualizarListasServico();

	}

	private void atualizarListasServico() {
		this.grupos = tipoServClient.obterGruposServico();
		this.tiposServico = tipoServClient.obterTiposServico();
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void pesquisar() {
		this.servicos = tipoServClient.obterTiposServicoPorGrupoETipo(grupo, tipoServico);
		atualizarListasServico();
	}

	public void novoGrupo() {
		this.grupoGravacao = new GrupoServicoDTO();
		atualizarVisoes(false, true, false);
	}

	public void novoServico() {
		this.tipoServicoGravacao = new TipoServicoDTO();
		atualizarVisoes(false, false, true);
	}

	public String gravarGrupo() {
		tipoServClient.gravarGrupo(this.grupoGravacao);
		atualizarListasServico();
		atualizarVisoes(true, false, false);
		setGrupoGravacao(null);
		return PAINEL_SERVICO_ADMINISTRADOR;
		
	}

	public String gravarServico() {
		if ( getGrupoGravacao() == null ) {
			throw new BusinessFrontException("Selecione um grupo válido");
		}
		tipoServicoGravacao.setGrupoServicoId(getGrupoGravacao().getId());
		tipoServicoGravacao.setUsuarioCriacaoId(loginAuth.getUsuarioLogado().getId());
 		tipoServClient.gravarServico(this.tipoServicoGravacao);
		atualizarListasServico();
		atualizarVisoes(true, false, false);
		setGrupoGravacao(null);
		this.tipoServicoGravacao = null;
		return PAINEL_SERVICO_ADMINISTRADOR;
	}

	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}

	public List<GrupoServicoDTO> obterGrupos(String valor) {
		return grupos.stream().filter(g -> g.getNome().toUpperCase().contains(valor.toUpperCase()))
				.collect(Collectors.toList());
	}

	public List<TipoServicoDTO> obterServicos(String valor) {
		return tiposServico.stream()
				.filter(s -> s.getNome().toUpperCase().contains(valor.toUpperCase())
						&& (this.grupo == null || this.grupo.getId().intValue() == s.getGrupoServicoId()))
				.collect(Collectors.toList());
	}

	public List<GrupoServicoDTO> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoServicoDTO> grupos) {
		this.grupos = grupos;
	}

	public List<TipoServicoDTO> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServicoDTO> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public GrupoServicoDTO getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoServicoDTO grupo) {
		this.grupo = grupo;
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

	public boolean isVisaoFichaGrupo() {
		return visaoFichaGrupo;
	}

	public boolean isVisaoFichaTipo() {
		return visaoFichaTipo;
	}

	public GrupoServicoDTO getGrupoGravacao() {
		return grupoGravacao;
	}

	public TipoServicoDTO getTipoServicoGravacao() {
		return tipoServicoGravacao;
	}

	public void limparServicoFiltro() {
		this.tipoServico = null;
	}

	public void cancelarGrupo() {
		this.grupoGravacao = null;
		atualizarVisoes(true, false, false);
	}

	public void cancelarServico() {
		this.tipoServicoGravacao = null;
		this.grupoGravacao = null;
		atualizarVisoes(true, false, false);
	}

	public void setGrupoGravacao(GrupoServicoDTO grupoGravacao) {
		this.grupoGravacao = grupoGravacao;
	}

}
