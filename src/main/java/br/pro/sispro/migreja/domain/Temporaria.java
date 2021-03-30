package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "temporaria")
public class Temporaria extends GenericDomain{

	@Column(length = 200, nullable = true)
	private String sessao;
	
	@Column(length = 200, nullable = true)
	private String ip;
	
	@Column(length = 200, nullable = true)
	private String acao;
	
	@Column(length = 200, nullable = true)
	private String nomeTabela;
	
	@Column(nullable = true)
	private Long codigoRegistro;
	
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public Long getCodigoRegistro() {
		return codigoRegistro;
	}

	public void setCodigoRegistro(Long codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
}
