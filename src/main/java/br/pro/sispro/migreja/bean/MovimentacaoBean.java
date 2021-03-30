package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Movimentacao;
import br.sispro.migreja.dao.MovimentacaoDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class MovimentacaoBean implements Serializable {

	private Movimentacao movimentacao;
	private List<Movimentacao> movimentacaos;

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public List<Movimentacao> getMovimentacaos() {
		return movimentacaos;
	}

	public void setMovimentacaos(List<Movimentacao> movimentacaos) {
		this.movimentacaos = movimentacaos;
	}

	@PostConstruct
	public void listar() {
		try {
			MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
			movimentacaos = movimentacaoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a movimentação!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		movimentacao = new Movimentacao();
	}

	public void salvar() {
		try {
			MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
			movimentacaoDAO.merge(movimentacao);

			movimentacao = new Movimentacao();
			movimentacaos = movimentacaoDAO.listar();

			Messages.addGlobalInfo("Cliente salvo com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma movimentação!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			movimentacao = (Movimentacao) evento.getComponent().getAttributes().get("movimentacaoSelecionado");

			MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
			movimentacaoDAO.excluir(movimentacao);

			movimentacaos = movimentacaoDAO.listar();

			Messages.addGlobalInfo("Horário: " + movimentacao.getHorario());
			System.out.println();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover a movimentação!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void editar(ActionEvent evento) {
		movimentacao = (Movimentacao) evento.getComponent().getAttributes().get("movimentacaoSelecionado");
	}
}
