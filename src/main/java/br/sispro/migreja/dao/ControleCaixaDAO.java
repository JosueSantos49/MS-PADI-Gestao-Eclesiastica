package br.sispro.migreja.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.pro.sispro.migreja.domain.ControleCaixa;
import br.pro.sispro.migreja.util.HibernateUtil;

public class ControleCaixaDAO extends GenericDAO<ControleCaixa> {

	public void salvar(ControleCaixa controleCaixa) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.save(controleCaixa);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
		
	}

}
