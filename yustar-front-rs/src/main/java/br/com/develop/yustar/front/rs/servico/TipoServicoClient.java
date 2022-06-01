package br.com.develop.yustar.front.rs.servico;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.develop.yustar.front.rs.exception.BusinessFrontException;
import br.com.develop.yustar.rs.connector.HttpPostConnector;
import br.com.develop.yustar.rs.gadgets.JsonUtils;
import br.com.develop.yustar.rs.model.GrupoServicoDTO;
import br.com.develop.yustar.rs.model.TipoServicoDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

public class TipoServicoClient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public TipoServicoClient() {
		super();
	}
	
	
	public List<GrupoServicoDTO> obterGruposServico() {
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/obtergruposservico";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<GrupoServicoDTO>> type = new TypeToken<List<GrupoServicoDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<GrupoServicoDTO>) lista;
	}

	public List<TipoServicoDTO> obterTiposServicoPorGrupo(String grupoServicoId) {
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/obtertiposservicoporgrupo";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("gruposervicoid", grupoServicoId).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<TipoServicoDTO>> type = new TypeToken<List<TipoServicoDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<TipoServicoDTO>) lista;
	}
	
	public List<TipoServicoDTO> obterTiposServico() {
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/obtertiposservico";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<TipoServicoDTO>> type = new TypeToken<List<TipoServicoDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<TipoServicoDTO>) lista;
	}


	public List<TipoServicoDTO> obterTiposServicoPorGrupoETipo(GrupoServicoDTO grupo, TipoServicoDTO tipoServico) {
			String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/obtertiposservicoporgrupoetipo";
			
			HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
					.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
			if ( grupo != null ) {
				conn.addParams("gruposervicoid", grupo.getId().toString());
			}
			if ( tipoServico != null ) {
				conn.addParams("tiposervicoid", tipoServico.getId().toString());
			}
			
			Response response = conn.getResponse();
			if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
				String mensagem = response.readEntity(String.class);
				throw new BusinessFrontException(mensagem);
			}
			TypeToken<List<TipoServicoDTO>> type = new TypeToken<List<TipoServicoDTO>>() {
			};
			String retorno = response.readEntity(String.class);
			
			Gson gson = JsonUtils.getGson();
			List<?> lista = gson.fromJson(retorno, type.getType());
			
			return (List<TipoServicoDTO>) lista;
	}


	public void gravarGrupo(GrupoServicoDTO grupo) {
		validarGrupoGravacao(grupo);
		
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/gravargrupo";
		
		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
		conn.addParams("nome", grupo.getNome());
		Response response = conn.getResponse();
		
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}


	private void validarGrupoGravacao(GrupoServicoDTO grupo) {
		if ( grupo == null) {
			throw new BusinessFrontException("Grupo inválido");
		}
		if ( grupo.getNome() == null) {
			throw new BusinessFrontException("Nome do grupo inválido");
		}
	}


	public void gravarServico(TipoServicoDTO tipo) {
		validarTipoServicoGravacao(tipo);
		
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/gravarservico";
		
		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);

		conn.addParams("nome", tipo.getNome() );
		conn.addParams("descricao", tipo.getDescricao() );
		conn.addParams("gruposervicoid", String.valueOf( tipo.getGrupoServicoId() ) );
		conn.addParams("usuarioid", String.valueOf( tipo.getUsuarioCriacaoId()) );
		
		Response response = conn.getResponse();
		
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}


	private int obterUsuarioIdDaSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		UsuarioDTO usu = (UsuarioDTO) session.getAttribute("usuario");
		if ( usu == null ) {
			throw new BusinessFrontException("Usuário não encontrado na sessão");
		}
		return usu.getId();
	}


	private void validarTipoServicoGravacao(TipoServicoDTO tipo) {
		if ( tipo == null ) {
			throw new BusinessFrontException("Tipo de serviço inválido");
		}
		if ( tipo.getGrupoServicoId() == 0 ) {
			throw new BusinessFrontException("Grupo do tipo do serviço está inválido");
		}
		if ( tipo.getDescricao() == null ) {
			throw new BusinessFrontException("Descrição do tipo de serviço está inválida");
		}
	}
	

}
