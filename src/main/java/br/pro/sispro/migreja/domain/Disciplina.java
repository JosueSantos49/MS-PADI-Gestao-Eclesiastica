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
@Table(name = "disciplina")
public class Disciplina extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String nomeDisciplina;
	
	@Column(length = 80, nullable = false)
	private String caragaHoraria;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date inicioAula;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date terminoAula;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="professor", nullable = false)
	private Professor professor = new Professor();

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getCaragaHoraria() {
		return caragaHoraria;
	}

	public void setCaragaHoraria(String caragaHoraria) {
		this.caragaHoraria = caragaHoraria;
	}

	public Date getInicioAula() {
		return inicioAula;
	}

	public void setInicioAula(Date inicioAula) {
		this.inicioAula = inicioAula;
	}

	public Date getTerminoAula() {
		return terminoAula;
	}

	public void setTerminoAula(Date terminoAula) {
		this.terminoAula = terminoAula;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	
}
