package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.pro.sispro.migreja.domain.Cidade;
import br.pro.sispro.migreja.domain.Instituicao;
import br.pro.sispro.migreja.util.HibernateUtil;

public class InstituicaoDAO extends GenericDAO<Instituicao>{
	
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
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	private Connection conexao;
	public List<Instituicao> listarTodos(){
		List<Instituicao> listaInstituicao = new ArrayList<>();
		String sql = "SELECT * FROM instituicao";
		
		try {
			conexao = HibernateUtil.getConexao();
			PreparedStatement ps = conexao.prepareStatement(sql);			
			ResultSet rs = ps.executeQuery();			
			CidadeDAO cidadeDAO = new CidadeDAO();
			
			while(rs.next()){
				Instituicao instituicao = new Instituicao();
				instituicao.setCodigo(rs.getLong("codigo"));
				instituicao.setRazaosocial(rs.getString("razaosocial"));
				instituicao.setCnpj(rs.getString("cnpj"));
				instituicao.setDataCadastro(rs.getDate("dataCadastro"));
				instituicao.setEndereco(rs.getString("endereco"));
				instituicao.setTelefone(rs.getString("telefone"));
				instituicao.setFundador(rs.getString("fundador"));
				instituicao.setCep(rs.getString("cep"));
				instituicao.setEmail(rs.getString("email"));
				instituicao.setSite(rs.getString("site"));
				instituicao.setCidade(cidadeDAO.buscaCidadePorCodigo(rs.getLong("codigo")));
				listaInstituicao.add(instituicao);				
			}
			
		} catch (SQLException erro) {
			Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, erro);
		}
		return listaInstituicao;
	}
}
