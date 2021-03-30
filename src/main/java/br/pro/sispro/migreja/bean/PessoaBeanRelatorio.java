package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Pessoa;
import br.sispro.migreja.dao.PessoaDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped

public class PessoaBeanRelatorio implements Serializable {

	private Pessoa pessoa;
	private PessoaDAO pessoaDAO = new PessoaDAO();
	private List<Pessoa> lista = new ArrayList<Pessoa>();

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public List<Pessoa> getLista() {
		return lista;
	}

	public void setLista(List<Pessoa> lista) {
		this.lista = lista;
	}
	

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	@PostConstruct
	public void inicio() {
		try {			
			lista = pessoaDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	/*public void getRelatorio() {
		Relatorio relatorio = new Relatorio();
		relatorio.getRelatorio(lista);
	}*/

}
