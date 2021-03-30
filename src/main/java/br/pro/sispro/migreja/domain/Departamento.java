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
@Table(name = "departamento")
public class Departamento extends GenericDomain {

	@Column(length = 80, nullable = false)
	private String nomeDepartamento;

	@Column(length = 80, nullable = false)
	private String presidente;

	@Column(length = 80, nullable = true)
	private String vicePresidente;

	@Column(length = 80, nullable = true)
	private String secretario;

	@Column(length = 80, nullable = true)
	private String auxiliar;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(length = 600, nullable = true)
	private String obs;

	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();

	@ManyToOne
	@JoinColumn(name="pessoa",nullable = false)
	private Pessoa pessoa = new Pessoa();

	public String getNomeDepartamento() {
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}

	public String getPresidente() {
		return presidente;
	}

	public void setPresidente(String presidente) {
		this.presidente = presidente;
	}

	public String getVicePresidente() {
		return vicePresidente;
	}

	public void setVicePresidente(String vicePresidente) {
		this.vicePresidente = vicePresidente;
	}

	public String getSecretario() {
		return secretario;
	}

	public void setSecretario(String secretario) {
		this.secretario = secretario;
	}

	public String getAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(String auxiliar) {
		this.auxiliar = auxiliar;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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

}
