package br.sispro.migreja.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.pro.sispro.migreja.domain.Cliente;
import br.pro.sispro.migreja.util.HibernateUtil;

public class ClienteDAO extends GenericDAO<Cliente>{

	@SuppressWarnings("unchecked")
	public List<Cliente> listarOrdenado() {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Cliente.class);
			consulta.createAlias("pessoa", "p");
			consulta.addOrder(Order.asc("p.nome"));
			List<Cliente> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}
