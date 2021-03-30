package br.pro.sispro.migreja.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Pedido;
import br.sispro.migreja.dao.PedidoDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class PedidoHistoricoBean implements Serializable {
	private Pedido pedido;
	private Boolean exibePainelDados;

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Boolean getExibePainelDados() {
		return exibePainelDados;
	}

	public void setExibePainelDados(Boolean exibePainelDados) {
		this.exibePainelDados = exibePainelDados;
	}

	@PostConstruct
	public void novo() {
		pedido = new Pedido();
		exibePainelDados = false;
	}

	public void buscar() {
		try {
			PedidoDAO pedidoDAO = new PedidoDAO();
			Pedido resultado = pedidoDAO.buscar(pedido.getCodigo());

			if (resultado == null) {
				exibePainelDados = false;
				Messages.addGlobalWarn("Não existe pedido cadastrado para o código informado!");
			} else {
				exibePainelDados = true;
				pedido = resultado;
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar buscar o pedido!");
			erro.printStackTrace();
		}
	}
}
