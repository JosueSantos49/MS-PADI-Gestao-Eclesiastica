package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "eventocalendario")
public class EventoCalendario extends GenericDomain {
	
	@Column(length = 80, nullable = false)
	private String titulo; 
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date inicio;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fim;
	
	@Column(length = 200, nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private Boolean status;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	} 
	
	
	
	
	
}
