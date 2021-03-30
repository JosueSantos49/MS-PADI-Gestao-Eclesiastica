package br.pro.sispro.migreja.bean;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.UploadFile;
import br.sispro.migreja.dao.UploadFileDAO;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class UploadBean implements Serializable{

	private UploadFile uploadFile = new UploadFile();
	private List<UploadFile> uploadFiles;
	
	UploadFileDAO uploadFileDAO = new UploadFileDAO();
	
	private javax.servlet.http.Part file;

	public List<UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
	

	public UploadFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	@PostConstruct
	public void listar() {
		try {
			UploadFileDAO uploadFileDAO = new UploadFileDAO();
			uploadFiles = uploadFileDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o arquivo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void novo() {
		try {
			uploadFile = new UploadFile();
			
			UploadFileDAO uploadFileDAO = new UploadFileDAO();
			uploadFiles = uploadFileDAO.listar();

			uploadFiles = new ArrayList<UploadFile>(); 
			uploadFiles = uploadFileDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo upload!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void upload() {
		try {
						
			if (file != null) {
				
				InputStream is = file.getInputStream();
				
				//código usando Apache Commons IO
				byte[] bytes = IOUtils.toByteArray(is);
				uploadFile.setImage(bytes);
				
				uploadFileDAO.merge(uploadFile); 
				
				uploadFiles = uploadFileDAO.listar();				
				
				FacesMessage message = new FacesMessage("Exito", uploadFile.getNome() + uploadFile.getImage() + "arquivo executado.");
				FacesContext.getCurrentInstance().addMessage(null, message);				
				
			}
		} catch (Exception erro) {
			erro.printStackTrace();
			FacesMessage message = new FacesMessage("Erro ao executar upload do arquivo!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
		
	public void excluir(ActionEvent evento) {
		try {
			uploadFile = (UploadFile) evento.getComponent().getAttributes().get("uploadSelecionado");

			UploadFileDAO uploadFileDAO = new UploadFileDAO();
			uploadFileDAO.excluir(uploadFile);

			uploadFiles = uploadFileDAO.listar();

			Messages.addGlobalInfo("Arquivo exlcuído com sucesso!");
			System.out.println();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um arquivo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void editar(ActionEvent evento) {
		try {
			uploadFile = (UploadFile) evento.getComponent().getAttributes().get("uploadSelecionado");
			
			UploadFileDAO uploadFileDAO = new UploadFileDAO();
			uploadFiles = uploadFileDAO.listar();
			
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um arquivo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	 public void limparCampo() {
	        uploadFile = new UploadFile();
	         
	        FacesMessage msg = new FacesMessage("Limpar Campos executado!");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }

	public UploadFileDAO getUploadFileDAO() {
		return uploadFileDAO;
	}

	public void setUploadFileDAO(UploadFileDAO uploadFileDAO) {
		this.uploadFileDAO = uploadFileDAO;
	}

	public javax.servlet.http.Part getFile() {
		return file;
	}

	public void setFile(javax.servlet.http.Part file) {
		this.file = file;
	}
	
	
}
