package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.model.ScheduleModel;

import br.pro.sispro.migreja.domain.Caixa;
import br.pro.sispro.migreja.domain.Funcionario;
import br.sispro.migreja.dao.CaixaDAO;
import br.sispro.migreja.dao.FuncionarioDAO;

@SuppressWarnings("serial")
@ManagedBean 
@ViewScoped
public class CaixaBean2 implements Serializable {

	private Caixa caixa;
	private List<Caixa> caixas;
	private ScheduleModel listagemCaixas;
	private List<Funcionario> funcionarios;

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public List<Caixa> getCaixas() {
		return caixas;
	}

	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public ScheduleModel getListagemCaixas() {
		return listagemCaixas;
	}

	public void setListagemCaixas(ScheduleModel listagemCaixas) {
		this.listagemCaixas = listagemCaixas;
	}	

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	@PostConstruct
	public void listar() {
		try {
			CaixaDAO caixaDAO = new CaixaDAO();
			caixas = caixaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void salvar() {
		try {
			CaixaDAO caixaDAO = new CaixaDAO();
			caixaDAO.merge(caixa);

			caixa = new Caixa();
			caixas = caixaDAO.listar();

			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listar();			

			Messages.addGlobalInfo("Salvo com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	
	public void excluir(ActionEvent evento) {
		try {
			caixa = (Caixa) evento.getComponent().getAttributes().get("caixaSelecionado");

			CaixaDAO caixaDAO = new CaixaDAO();
			caixaDAO.excluir(caixa);

			caixas = caixaDAO.listar();

			Messages.addGlobalInfo("Data de Abertura: " + caixa.getDataAbertura());
			System.out.println();
			Messages.addGlobalInfo("Funcionário: " + caixa.getFuncionario().getPessoa().getNome());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void editar(ActionEvent evento) {
		try {
			caixa = (Caixa) evento.getComponent().getAttributes().get("caixaSelecionado");

			CaixaDAO caixaDAO = new CaixaDAO();
			caixas = caixaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
}
