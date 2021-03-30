package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "pedido")
public class Pedido extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String assunto;

	@Column(length = 80, nullable = false)
	private String descricao;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataSolicitacao;

	@Column(length = 80, nullable = false)
	private String statusPedido;
	
	@Column(length = 80, nullable = false)
	private String prioridade;

	@Column(length = 80, nullable = false)
	private String email;

	@Column(length = 500, nullable = false)
	private String obs;
	
	@OneToOne
	@JoinColumn(name="membro", nullable = false)
	private Membro membro;

	@OneToOne
	@JoinColumn(name="departamento",nullable = false)
	private Departamento departamento = new Departamento();
	
	@OneToOne
	@JoinColumn(name="pessoa",nullable = false)
	private Pessoa pessoa = new Pessoa();

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public String getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(String statusPedido) {
		this.statusPedido = statusPedido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public String getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	

}
