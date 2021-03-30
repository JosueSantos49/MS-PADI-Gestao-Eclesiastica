/* 
	Autor: Josue da Conceicao Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2016
 */
package br.pro.sispro.migreja.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "cidade")
public class Cidade extends GenericDomain {

	@Column(length = 50, nullable = false)
	private String nome;
	
	//Chave estrangeira composição
	@ManyToOne
	@JoinColumn(name="estado", nullable = false)
	private Estado estado = new Estado();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
		
		
}
