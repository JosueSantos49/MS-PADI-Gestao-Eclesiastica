/* 
	Autor: Josu� da Concei��o Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2015
 */
package br.pro.sispro.migreja.util;

import javax.mail.*;

public class Autenticar extends Authenticator {
	private String usuario;
	private String senha;
	
	public Autenticar(){}
	
	public Autenticar(String usuario, String senha){
		this.usuario = usuario;
		this.senha = senha;
	}
	
	public PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(usuario,senha);	
	}
}
