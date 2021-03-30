package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.pro.sispro.migreja.domain.Cidade;
import br.pro.sispro.migreja.util.HibernateUtil;

public class CidadeDAO extends GenericDAO<Cidade> {
	
	@SuppressWarnings("unchecked")
	public List<Cidade> buscarPorEstado(Long estadoCodigo) {
		
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		
		try {
			Criteria consulta = sessao.createCriteria(Cidade.class);
			consulta.add(Restrictions.eq("estado.codigo", estadoCodigo));			
			consulta.addOrder(Order.asc("nome"));			
			List<Cidade> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			System.out.println("Erro a listar a cidade buscar por estado (CidadeDAO): " + erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	private Connection conexao;
	
	public Cidade buscaCidadePorCodigo(Long codigo){
		
		Cidade cidade = new Cidade();
		
		try {
			conexao = HibernateUtil.getConexao();
			String sql ="SELECT * FROM cidade WHERE codigo=?";
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setLong(1, codigo);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				cidade.setCodigo(rs.getLong("codigo"));
				cidade.setNome(rs.getString("nome"));				
			}
			
		} catch (SQLException erro) {
			System.out.println("Erro a listar a cidade buscar cidade por codigo (CidadeDAO): " + erro);
			Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, erro);
		}
		return cidade;
	}
}
