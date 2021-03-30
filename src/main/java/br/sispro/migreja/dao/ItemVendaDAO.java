package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.pro.sispro.migreja.domain.ItemVenda;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.HibernateUtil;

public class ItemVendaDAO extends GenericDAO<ItemVenda> {

	public boolean excluirItem(ItemVenda itemVenda) {
		
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.delete(itemVenda);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
		return true;		
	}

	//####################### REMOVE O ITEM DA VENDA DO CONTROLE DE CAIXA (EXTORNO) ##########################
	public void excluirItemControleCaixa(ItemVenda itemVenda) throws Exception {
		
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			ps = con.prepareStatement("DELETE FROM controlecaixa WHERE codDoc = ?;");
			
			ps.setLong(1, itemVenda.getCodigo());//setando
			ps.executeUpdate();
			ps.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erro ao excluir, o registro da vendo do controle do caixa: "+e);
		}finally {
			con.close();
		}	
		
	}

}
