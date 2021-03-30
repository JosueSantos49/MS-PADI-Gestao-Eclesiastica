package br.pro.sispro.migreja.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "uploadfile")
public class UploadFile extends GenericDomain {

	@Lob
	@Column(nullable = false)
	private byte[] image;

	@Column(length = 150, nullable = true)
	private String nome;

	@Column(length = 2000, nullable = true)
	private String comentario;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	
	

}
