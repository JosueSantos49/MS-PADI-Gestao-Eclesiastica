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
@Table(name = "metodologia")
public class Metodologia extends GenericDomain{

	@Column(length = 80, nullable = true)
	private String literatura;
	
	@Column(length = 80, nullable = true)
	private String recursoDida;
	
	@Column(length = 80, nullable = true)
	private String dinamica;
	
	@Column(length = 80, nullable = true)
	private String trabManual;
	
	@Column(length = 80, nullable = true)
	private String recreacao;
	
	@Column(length = 80, nullable = true)
	private String ativCasa;
	
	@Column(length = 80, nullable = true)
	private String progPais;
	
	@Column(length = 80, nullable = true)
	private String cultoDomes;
	
	@Column(length = 80, nullable = true)
	private String visitacao;
	
	@Column(length = 600, nullable = true)
	private String obs;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCad;
	
	@ManyToOne
	@JoinColumn(name="curso", nullable = false)
	private Curso curso = new Curso();
	
	@ManyToOne
	@JoinColumn(name="professor", nullable = false)
	private Professor professor = new Professor();

	public String getLiteratura() {
		return literatura;
	}

	public void setLiteratura(String literatura) {
		this.literatura = literatura;
	}

	public String getRecursoDida() {
		return recursoDida;
	}

	public void setRecursoDida(String recursoDida) {
		this.recursoDida = recursoDida;
	}

	public String getDinamica() {
		return dinamica;
	}

	public void setDinamica(String dinamica) {
		this.dinamica = dinamica;
	}

	public String getTrabManual() {
		return trabManual;
	}

	public void setTrabManual(String trabManual) {
		this.trabManual = trabManual;
	}

	public String getRecreacao() {
		return recreacao;
	}

	public void setRecreacao(String recreacao) {
		this.recreacao = recreacao;
	}

	public String getAtivCasa() {
		return ativCasa;
	}

	public void setAtivCasa(String ativCasa) {
		this.ativCasa = ativCasa;
	}

	public String getProgPais() {
		return progPais;
	}

	public void setProgPais(String progPais) {
		this.progPais = progPais;
	}

	public String getCultoDomes() {
		return cultoDomes;
	}

	public void setCultoDomes(String cultoDomes) {
		this.cultoDomes = cultoDomes;
	}

	public String getVisitacao() {
		return visitacao;
	}

	public void setVisitacao(String visitacao) {
		this.visitacao = visitacao;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getDataCad() {
		return dataCad;
	}

	public void setDataCad(Date dataCad) {
		this.dataCad = dataCad;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	
}
