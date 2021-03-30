package br.sispro.migreja.dao;

import java.security.NoSuchAlgorithmException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.pro.sispro.migreja.domain.Usuario;
import br.pro.sispro.migreja.util.ConvertPasswordToMD5;
import br.pro.sispro.migreja.util.HibernateUtil;

public class UsuarioDAO extends GenericDAO<Usuario> {

	// Metodo de autenticacao
	public Usuario autenticar(String cpf, String senha) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.createAlias("pessoa", "p");

			consulta.add(Restrictions.eq("p.cpf", cpf));

			SimpleHash hash = new SimpleHash("md5", senha);
			consulta.add(Restrictions.eq("senha", hash.toHex()));

			Usuario resultado = (Usuario) consulta.uniqueResult();// Cast

			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public Usuario merge(Usuario usuario) {
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();

			String senha = usuario.getSenha();
			String md5 = null;
			try {
				md5 = ConvertPasswordToMD5.convertPasswordToMD5(senha);
				System.out.println("Senha convetida Md5: " + md5);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			usuario.setSenha(md5);
			Usuario retorno = (Usuario) sessao.merge(usuario);

			transacao.commit();
			return retorno;
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
