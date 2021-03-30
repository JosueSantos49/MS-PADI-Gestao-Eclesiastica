/* 
	Autor: Josue da Conceicao Santos
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
@Table(name = "membro")
public class Membro extends GenericDomain {

	@Column(length = 150, nullable = true)
	private String profissao;

	@Column(length = 150, nullable = false)
	private String grauInstrucao;

	@Column(length = 150, nullable = false)
	private String pai;

	@Column(length = 150, nullable = false)
	private String mae;

	@Column(length = 150, nullable = true)
	private String casamento;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataBatismo;

	@Column(length = 150, nullable = true)
	private String igrejaBatismo;

	@Column(length = 150, nullable = true)
	private String obs;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(length = 150, nullable = true)
	private String cargoMinisterial;

	@ManyToOne
	@JoinColumn(name="pessoa", nullable = false)
	private Pessoa pessoa = new Pessoa();

	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getGrauInstrucao() {
		return grauInstrucao;
	}

	public void setGrauInstrucao(String grauInstrucao) {
		this.grauInstrucao = grauInstrucao;
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae;
	}

	public String getCasamento() {
		return casamento;
	}

	public void setCasamento(String casamento) {
		this.casamento = casamento;
	}

	public String getIgrejaBatismo() {
		return igrejaBatismo;
	}

	public void setIgrejaBatismo(String igrejaBatismo) {
		this.igrejaBatismo = igrejaBatismo;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getDataBatismo() {
		return dataBatismo;
	}

	public void setDataBatismo(Date dataBatismo) {
		this.dataBatismo = dataBatismo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getCargoMinisterial() {
		return cargoMinisterial;
	}

	public void setCargoMinisterial(String cargoMinisterial) {
		this.cargoMinisterial = cargoMinisterial;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

}
