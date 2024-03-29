package br.pro.sispro.migreja.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImagemBean {
	
	@ManagedProperty("#{param.caminho}")
	private String caminho;
	private StreamedContent foto;
	
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	public StreamedContent getFoto() throws IOException {
		
		if(caminho == null || caminho.isEmpty()){
			Path path = Paths.get("C:/Upload/branco.png");
			InputStream strem = Files.newInputStream(path);
			foto = new DefaultStreamedContent(strem);
		}else{
			Path path = Paths.get(caminho);
			InputStream strem = Files.newInputStream(path);
			foto = new DefaultStreamedContent(strem);
		}
		return foto;		
	}
	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}
}
