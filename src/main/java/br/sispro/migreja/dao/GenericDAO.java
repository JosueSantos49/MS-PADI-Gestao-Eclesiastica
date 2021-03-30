package br.sispro.migreja.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.pro.sispro.migreja.util.HibernateUtil;

/*Classe generica para aproveitar metodos e não ter redundância*/
public class GenericDAO<Entidade> {

	private Class<Entidade> classe;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.classe = (Class<Entidade>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void salvar(Entidade entidade) {
		
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			
			transacao = sessao.beginTransaction();
			sessao.save(entidade);			
			transacao.commit();
						
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			System.out.println("Erro ao salvar a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Entidade> listar() {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(classe);
			List<Entidade> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			System.out.println("Erro ao listar a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Entidade> listar(String campoOrdenacao) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			
			Criteria consulta = sessao.createCriteria(classe);
			consulta.addOrder(Order.asc(campoOrdenacao));
			List<Entidade> resultado = consulta.list();
			
			return resultado;
			
		} catch (RuntimeException erro) {
			System.out.println("Erro ao listar campo ordenação a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Entidade buscar(Long codigo) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(classe);
			consulta.add(Restrictions.idEq(codigo));// Comparação da chave
													// primaria
			Entidade resultado = (Entidade) consulta.uniqueResult();// Cast
			return resultado;
		} catch (RuntimeException erro) {
			System.out.println("Erro ao buscar a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}
		
	public void excluir(Entidade entidade) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.delete(entidade);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			System.out.println("Erro ao excluir a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

	public void editar(Entidade entidade) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			
			transacao = sessao.beginTransaction();
			sessao.update(entidade);
			transacao.commit();
			
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			System.out.println("Erro ao editar a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Entidade merge(Entidade entidade) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			Entidade retorno = (Entidade) sessao.merge(entidade);
			transacao.commit();
			return retorno;
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			System.out.println("Erro ao merge a entidade (GenericDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}

}
