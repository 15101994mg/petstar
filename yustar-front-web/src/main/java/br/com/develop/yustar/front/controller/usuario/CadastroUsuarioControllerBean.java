package br.com.develop.yustar.front.controller.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.develop.yustar.front.rs.usuario.UsuarioClient;
import br.com.develop.yustar.rs.model.BairroDTO;

@Named(value = CadastroUsuarioControllerBean.CDI_NAME)
@ViewScoped
public class CadastroUsuarioControllerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CDI_NAME = "cadastroUsuarioControllerBean";

	private String TIPO_USUARIO_CLIENTE = "1";

	private String TIPO_USUARIO_FORNECEDOR = "2";

	private String TIPO_FORNECEDOR_PESSOA_FISICA = "Pessoa Física";
	private String TIPO_FORNECEDOR_PESSOA_JURIDICA = "Pessoa Jurídica";

	@Inject
	private UsuarioClient usuClient;

	private String tipoUsuario;

	private boolean selecaoTipoClienteInvalida;

	private String nome;

	private String nomeSocial;

	private String login;

	private String email;

	private String cep;

	private String cpf;

	private String cnpj;

	private String endereco;

	private String sexo;

	private String senha;

	private String tipoFornecedor;

	private List<BairroDTO> bairros;

	private BairroDTO bairro;

	private String ddd;

	private String numeroTelefone;

	public CadastroUsuarioControllerBean() {
		super();
		this.tipoUsuario = TIPO_USUARIO_CLIENTE;
		this.sexo = "Masculino";
		this.tipoFornecedor = "Pessoa Física";
	}

	@PostConstruct
	public void inicializar() {
		this.bairros = usuClient.obterTodosBairros();
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public String obterTipoCadastro() {
		if (TIPO_USUARIO_CLIENTE.equals(this.tipoUsuario)) {
			return "Cadastrando Cliente...";
		}
		return "Cadastrando Fornecedor(Prestador de serviço)...";
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public boolean isSelecaoTipoClienteInvalida() {
		return selecaoTipoClienteInvalida;
	}

	public void setSelecaoTipoClienteInvalida(boolean selecaoTipoClienteInvalida) {
		this.selecaoTipoClienteInvalida = selecaoTipoClienteInvalida;
	}

	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String gravarUsuario() throws Exception {
		try {
			if (TIPO_USUARIO_CLIENTE.contentEquals(tipoUsuario)) {
				usuClient.cadastrarCliente(nome, nomeSocial, login, email, cep, endereco, cpf, sexo, senha, bairro, ddd, numeroTelefone);

			} else {
				usuClient.cadastrarFornecedor(nome, nomeSocial, login, email, cep, endereco, cpf, sexo, cnpj,
						TIPO_FORNECEDOR_PESSOA_FISICA.equals(tipoFornecedor) ? "1" : "2", senha, bairro, ddd, numeroTelefone);

			}

			return "LOGIN";
		} catch (Exception e) {
			throw new Exception("erro ao cadastrar cliente");
		}

	}

	public List<BairroDTO> obterBairros(String valor) {
		return this.bairros.stream().filter(b -> b.getNome().toUpperCase().contains(valor.toUpperCase()))
				.collect(Collectors.toList());
	}

	public boolean cadastrandoComoCliente() {
		return TIPO_USUARIO_CLIENTE.equals(tipoUsuario);
	}

	public void seguirPasso2() {
		this.selecaoTipoClienteInvalida = true;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(String tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public boolean cadastrandoCPFValido() {
		return cadastrandoComoCliente() || TIPO_FORNECEDOR_PESSOA_FISICA.equals(this.tipoFornecedor);
	}

	public boolean cadastrandoCPNJValido() {
		return !cadastrandoComoCliente() && TIPO_FORNECEDOR_PESSOA_JURIDICA.equals(this.tipoFornecedor);
	}

	public List<BairroDTO> getBairros() {
		return bairros;
	}

	public void setBairros(List<BairroDTO> bairros) {
		this.bairros = bairros;
	}

	public BairroDTO getBairro() {
		return bairro;
	}

	public void setBairro(BairroDTO bairro) {
		this.bairro = bairro;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

}
