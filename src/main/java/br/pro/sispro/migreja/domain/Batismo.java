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
@Table(name = "batismo")
public class Batismo extends GenericDomain{

	@Column(length = 80, nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataBastismo;
	
	@Column(length = 80, nullable = false)
	private String local;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato1;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato2;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato3;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato4;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato5;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato6;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato7;
	
	@Column(length = 200, nullable = true)
	private String pergCandidato8;
	
	@Column(length = 300, nullable = true)
	private String obs;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();
	
	@ManyToOne
	@JoinColumn(name="pessoa",nullable = false)
	private Pessoa pessoa = new Pessoa();

	@ManyToOne
	@JoinColumn(name="membro",nullable = false)
	private Membro membro = new Membro();
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataBastismo() {
		return dataBastismo;
	}

	public void setDataBastismo(Date dataBastismo) {
		this.dataBastismo = dataBastismo;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getPergCandidato1() {
		return pergCandidato1;
	}

	public void setPergCandidato1(String pergCandidato1) {
		this.pergCandidato1 = pergCandidato1;
	}

	public String getPergCandidato2() {
		return pergCandidato2;
	}

	public void setPergCandidato2(String pergCandidato2) {
		this.pergCandidato2 = pergCandidato2;
	}

	public String getPergCandidato3() {
		return pergCandidato3;
	}

	public void setPergCandidato3(String pergCandidato3) {
		this.pergCandidato3 = pergCandidato3;
	}

	public String getPergCandidato4() {
		return pergCandidato4;
	}

	public void setPergCandidato4(String pergCandidato4) {
		this.pergCandidato4 = pergCandidato4;
	}

	public String getPergCandidato5() {
		return pergCandidato5;
	}

	public void setPergCandidato5(String pergCandidato5) {
		this.pergCandidato5 = pergCandidato5;
	}

	public String getPergCandidato6() {
		return pergCandidato6;
	}

	public void setPergCandidato6(String pergCandidato6) {
		this.pergCandidato6 = pergCandidato6;
	}

	public String getPergCandidato7() {
		return pergCandidato7;
	}

	public void setPergCandidato7(String pergCandidato7) {
		this.pergCandidato7 = pergCandidato7;
	}

	public String getPergCandidato8() {
		return pergCandidato8;
	}

	public void setPergCandidato8(String pergCandidato8) {
		this.pergCandidato8 = pergCandidato8;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}
	
	
}
