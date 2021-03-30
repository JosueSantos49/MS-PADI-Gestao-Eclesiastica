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
@Table(name = "perfilinstituicao")
public class PerfilInstituicao extends GenericDomain{

	@Column(length = 80, nullable = false)
	private String dependencia;
	
	@Column(length = 80, nullable = true)
	private String salaLeitura;
	
	@Column(length = 80, nullable = true)
	private String salaTv;
	
	@Column(length = 80, nullable = true)
	private String bercario;
	
	@Column(length = 80, nullable = true)
	private String salaAdolecente;
	
	@Column(length = 80, nullable = true)
	private String salaJovens;
	
	@Column(length = 80, nullable = true)
	private String salaConvertidos;
	
	@Column(length = 80, nullable = true)
	private String salaTematicos;
	
	@Column(length = 80, nullable = true)
	private String almoxarifado;
	
	@Column(length = 80, nullable = true)
	private String areaCirculacao;
	
	@Column(length = 80, nullable = true)
	private String refeitorio;
	
	@Column(length = 80, nullable = true)
	private String sanitario;
	
	@Column(length = 80, nullable = true)
	private String salao;
	
	@Column(length = 600, nullable = true)
	private String obs;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();
	
	@ManyToOne
	@JoinColumn(name="pessoa", nullable = false)
	private Pessoa pessoa = new Pessoa();

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getSalaLeitura() {
		return salaLeitura;
	}

	public void setSalaLeitura(String salaLeitura) {
		this.salaLeitura = salaLeitura;
	}

	public String getSalaTv() {
		return salaTv;
	}

	public void setSalaTv(String salaTv) {
		this.salaTv = salaTv;
	}

	public String getBercario() {
		return bercario;
	}

	public void setBercario(String bercario) {
		this.bercario = bercario;
	}

	public String getSalaAdolecente() {
		return salaAdolecente;
	}

	public void setSalaAdolecente(String salaAdolecente) {
		this.salaAdolecente = salaAdolecente;
	}

	public String getSalaJovens() {
		return salaJovens;
	}

	public void setSalaJovens(String salaJovens) {
		this.salaJovens = salaJovens;
	}

	public String getSalaConvertidos() {
		return salaConvertidos;
	}

	public void setSalaConvertidos(String salaConvertidos) {
		this.salaConvertidos = salaConvertidos;
	}

	public String getSalaTematicos() {
		return salaTematicos;
	}

	public void setSalaTematicos(String salaTematicos) {
		this.salaTematicos = salaTematicos;
	}

	public String getAlmoxarifado() {
		return almoxarifado;
	}

	public void setAlmoxarifado(String almoxarifado) {
		this.almoxarifado = almoxarifado;
	}

	public String getAreaCirculacao() {
		return areaCirculacao;
	}

	public void setAreaCirculacao(String areaCirculacao) {
		this.areaCirculacao = areaCirculacao;
	}

	public String getRefeitorio() {
		return refeitorio;
	}

	public void setRefeitorio(String refeitorio) {
		this.refeitorio = refeitorio;
	}

	public String getSalao() {
		return salao;
	}

	public void setSalao(String salao) {
		this.salao = salao;
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getSanitario() {
		return sanitario;
	}

	public void setSanitario(String sanitario) {
		this.sanitario = sanitario;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
	
}
