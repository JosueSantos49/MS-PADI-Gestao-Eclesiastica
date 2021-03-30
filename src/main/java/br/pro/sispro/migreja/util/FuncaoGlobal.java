package br.pro.sispro.migreja.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

import javax.faces.context.FacesContext;

import br.pro.sispro.migreja.domain.Usuario;

//Possui várias funções que podem ser usada na aplicação. Como várias transações do sistema vai usar é definida como global
// se precisar de manutenção modifica em um só lugar
public class FuncaoGlobal {

	//####################### RETORNA O IP DA REDE ###################################
	public String pegaIpRede() throws SocketException {
		
		Enumeration<NetworkInterface> net = null;
		
		//Pegar IP da Rede
		net = NetworkInterface.getNetworkInterfaces();
		NetworkInterface element = net.nextElement();
		Enumeration<InetAddress> adresses = element.getInetAddresses();
		InetAddress ip = adresses.nextElement();
		String ipRede = ip.getHostAddress();
		
		return ipRede;
		
	}
	
	//########################## RETORNA A DATA ATUAL #################################	
	public Date pegadataAtual() {		
		
		Date data = new Date();	
				
		java.sql.Date dataAgora = new java.sql.Date(data.getTime());
		return dataAgora;
		
	}
	
	//############################ RETORNA O USUÁRIO LOGADO DA SESSÃO ################################# 
	public String pegarUsuarioSessao() {
		
		//Pegar usuário da sessão					
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Usuario usuarioLogado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuarioLogado");
		String usuarioLogadoSistema = usuarioLogado.getPessoa().getNome();
		
		return usuarioLogadoSistema;
		
	}
		
}
