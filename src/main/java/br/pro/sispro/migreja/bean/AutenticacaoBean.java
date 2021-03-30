package br.pro.sispro.migreja.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.domain.Usuario;
import br.sispro.migreja.dao.UsuarioDAO;


@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class AutenticacaoBean implements Serializable {
	
	private Usuario usuario;
	private Usuario usuarioLogado;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public AutenticacaoBean() {
		usuario = new Usuario();
	}

	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
		usuario.setPessoa(new Pessoa());
	}

	public void autenticar() {
		
		try {
			
			//############# RECUPERANDO VALORES DO FORM ##################
			String usuarioInformado = getUsuario().getPessoa().getCpf();
			String senhaUsuario = getUsuario().getSenha();
			
			//############# VALIDAÇÃO DE CAMPOS ############
			if(usuarioInformado != null && usuarioInformado.isEmpty()) {
				Messages.addGlobalError("Informar o usuário!");				
				return;
			}
			if(senhaUsuario != null && senhaUsuario.isEmpty()) {
				Messages.addGlobalError("Informar a senha!");				
				return;
			}			
			
			//############## VERIFICA A AUTENTICAÇÃO #######################
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarioLogado = usuarioDAO.autenticar(usuario.getPessoa().getCpf(), usuario.getSenha());

			if (usuarioLogado == null) {
				Messages.addGlobalError("CPF e/ou senha incorretos!");
				return;
			}

			// Jogando o usuario na sessao
			HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			sessao.setAttribute("usuarioLogado", this.usuarioLogado);
			//Faces.redirect("./templates/modelo.xhtml");//For antiga executada quando deve navegar dentro de pasta.
			
			if(usuarioLogado != null) {				
				Faces.redirect("./adm.xhtml");
			}
			//Faces.redirect("./adm.xhtml");					

		} catch (IOException erro) {
			
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
			
		}
	}

	//############## LOGOUT DO SISTEMA ####################
	public String sair() {
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/seguranca/autenticacao.xhtml?faces-redirect=true";
		
	}

	public boolean temPermissoes(List<String> permissoes) {

		for (String permissao : permissoes) {
			if (usuarioLogado.getTipo() == permissao.charAt(0)) {
				return true;
			}
		}
		return false;
	}
}
