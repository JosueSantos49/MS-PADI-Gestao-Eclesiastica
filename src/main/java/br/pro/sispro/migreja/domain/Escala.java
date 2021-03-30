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
/*
 * Quando for necessario cadastrar uma escala devera selecionar o membro a ser
 * escalado.
 */
@Table(name = "escala")
public class Escala extends GenericDomain {

	@Column(length = 150, nullable = false)
	private String funcao;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date inicio;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fim;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date hora;

	@Column(length = 80, nullable = false)
	private String atividade;

	@Column(length = 600, nullable = true)
	private String obs;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="pessoa",nullable = false)
	private Pessoa pessoa = new Pessoa();

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	

}
