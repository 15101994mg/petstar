package br.com.develop.yustar.front.rs.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.develop.yustar.front.rs.exception.BusinessFrontException;
import br.com.develop.yustar.rs.connector.HttpPostConnector;
import br.com.develop.yustar.rs.gadgets.DateFrontUtils;
import br.com.develop.yustar.rs.gadgets.JsonUtils;
import br.com.develop.yustar.rs.model.AgendaDTO;

public class AgendaClient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public List<AgendaDTO> obterTodosAgendasPorFornecedorData(int fornecedorId, Date data){
		String urlStr = "http://localhost:8080/yustarback/rest/agenda/obteragendaporfornecedor";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("fornecedorid", String.valueOf(fornecedorId))
				.addParams("datainicio",  DateFrontUtils.getStringDateTimeMillis(data) )
				.getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<List<AgendaDTO>> type = new TypeToken<List<AgendaDTO>>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		List<?> lista = gson.fromJson(retorno, type.getType());
		
		return (List<AgendaDTO>) lista;
	}

	public AgendaDTO agendar(int configuracaoServicoFornecedorId,
			int clienteId, Date dataInicioAgenda,
			String tipoPagamento) {
		
		
		String urlStr = "http://localhost:8080/yustarback/rest/agenda/agendar";
		Response response = new HttpPostConnector().setUrl(urlStr)
				.setMediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.addParams("configuracaoservicofornecedorid", String.valueOf(configuracaoServicoFornecedorId))
				.addParams("clienteid", String.valueOf(clienteId))
				.addParams("datainicio", DateFrontUtils.getStringDateTimeMillis(dataInicioAgenda))
				.addParams("tipopagamento",  tipoPagamento )
				.getResponse();
		if ( Response.Status.OK.getStatusCode() != response.getStatus() ) {
			String mensagem = response.readEntity(String.class);
			throw new BusinessFrontException(mensagem);
		}
		TypeToken<AgendaDTO> type = new TypeToken<AgendaDTO>() {
		};
		String retorno = response.readEntity(String.class);
		
		Gson gson = JsonUtils.getGson();
		
		return gson.fromJson(retorno, type.getType());
	}
	

}
