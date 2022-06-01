package br.com.develop.yustar.rs.connector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import br.com.develop.yustar.rs.utils.StringKeyValuePair;

public class HttpPostConnector implements Serializable {

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
	private List<StringKeyValuePair> params;

	public HttpPostConnector() {
		super();
		this.url = "";
		this.path = "";
		this.context = "";
		this.parameter = "";
		this.mediaType = MediaType.TEXT_PLAIN_TYPE;
		this.params = new ArrayList<>();
	}

	public HttpPostConnector setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpPostConnector setParameters(StringKeyValuePair valuePair) {
		this.params.add(valuePair);
		return this;
	}

	public HttpPostConnector setContext(String context) {
		this.context = context;
		return this;
	}

	public HttpPostConnector setGetParameter(String parameter) {
		this.parameter = this.parameter + "/" + parameter;
		return this;
	}

	public HttpPostConnector setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
		return this;
	}
	
	public HttpPostConnector addParams(String key, String value) {
		this.params.add(new StringKeyValuePair(key, value));
		return this;
	}

	public Response getResponse() {
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(this.url);
		Form form = new Form();
		for (StringKeyValuePair vp : params) {
			form.param(vp.getKey(), (String) vp.getValue());
		}
		Entity<Form> entity = Entity.form(form);
		Response response = target.request(this.mediaType).acceptEncoding("UTF-8").header("Content-Type", this.mediaType.toString() + ";charset=UTF-8" ).post(entity);
 		return response;
		
		
				/*		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(this.url);
		for (StringKeyValuePair vp : params) {
			target.queryParam(vp.getKey(), vp.getValue());
		}
		
		Invocation.Builder builder = target.request().header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
		for (StringKeyValuePair vp : params) {
			builder.property(vp.getKey(), vp.getValue());
		}
		return builder.post(Entity.text("")); */
		
/*
		this.path = path + context + parameter + "/";

		WebTarget methodTarget = target.path(path);
		
		methodTarget.request().header("", "").post
		
		methodTarget.request().accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
		this.builder = methodTarget.request(this.mediaType).accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN).;
		return this; */
	}

	public static void main(String[] args) {
		HttpPostConnector conn = new HttpPostConnector();
		conn.setUrl("http://localhost:8080/yustarback/rest/auth/getauthtoken");
		conn.addParams("admin", "21232f297a57a5a743894a0e4a801fc3");
		Response resp = conn.getResponse();
		System.out.println(resp.getStatus());
	}
}
