package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.pro.sispro.migreja.domain.ItemVenda;
import br.pro.sispro.migreja.domain.Produto;
import br.pro.sispro.migreja.domain.Venda;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.HibernateUtil;

public class VendaDAO extends GenericDAO<Venda> {
	
	public boolean salvar(Venda venda, List<ItemVenda> itensVenda) {
		
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			
			transacao = sessao.beginTransaction();

			sessao.save(venda);

			// Capturando. Salvando todos os itens da venda.
			for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
				
				// Caputurar um item da linha corrente
				ItemVenda itemVenda = itensVenda.get(posicao);
				
				// Setando a venda
				itemVenda.setVenda(venda);

				// salvando com a sessao. Para uma venda salvar n itens.
				sessao.save(itemVenda);

				//###################### baixa no estoque ###########################
				// Capturar o produto, para isolar
				Produto produto = itemVenda.getProduto();

				int quantidade = produto.getQuantidade() - itemVenda.getQuantidade();
				
				if (quantidade >= 0) {
					
					// Pega o que esta no estoque e subtrai pela quantidade vendida. Converte para String e passa para Short
					produto.setQuantidade(new Short(quantidade + ""));
					
					// atualiza o produto
					sessao.update(produto);
					
				} else {
					//Força o DAO a jogar um erro para Bean
					throw new RuntimeException("Quantidade insuficiente em estoque!");
				}

			}

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

	//##################### REGISTRA A VENDA NO CONTROLE DO CAIXA ###################################
	public void registrarVenda(Venda venda) throws Exception {
		
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			ps = con.prepareStatement("INSERT INTO controlecaixa (codDoc, descricao, data, valorTotal) VALUES (?,?,?,?)");
			
			ps.setDouble(1, venda.getCodigo());
			ps.setDouble(2, venda.getCodigo());
			ps.setDate(3, new java.sql.Date(venda.getHorario().getTime()));
			ps.setBigDecimal(4, venda.getPrecoTotal());

			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erro ao registrar conta no controle do caixa: "+e);
		}finally {
			con.close();
		}
		
	}
}
