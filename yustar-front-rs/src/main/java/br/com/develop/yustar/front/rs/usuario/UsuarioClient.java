package br.com.develop.yustar.front.rs.usuario;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.develop.yustar.front.rs.exception.BusinessFrontException;
import br.com.develop.yustar.rs.connector.HttpPostConnector;
import br.com.develop.yustar.rs.gadgets.JsonUtils;
import br.com.develop.yustar.rs.model.BairroDTO;
import br.com.develop.yustar.rs.model.LinhaPesquisaPorFornecedorDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;

public class UsuarioClient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public UsuarioClient() {
		super();
	}
	
	
	public void cadastrarCliente(String nome, String nomeSocial, String login, String email, String cep,
			String endereco, String cpf, String sexo, String senha, BairroDTO bairro, String ddd,
			String numeroTelefone) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/cadastrarcliente";

		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("nome", nome)
				.addParams("nomesocial", nomeSocial)
				.addParams("login", login)
				.addParams("email", email)
				.addParams("cep", cep)
				.addParams("endereco", endereco)
				.addParams("cpf", cpf)
				.addParams("sexo", sexo)
				.addParams("senha", getSenhaEncriptada(senha))
				.addParams("bairro", bairro == null ? "0" : bairro.getId().toString())
				.addParams("ddd", ddd)
				.addParams("telefone", numeroTelefone)
				
				.getResponse();
		
	}
	
	public void cadastrarFornecedor(String nome, String nomeSocial, String login, String email, String cep,
			String endereco, String cpf, String sexo, String cnpj, String tipoFornecedor, String senha, BairroDTO bairro, String ddd, String numeroTelefone) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/cadastrarfornecedor";

		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("nome", nome)
				.addParams("nomesocial", nomeSocial)
				.addParams("login", login)
				.addParams("email", email)
				.addParams("cep", cep)
				.addParams("endereco", endereco)
				.addParams("cpf", cpf)
				.addParams("cnpj", cnpj)
				.addParams("tipofornecedor", tipoFornecedor)
				.addParams("sexo", sexo)
				.addParams("senha", getSenhaEncriptada(senha))
				.addParams("bairro", bairro == null ? "0" : bairro.getId().toString())
				.addParams("ddd", ddd)
				.addParams("telefone", numeroTelefone)
				.getResponse();

	}
	
	public List<BairroDTO> obterTodosBairros(){
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/obterbairros";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<BairroDTO>> type = new TypeToken<List<BairroDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<BairroDTO>) lista;
	}
	
	private String getSenhaEncriptada(String password) {
		return DigestUtils.md5Hex(password);
	}


	public List<UsuarioDTO> obterUsuariosPorTipo(String tipoUsuario) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/usuariosportipo";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("tipo", tipoUsuario).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<UsuarioDTO>> type = new TypeToken<List<UsuarioDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<UsuarioDTO>) lista;
	}

	public List<String> obterMunicipiosPorUF(String uf) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/obtermunicipiosporuf";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("uf", uf).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<String>> type = new TypeToken<List<String>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<String>) lista;
	}
	
	public void excluirUsuario(UsuarioDTO usuario) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/excluirporid";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("id", usuario.getId().toString()).getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
	}
	
	public List<String> obterZonasPorUFEMunicipio(String uf, String municipio) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/obterzonasporufemunicipio";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("uf", uf)
				.addParams("municipio", municipio)
				.getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<String>> type = new TypeToken<List<String>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<String>) lista;
		
	}
	
	public List<BairroDTO> obterBairrosPorUFMunicipioEZona(String uf, String municipio, String zona) {
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/obterbairrosporufemunicipio";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("uf", uf)
				.addParams("municipio", municipio)
				.getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<BairroDTO>> type = new TypeToken<List<BairroDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		return (List<BairroDTO>) lista;
	}
	
	public List<LinhaPesquisaPorFornecedorDTO> obterFornecedoresPorTipoServicoUFEMunicipio(int tipoServicoId,
			String uf, String municipio) {	
		String urlStr = "http://localhost:8080/yustarback/rest/usuario/obterfornecedoresportipoufemunicipio";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("tiposervicoid", String.valueOf(tipoServicoId))
				.addParams("uf", uf)
				.addParams("municipio", municipio)
				.getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<LinhaPesquisaPorFornecedorDTO>> type = new TypeToken<List<LinhaPesquisaPorFornecedorDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		return (List<LinhaPesquisaPorFornecedorDTO>) lista;
		
	}

}
