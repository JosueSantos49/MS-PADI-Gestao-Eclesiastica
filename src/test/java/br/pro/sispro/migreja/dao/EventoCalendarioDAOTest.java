package br.pro.sispro.migreja.dao;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.EventoCalendario;
import br.sispro.migreja.dao.EventoCalendarioDAO;

public class EventoCalendarioDAOTest {

	@Test
	@Ignore
	public void listar() throws Exception{
		EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
		List<EventoCalendario> resultado = eventoCalendarioDAO.listar();
		
		System.out.println("Total de registros encontrados: " + resultado.size());
		
		for(EventoCalendario e: resultado){
			System.out.println(e.getCodigo());
			System.out.println(e.getTitulo());
			System.out.println(e.getDescricao());
			System.out.println(e.getInicio());
			System.out.println(e.getFim());
		}
	}
}
