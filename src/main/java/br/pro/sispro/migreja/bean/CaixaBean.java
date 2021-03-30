package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import br.pro.sispro.migreja.domain.Caixa;
import br.pro.sispro.migreja.domain.Funcionario;
import br.sispro.migreja.dao.CaixaDAO;
import br.sispro.migreja.dao.FuncionarioDAO;

@SuppressWarnings("serial")
@ManagedBean 
@ViewScoped
public class CaixaBean implements Serializable {

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

	/*@PostConstruct
	public void listar() {
		try {
			CaixaDAO caixaDAO = new CaixaDAO();
			caixas = caixaDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}*/

	@PostConstruct
	public void listarSchedule(){
		listagemCaixas = new DefaultScheduleModel();
	}
	
	public void novo(SelectEvent evento) {
	
		caixa = new Caixa();
		caixa.setDataAbertura((Date)evento.getObject());
		
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		funcionarios = funcionarioDAO.listar();
		
	}

	public void salvar(){
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(caixa.getDataAbertura());
			calendar.add(Calendar.DATE, 1);
			caixa.setDataAbertura(calendar.getTime());
			
			CaixaDAO caixaDAO = new CaixaDAO();
			caixaDAO.salvar(caixa);

			Messages.addGlobalInfo("Caixa aberto com sucesso!");

	}

	/*public void excluir(ActionEvent evento) {
		try {
			caixa = (Caixa) evento.getComponent().getAttributes().get("caixaSelecionado");

			CaixaDAO caixaDAO = new CaixaDAO();
			caixaDAO.excluir(caixa);

			caixas = caixaDAO.listar();

			Messages.addGlobalInfo("Data de Abertura: " + caixa.getDataAbertura());
			System.out.println();
			Messages.addGlobalInfo("Data de Fechamento: " + caixa.getDataFechamento());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o cliente!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}*/

	/*public void editar(ActionEvent evento) {
		caixa = (Caixa) evento.getComponent().getAttributes().get("caixaSelecionado");
	}*/
	
}
