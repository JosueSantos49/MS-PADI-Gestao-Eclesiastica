package br.pro.sispro.migreja.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import br.pro.sispro.migreja.bean.AutenticacaoBean;
import br.pro.sispro.migreja.domain.Usuario;

//Interceptar acoes no jsf
@SuppressWarnings("serial")
public class AutenticacaoListener implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		
		//Verificar qual pagina que esta sendo acessada no momento (publica ou protegida).Pagina atual.
		String paginaAtual = Faces.getViewId();
		
		boolean ehPaginaDeAutenticacao = paginaAtual.contains("autenticacao.xhtml");
				
		if(!ehPaginaDeAutenticacao){
			//verificar se usuário esta logado
			AutenticacaoBean autenticacaoBean = Faces.getSessionAttribute("autenticacaoBean");
						
			if(autenticacaoBean == null){
				Faces.navigate("/seguranca/autenticacao.xhtml");
				return;
			}
			
			Usuario usuario = autenticacaoBean.getUsuarioLogado();
			if(usuario == null){
				Faces.navigate("/seguranca/autenticacao.xhtml");
				return;
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		//System.out.println("ANTES DA FASE: " + event.getPhaseId());

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
