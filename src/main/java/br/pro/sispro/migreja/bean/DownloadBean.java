package br.pro.sispro.migreja.bean;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.pro.sispro.migreja.domain.UploadFile;
import br.pro.sispro.migreja.util.Conexao;

@ManagedBean
@SessionScoped
public class DownloadBean {

	private StreamedContent file;
	private int codigo;
	private UploadFile uploadFile;

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public UploadFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public StreamedContent fazerDownload(UploadFile uploadFile) {
		return new DefaultStreamedContent(new ByteArrayInputStream(uploadFile.getImage()),"attachment;filename=",uploadFile.getNome());	
	}	  	
	
	public void download() {
		ResultSet rs;
		 byte[] bytes = null;
         String fileName = "";
		try {

			Connection con = Conexao.getConnection();

			PreparedStatement st = con.prepareStatement("SELECT image, nome FROM uploadfile WHERE codigo = (?) ");
			st.setInt(1, codigo);

			rs = st.executeQuery();
			while (rs.next()) {
				bytes = rs.getBytes("image");
				fileName = rs.getString("nome");
			}
			con.close();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            

			FacesMessage message = new FacesMessage("Exito", " File descarregado com sucesso.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception erro) {
			erro.printStackTrace();
			FacesMessage message = new FacesMessage("Erro ao executar download da image.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/*
	// Metodo original
		public void download() {
			ResultSet rs;
			try {

				Connection con = Conexao.getConnection();

				PreparedStatement st = con.prepareStatement("SELECT image FROM uploadfile WHERE codigo = (?) ");
				st.setInt(1, codigo);

				rs = st.executeQuery();
				while (rs.next()) {
					InputStream stream = rs.getBinaryStream("image");
					file = new DefaultStreamedContent(stream, "image/jpg", "Arquivo.pdf");
				}
				con.close();

				FacesMessage message = new FacesMessage("Exito", " File descarregado com sucesso.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception erro) {
				erro.printStackTrace();
				FacesMessage message = new FacesMessage("Erro ao executar download da image.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}*/
	
	
    

	public void ver() {
		ResultSet rs;
		try {
			byte[] bytes = null;
			Connection con = Conexao.getConnection();

			PreparedStatement st = con.prepareStatement("SELECT image FROM uploadfile WHERE codigo = (?)");
			st.setInt(1, codigo);

			rs = st.executeQuery();
			while (rs.next()) {
				bytes = rs.getBytes("image");
			}
			con.close();

			FacesMessage message = new FacesMessage("Exito", " File descarregado com sucesso.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.getOutputStream().write(bytes);
			response.getOutputStream().close();

			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception erro) {
			erro.printStackTrace();
			FacesMessage message = new FacesMessage("Erro ao executar download da image.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

}
