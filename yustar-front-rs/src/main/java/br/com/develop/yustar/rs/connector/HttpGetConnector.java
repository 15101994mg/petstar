package br.com.develop.yustar.rs.connector;

import java.io.Serializable;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HttpGetConnector implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String path;
	private MediaType mediaType;
	Invocation.Builder builder;
	private String context;
	private String parameter;

	public HttpGetConnector() {
		super();
		this.url = "";
		this.path = "";
		this.context = "";
		this.parameter = "";
		this.mediaType = MediaType.TEXT_PLAIN_TYPE;
	}

	public Response getResponse() {
		return builder.get();
	}

	public HttpGetConnector setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpGetConnector setContext(String context) {
		this.context = context;
		return this;
	}

	public HttpGetConnector setGetParameter(String parameter) {
		this.parameter = this.parameter + "/" + parameter;
		return this;
	}

	public HttpGetConnector setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public HttpGetConnector build() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(this.url);

		this.path = path + context + parameter + "/";

		WebTarget methodTarget = target.path(path);
		methodTarget.request().accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
		this.builder = methodTarget.request(this.mediaType).accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
		return this;
	}

}
