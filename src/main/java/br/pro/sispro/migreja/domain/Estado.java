/* 
	Autor: Josué da Conceição Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2016
 */
package br.pro.sispro.migreja.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//Estado é uma entidade do Hibernate Herdando a PK de GenericDomain (herança). É usado o gerador de tabela do Hibernate

@SuppressWarnings("serial")
@Entity 
@Table(name = "estado")
public class Estado extends GenericDomain {
	
	@Column(length = 2, nullable = false)	
	private String sigla;
	
	@Column(length = 50, nullable = false)
	private String nome;
		
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
