package br.pro.sispro.migreja.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Caixa;
import br.sispro.migreja.dao.CaixaDAO;

public class CaixaDAOTest {
	@Test
	@Ignore
	public void salvar() throws ParseException{
		Caixa caixa = new Caixa();
		caixa.setDataAbertura(new SimpleDateFormat("dd/MM/yyyy").parse("18/08/2016"));
		caixa.setValorAbertura(new BigDecimal("200.00"));
		
		CaixaDAO caixaDAO = new CaixaDAO();
		caixaDAO.salvar(caixa);
	}
	
	@Test
	@Ignore
	public void buscar() throws ParseException{
		CaixaDAO caixaDAO = new CaixaDAO();
		Caixa caixa = caixaDAO.buscar(new SimpleDateFormat("dd/MM/yyyy").parse("17/08/2016"));
		System.out.println(caixa);
		Assert.assertNull(caixa);
	}
}
