package br.pro.sispro.migreja.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.omnifaces.util.Messages;
import org.springframework.scheduling.annotation.Scheduled;

import br.pro.sispro.migreja.domain.Mensagem;
import br.pro.sispro.migreja.domain.Pessoa;

public class EmailUtils {

	private static final String HOSTNAME = "sisproweb.com.br";
	private static final String USERNAME = "sispro@sisproweb.com.br";
	private static final String PASSWORD = "josuerj4929091988";
	private static final String EMAILORIGEM = "sispro@sisproweb.com.br";

	@SuppressWarnings("deprecation")
	public static Email conectaEmail() throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		email.setTLS(true);
		email.setFrom(EMAILORIGEM);
		return email;
	}

	@SuppressWarnings("unused")
	public static void enviaEmail(Mensagem mensagem) throws EmailException {
		Email email = new SimpleEmail();
		email = conectaEmail();
		email.setSubject(mensagem.getTitulo());
		email.setMsg(mensagem.getMensagem()+"text/html; charset=UTF-8");
		email.addTo(mensagem.getDestino());
		String resposta = email.send();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"E-mail enviado com sucesso para: " + mensagem.getDestino(), "Informação"));
	}

	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 10000000)
	public static void enviaEmailPessoa(Pessoa pessoa) throws EmailException {
		try {
			Email email = new SimpleEmail();
			email = conectaEmail();
			email.setFrom("conceicaojosue@outlook.com.br");
			email.setSubject("Pessoa cadastrada com sucesso!");
			email.setMsg("Mensagem automatica de confirmação de cadastro de 'Pessoa'!"+"text/html; charset=UTF-8");
			email.addTo("conceicaojosue@yahoo.com.br");

			String resposta = email.send();

			/*FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"E-mail enviado com sucesso para: " + email.addTo("conceicaojosue@yahoo.com.br"), "Informação"));*/
			
			Messages.addGlobalInfo("E-mail Enviado: conceicaojosue@yahoo.com.br");
			
		} catch (Exception erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar envia email da pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 10000000)
	public static void enviaEmailConfirmaCadastro(Pessoa pessoa, String nome, String emaill) {
		try {
			Email email = new SimpleEmail();
			email = conectaEmail();
			email.setFrom(emaill);
			email.setSubject("Sistema Monitormaneto Para Administração de Igrejas - Confirmação de Cadastro!");
			email.setMsg("Olá, "+pessoa.getNome()+", seja bem vindo! Você, foi cadastrado no sistema."
					+ "\r\n Segue, o e-mail de cadastro:" 
					+ "\r\n Usuário: "+emaill+ ""
					+ "\r\n text/html; charset=UTF-8");
			email.addTo(emaill);

			String resposta = email.send();

			/*FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"E-mail enviado: " + email.addTo(emaill), "Informação"));*/
			
			Messages.addGlobalInfo("E-mail Enviado: "+emaill);
			
		} catch (Exception erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar envia email de confirma de cadastro pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}		
	}
	
}
