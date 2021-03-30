package br.pro.sispro.migreja.util;

import org.hibernate.Session;
import org.junit.Test;

public class HibernateUtilTest {
	@Test
	public void conectar(){
		//Pegar a sessão conectar e desconectar do BD.
		// Capturar uma fabrica de sessões e pegar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();		
		sessao.close();
		HibernateUtil.getFabricaDeSessoes().close();
	}
}
