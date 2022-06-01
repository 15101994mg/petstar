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
import br.com.develop.yustar.rs.model.ConfiguracaoServicoFornecedorDTO;
import br.com.develop.yustar.rs.model.GrupoServicoDTO;
import br.com.develop.yustar.rs.model.TipoServicoDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

public class ConfiguracaoServicoFornecedorClient implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConfiguracaoServicoFornecedorClient() {
		super();
	}

	public List<ConfiguracaoServicoFornecedorDTO> obterConfiguracoesPorUsuarioETipo(UsuarioDTO usuario,
			TipoServicoDTO tipoServico) {

		if (usuario == null) {
			throw new BusinessFrontException("Usuário inválido");
		}
		String urlStr = "http://localhost:8080/yustarback/rest/configuracaoservicofornecedor/obterservicosativosporfornecedoretipo";

		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("fornecedorid", usuario.getId().toString());
		if (tipoServico != null) {
			conn.addParams("tiposervicoid", tipoServico.getId().toString());
		}
		Response response = conn.getResponse();
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<ConfiguracaoServicoFornecedorDTO>> type = new TypeToken<List<ConfiguracaoServicoFornecedorDTO>>() {
		};
		String retorno = response.readEntity(String.class);

		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());

		return (List<ConfiguracaoServicoFornecedorDTO>) lista;
	}

	public List<TipoServicoDTO> obterTiposServicoPorGrupo(String grupoServicoId) {
		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/obtertiposservicoporgrupo";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE).addParams("gruposervicoid", grupoServicoId)
				.getResponse();
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
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
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
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
		if (grupo != null) {
			conn.addParams("gruposervicoid", grupo.getId().toString());
		}
		if (tipoServico != null) {
			conn.addParams("tiposervicoid", tipoServico.getId().toString());
		}

		Response response = conn.getResponse();
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
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

		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}

	private void validarGrupoGravacao(GrupoServicoDTO grupo) {
		if (grupo == null) {
			throw new BusinessFrontException("Grupo inválido");
		}
		if (grupo.getNome() == null) {
			throw new BusinessFrontException("Nome do grupo inválido");
		}
	}

	public void gravarServico(TipoServicoDTO tipo) {
		validarTipoServicoGravacao(tipo);
		Integer usuarioId = obterUsuarioIdDaSessao();

		String urlStr = "http://localhost:8080/yustarback/rest/tiposervico/gravarservico";

		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);

		conn.addParams("nome", tipo.getNome());
		conn.addParams("descricao", tipo.getDescricao());
		conn.addParams("gruposervicoid", String.valueOf(tipo.getGrupoServicoId()));
		conn.addParams("usuarioid", usuarioId.toString());

		Response response = conn.getResponse();

		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}

	private int obterUsuarioIdDaSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		UsuarioDTO usu = (UsuarioDTO) session.getAttribute("usuario");
		if (usu == null) {
			throw new BusinessFrontException("Usuário não encontrado na sessão");
		}
		return usu.getId();
	}

	private void validarTipoServicoGravacao(TipoServicoDTO tipo) {
		if (tipo == null) {
			throw new BusinessFrontException("Tipo de serviço inválido");
		}
		if (tipo.getGrupoServicoId() == 0) {
			throw new BusinessFrontException("Grupo do tipo do serviço está inválido");
		}
		if (tipo.getDescricao() == null) {
			throw new BusinessFrontException("Descrição do tipo de serviço está inválida");
		}
	}

	public void gravarConfiguracaoServico(ConfiguracaoServicoFornecedorDTO conf) {
		validarConfiguracao(conf);

		String urlStr = "http://localhost:8080/yustarback/rest/configuracaoservicofornecedor/cadastrar";

		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);

		conn.addParams("configuracaoid", conf.getId().toString());
		conn.addParams("fornecedorid", String.valueOf(conf.getFornecedorId()));
		conn.addParams("tiposervicoid", String.valueOf(conf.getTipoServicoId()));
		conn.addParams("qtdhoras", String.valueOf(conf.getQuantidadeHoras()));
		conn.addParams("qtdminutos", String.valueOf(conf.getQuantidadeMinutos()));
		conn.addParams("valservico", String.valueOf(conf.getValorServico()));
		conn.addParams("ativo", String.valueOf(conf.isAtivo()));
		conn.addParams("atendedomicilio", String.valueOf(conf.isAtendeDomicilio()));
		conn.addParams("horainicioatendimento", String.valueOf(conf.getHoraInicioAtendimento()));
		conn.addParams("horafimatendimento", String.valueOf(conf.getHoraFimAtendimento()));
		conn.addParams("minutoinicioatendimento", String.valueOf(conf.getMinutoInicioAtendimento()));
		conn.addParams("minutofimatendimento", String.valueOf(conf.getMinutoFimAtendimento()));
		conn.addParams("descricao", conf.getDescricaoServico());

		Response response = conn.getResponse();

		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}

	}

	private void validarConfiguracao(ConfiguracaoServicoFornecedorDTO conf) {
		if (conf == null) {
			throw new BusinessFrontException("configuração inválida");
		}
		if (conf.getFornecedorId() == 0) {
			throw new BusinessFrontException("fornecedor inválido");
		}
	}

	public void exluirConfiguracaoPorId(int id) {
		String urlStr = "http://localhost:8080/yustarback/rest/configuracaoservicofornecedor/desativar";

		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);

		conn.addParams("configuracaoservicofornecedorid", String.valueOf(id));

		Response response = conn.getResponse();

		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}

	public ConfiguracaoServicoFornecedorDTO obterConfiguracaoPorId(int id) {
		String urlStr = "http://localhost:8080/yustarback/rest/configuracaoservicofornecedor/obterporid";

		HttpPostConnector conn = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE).addParams("id", String.valueOf(id));

		Response response = conn.getResponse();
		if (Response.Status.OK.getStatusCode() != response.getStatus()) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<ConfiguracaoServicoFornecedorDTO> type = new TypeToken<ConfiguracaoServicoFornecedorDTO>() {
		};
		String retorno = response.readEntity(String.class);

		Gson gson = JsonUtils.getGson();
		Object conf = gson.fromJson(retorno, type.getType());

		return (ConfiguracaoServicoFornecedorDTO) conf;
	}

}
