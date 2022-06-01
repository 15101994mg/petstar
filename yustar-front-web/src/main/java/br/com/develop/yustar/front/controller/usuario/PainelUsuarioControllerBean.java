package br.com.develop.yustar.front.controller.usuario;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.develop.yustar.front.rs.usuario.UsuarioClient;
import br.com.develop.yustar.rs.model.UsuarioDTO;

@Named(value = PainelUsuarioControllerBean.CDI_NAME)
@ViewScoped
public class PainelUsuarioControllerBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CDI_NAME = "painelUsuarioControllerBean";
	
	@Inject
	private UsuarioClient usuClient;

	
	private String tipoUsuario;

	private List<UsuarioDTO> usuarios;
	
	public PainelUsuarioControllerBean() {
		super();
		this.tipoUsuario = "0";
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	
	public void pesquisar() {
		this.usuarios =  usuClient.obterUsuariosPorTipo( this.tipoUsuario);
	}
	
	public void excluirUsuario( UsuarioDTO usuario) {
		usuClient.excluirUsuario(usuario);
		pesquisar();
	}

	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}
	

}
