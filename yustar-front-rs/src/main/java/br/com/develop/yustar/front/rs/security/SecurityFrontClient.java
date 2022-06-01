package br.com.develop.yustar.front.rs.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.develop.yustar.front.rs.exception.AuthenticationException;
import br.com.develop.yustar.front.rs.exception.InvalidPasswordException;
import br.com.develop.yustar.rs.connector.HttpPostConnector;
import br.com.develop.yustar.rs.model.PerfilDTO;
import br.com.develop.yustar.rs.model.UsuarioDTO;
import br.com.develop.yustar.rs.model.factory.SecurityRSFactory;
import br.com.develop.yustar.rs.utils.StringKeyValuePair;

@Named
public class SecurityFrontClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String UTF8 = "UTF-8";

	public UsuarioDTO authenticate(String login, String password) throws AuthenticationException, IOException {
		String usuJson = getAuthenticationToken(login, password);
		UsuarioDTO usuDTO  = null;
		boolean autenticacaoRealizada = !"INVALID".equals(usuJson) && !usuJson.trim().equals("");
		if (autenticacaoRealizada) {
			usuDTO = SecurityRSFactory.getUsuarioFromJsonString(usuJson);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.setAttribute("usuario", usuDTO);
		}
		return usuDTO;
	}

	private String getAuthenticationToken(String login, String senha)
			throws IOException, InvalidPasswordException {
		String urlStr = "http://localhost:8080/yustarback/rest/auth/getauthtoken";

		Response response = new HttpPostConnector().setUrl(urlStr).addParams("login", login)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("password", getSenhaEncriptada(senha)).getResponse();

		/*
		 * String retorno = (new __HttpPostConnector()).setUrl(urlStr)
		 * .setRequestPropertyContentTypeApplicationFormUrlEncoded().setCharsetUTF8()
		 * .setParameters(new StringKeyValuePair("login", login)) .setParameters(new
		 * StringKeyValuePair("password", senhaEncriptada)) .setParameters(new
		 * StringKeyValuePair("vut", validUserToken)) .setParameters(new
		 * StringKeyValuePair("sigla", enumSistema.getSiglaSistema())
		 * 
		 * ).getStringValue();
		 */
		String retorno = response.readEntity(String.class);


		if ("INVALID".equals(retorno)) {
			throw new InvalidPasswordException();
		}
		return retorno;
	}

	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
		return readFully(inputStream).toString(encoding);
	}

	private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos;
	}

	private String getQuery(List<StringKeyValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (StringKeyValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getKey(), UTF8));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue().toString(), UTF8));
		}

		return result.toString();
	}

	private String getSenhaEncriptada(String password) {
		return DigestUtils.md5Hex(password);
	}

}
