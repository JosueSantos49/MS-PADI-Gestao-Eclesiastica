package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "pessoa")
public class Pessoa extends GenericDomain{

	@Column(length = 150, nullable = false)
	private String nome;

	@Column(length = 14, nullable = false)
	private String cpf;

	@Column(length = 12, nullable = false)
	private String id;
	
	@Column(length = 200, nullable = false)
	private String rua;

	@Column(length = 20, nullable = false)
	private String numero;

	@Column(length = 150, nullable = true)
	private String bairro;

	@Column(length = 9, nullable = false)
	private String cep;

	@Column(length = 150, nullable = true)
	private String complemento;

	@Column(length = 14, nullable = true)
	private String telefone;

	@Column(length = 15, nullable = true)
	private String celular;

	@Column(length = 200, nullable = true)
	private String email;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataAniversario;
	
	// Chave estrangeira composição. Relacionamento (1:N), uma cidade pode ter muitas
	//pessoas e uma pessoa pode pertencer a uma cidade.
	@ManyToOne
	@JoinColumn(name="cidade",nullable = false)//obrigatorio
	private Cidade cidade = new Cidade();
	
	//Campo transiente. Armazena o caminho do arquivo temporario.
	@Transient
	private String caminho;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Date getDataAniversario() {
		return dataAniversario;
	}

	public void setDataAniversario(Date dataAniversario) {
		this.dataAniversario = dataAniversario;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	

}
