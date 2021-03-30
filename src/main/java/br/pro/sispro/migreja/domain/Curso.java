package br.pro.sispro.migreja.domain;

import java.math.BigDecimal;
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
@Table(name = "curso")
public class Curso extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String nomeCurso;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date periodoInicio;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date periodoTermino;
	
	@Column(length = 80, nullable = false)
	private String especialidade;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();
	
	@ManyToOne
	@JoinColumn(name="departamento", nullable = false)
	private Departamento departamento = new Departamento();
	
	@Column(nullable = false, precision = 6, scale = 2)
	private BigDecimal valorCurso;

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoTermino() {
		return periodoTermino;
	}

	public void setPeriodoTermino(Date periodoTermino) {
		this.periodoTermino = periodoTermino;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public BigDecimal getValorCurso() {
		return valorCurso;
	}

	public void setValorCurso(BigDecimal valorCurso) {
		this.valorCurso = valorCurso;
	}
	
	
}
