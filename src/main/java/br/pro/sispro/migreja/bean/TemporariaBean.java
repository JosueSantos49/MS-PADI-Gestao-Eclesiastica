package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Temporaria;
import br.sispro.migreja.dao.TemporariaDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class TemporariaBean implements Serializable{

	private Temporaria temporaria;
	private List<Temporaria> temporarias;
	
	public Temporaria getTemporaria() {
		return temporaria;
	}
	public void setTemporaria(Temporaria temporaria) {
		this.temporaria = temporaria;
	}
	public List<Temporaria> getTemporarias() {
		return temporarias;
	}
	public void setTemporarias(List<Temporaria> temporarias) {
		this.temporarias = temporarias;
	}
	
	//######################## RETORNA A LISTA DO LOG ###################
	@PostConstruct
	public void listar() {
		try {
			
			TemporariaDAO temporariaDAO = new TemporariaDAO();
			temporarias = temporariaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o log!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	
	
}
