package br.com.develop.yustar.front.controller.main;

import javax.faces.bean.ManagedBean;

@ManagedBean( name = "mainPageController")
public class MainPageControllerBean {
	
	private String value = "teste";
	
	private String instituicao = "Escritório Zé das Couves";
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	
	

}
