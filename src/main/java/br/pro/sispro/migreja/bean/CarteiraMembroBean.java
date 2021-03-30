package br.pro.sispro.migreja.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Membro;
import br.sispro.migreja.dao.MembroDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CarteiraMembroBean implements Serializable{
	
	private Membro membro;
	private Boolean exibirPainelDados;
	
	public Membro getMembro() {
		return membro;
	}
	public void setMembro(Membro membro) {
		this.membro = membro;
	}
	public Boolean getExibirPainelDados() {
		return exibirPainelDados;
	}
	public void setExibirPainelDados(Boolean exibirPainelDados) {
		this.exibirPainelDados = exibirPainelDados;
	}
	
	@PostConstruct
	public void novo(){
		membro = new Membro();
		exibirPainelDados = false;
	}
	
	public void buscar(){
		try {
			MembroDAO membroDAO = new MembroDAO();
			Membro resultado = membroDAO.buscar(membro.getCodigo());

			if (resultado == null) {
				exibirPainelDados = false;
				Messages.addGlobalWarn("Não existe membro cadastrado para o código informado!");
			} else {
				exibirPainelDados = true;
				membro = resultado;
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar buscar o membro!");
			erro.printStackTrace();
		}
	}
		
}
