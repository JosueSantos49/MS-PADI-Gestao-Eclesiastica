package br.pro.sispro.migreja.bean;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.pro.sispro.migreja.util.Autenticar;

public class EnviarEmail {
	
	private String de;
	private String para;
	private String assunto;
	private String mensagem;
	
	private String email; 
	
	
	private static Properties props=null;
	private static Session session = null;
	
	/*Esta classe é especialista em executar envio de e-mail, onde, cada método executa suas necessidades.
	 * Atenção caso seja necessário configurar a propriedade de acesso ao servidor de hospedagem basta somente definir as portas abaixo
	 de acordo com o host, port e senha de autenticação.
	 * */
	static{
		props = System.getProperties();
		props.put("mail.smtp.host", "sisproweb.com.br");
		//props.put("mail.smtp.port", "25");//servidor online
		props.put("mail.smtp.port", "587");//servidor local
		props.put("mail.smtp.auth", "true");
		session = Session.getInstance(props, new Autenticar("sispro@sisproweb.com.br","josuerj49sispro27") //Seu e-mail e senha, criado no cPanel hospedagem.
		);
	}	
	public boolean enviar(){
		try{
			MimeMessage message = new MimeMessage(session); //cria a mensagem setando o remetente e seus destinatários
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(this.email)); // para - setamos o 1° destinatario
			message.setFrom(new InternetAddress(this.email));//de - seta remetente
			message.setSubject(this.email);//assunto
			//message.addRecipient(Message.RecipientType.CC, new InternetAddress (this.lista));//setando demais destinatários	CC		
			message.setSentDate(new Date());//data			
			message.setContent(this.email,"text/html; charset=ISO-8859-1");//mensagem
						
			Transport.send(message);
			
			return true;			
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	public String getDe() {
		return de;
	}
	public void setDe(String de) {
		this.de = de;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
