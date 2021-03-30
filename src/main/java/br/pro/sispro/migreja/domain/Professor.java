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
@Table(name = "professor")
public class Professor extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String titulacao;

	@Column(length = 80, nullable = false)
	private String intituicaoTitulacao;

	@Column(length = 50, nullable = false)
	private String cursoArea;

	@Column(length = 4, nullable = false)
	private String anoConclusao;

	@Column(length = 3, nullable = true)
	private String carteiraTrabalho;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataAdmissao;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(length = 600, nullable = true)
	private String obs;

	@ManyToOne
	@JoinColumn(name="curso", nullable = false)
	private Curso curso = new Curso();

	@ManyToOne
	@JoinColumn(name="pessoa", nullable = false)
	private Pessoa pessoa = new Pessoa();

	public String getTitulacao() {
		return titulacao;
	}

	public void setTitulacao(String titulacao) {
		this.titulacao = titulacao;
	}

	public String getIntituicaoTitulacao() {
		return intituicaoTitulacao;
	}

	public void setIntituicaoTitulacao(String intituicaoTitulacao) {
		this.intituicaoTitulacao = intituicaoTitulacao;
	}

	public String getCursoArea() {
		return cursoArea;
	}

	public void setCursoArea(String cursoArea) {
		this.cursoArea = cursoArea;
	}

	public String getAnoConclusao() {
		return anoConclusao;
	}

	public void setAnoConclusao(String anoConclusao) {
		this.anoConclusao = anoConclusao;
	}

	public String getCarteiraTrabalho() {
		return carteiraTrabalho;
	}

	public void setCarteiraTrabalho(String carteiraTrabalho) {
		this.carteiraTrabalho = carteiraTrabalho;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

}
