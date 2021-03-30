/* 
	Autor: Josue da Conceição Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2016
 */
package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name="instituicao")
public class Instituicao extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String razaosocial;

	@Column(length = 18, nullable = false)
	private String cnpj;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(length = 80, nullable = false)
	private String endereco;

	@Column(length = 50, nullable = true)
	private String complemento;

	@Column(length = 14, nullable = true)
	private String telefone;

	@Column(length = 150, nullable = true)
	private String fundador;

	@Column(length = 9, nullable = true)
	private String cep;

	@Column(length = 50, nullable = true)
	private String email;

	@Column(length = 35, nullable = true)
	private String site;

	@ManyToOne
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade= new Cidade();

	public String getRazaosocial() {
		return razaosocial;
	}

	public void setRazaosocial(String razaosocial) {
		this.razaosocial = razaosocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getFundador() {
		return fundador;
	}

	public void setFundador(String fundador) {
		this.fundador = fundador;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
