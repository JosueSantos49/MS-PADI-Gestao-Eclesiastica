/* 
	Autor: Josué da Conceição Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2016
 */
package br.pro.sispro.migreja.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.service.ServiceRegistry;

/*Conversar com o BD provendo conexões.*/
public class HibernateUtil {
	private static SessionFactory fabricaDeSessoes = criarFabricaDeSessoes();// variável
																				// global

	public static SessionFactory getFabricaDeSessoes() {
		return fabricaDeSessoes;
	}

	public static Connection getConexao() {
		Session sessao = fabricaDeSessoes.openSession();

		Connection conexao = sessao.doReturningWork(new ReturningWork<Connection>() {
			@Override
			public Connection execute(Connection conn) throws SQLException {
				return conn;
			}
		});
		return conexao;
	}

	private static SessionFactory criarFabricaDeSessoes() {
		try {
			// verifica o arquivo hibernate.cfg.xml (registro de serviços),
			// verifica as informações.
			Configuration configuracao = new Configuration().configure();

			// Pega o registro de serviços
			ServiceRegistry registro = new StandardServiceRegistryBuilder().applySettings(configuracao.getProperties())
					.build();

			// Passar e construir a fabrica de sessões
			SessionFactory fabrica = configuracao.buildSessionFactory(registro);

			return fabrica;// variável local

		} catch (Throwable ex) {
			System.out.println("A fábrica de sessões não pode ser criada: " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
