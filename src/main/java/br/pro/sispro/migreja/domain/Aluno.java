package br.pro.sispro.migreja.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "aluno")
public class Aluno extends GenericDomain{

	@ManyToOne
	@JoinColumn(name="pessoa",nullable = false)
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="turma",nullable = false)
	private Turma turma;

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	
}
