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
@Table(name = "reuniao")
public class Reuniao extends GenericDomain{

	@Column(length = 100, nullable = false)
	private String projeto;
	
	@Column(length = 80, nullable = false)
	private String assunto;
	
	@Temporal(TemporalType.DATE)
	private Date dataReuniao;
	
	@Column(length = 80, nullable = false)
	private String local;
	
	@Column(length = 300, nullable = true)
	private String obs;
	
	@Column(length = 80, nullable = true)
	private String email;
	
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="departamento", nullable = false)
	private Departamento departamento = new Departamento();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Date getDataReuniao() {
		return dataReuniao;
	}

	public void setDataReuniao(Date dataReuniao) {
		this.dataReuniao = dataReuniao;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	
	
	
}
