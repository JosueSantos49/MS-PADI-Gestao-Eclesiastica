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

import br.pro.sispro.migreja.domain.Estado;
import br.pro.sispro.migreja.util.HibernateUtil;

public class EstadoDAO extends GenericDAO<Estado>{
	
	@SuppressWarnings("unchecked")
	public List<Estado> buscarEstado(Long estadoCodigo) {
		
		// Capturar uma sessão
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		
		try {
			
			Criteria consulta = sessao.createCriteria(Estado.class);
			consulta.add(Restrictions.eq("estado.codigo", estadoCodigo));			
			consulta.addOrder(Order.asc("nome"));
			
			List<Estado> resultado = consulta.list();
			return resultado;
			
		} catch (RuntimeException erro) {
			
			System.out.println("Erro a listar o estado buscar estado (EstadoDAO): " + erro);
			throw erro;
			
		} finally {
			sessao.close();
		}
		
	}

	private Connection conexao;
	
	public Estado buscaEstadoPorCodigo(Long codigo){
		
		Estado estado = new Estado();
		
		try {
			
			conexao = HibernateUtil.getConexao();
			String sql ="SELECT * FROM estado WHERE codigo=?";
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setLong(1, codigo);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				estado.setCodigo(rs.getLong("codigo"));
				estado.setNome(rs.getString("nome"));				
			}
			
		} catch (SQLException erro) {
			System.out.println("Erro a listar o estado buscar estado por codigo (CidadeDAO): " + erro);
			Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, erro);
		}
		return estado;
	}
}
