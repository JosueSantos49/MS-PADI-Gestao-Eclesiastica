package br.pro.sispro.migreja.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Pessoa;
import br.sispro.migreja.dao.PessoaDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class PessoaBean2 implements Serializable {
	private Pessoa pessoa;
	private Boolean exibePainelDados;

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Boolean getExibePainelDados() {
		return exibePainelDados;
	}

	public void setExibePainelDados(Boolean exibePainelDados) {
		this.exibePainelDados = exibePainelDados;
	}

	@PostConstruct
	public void novo() {
		pessoa = new Pessoa();
		exibePainelDados = false;
	}

	public void buscar() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			Pessoa resultado = pessoaDAO.buscar(pessoa.getCodigo());

			if (resultado == null) {
				exibePainelDados = false;
				Messages.addGlobalWarn("Não existe pessoa cadastrada para o código informado!");
			} else {
				exibePainelDados = true;
				pessoa = resultado;
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar buscar a pessoa!");
			erro.printStackTrace();
		}
	}
}
