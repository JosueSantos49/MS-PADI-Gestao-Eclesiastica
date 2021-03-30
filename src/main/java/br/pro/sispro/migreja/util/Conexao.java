
package br.pro.sispro.migreja.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Conexao {
	
	//############### INVOCA A CONEXÃO AO DATASOURCE ###############
	public static Connection getConnection() throws Exception {
		
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/pool");
		
		try {
			
			return ds.getConnection();
			
		} catch (SQLException e) {
			
			System.out.println("Erro ao invocar a conexão ao DataSouce (Conexao): " + e);
			throw new Exception(e.getMessage());
			
		}
	}
}
