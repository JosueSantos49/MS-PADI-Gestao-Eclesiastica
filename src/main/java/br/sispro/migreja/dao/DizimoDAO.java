package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.pro.sispro.migreja.domain.Dizimo;
import br.pro.sispro.migreja.util.Conexao;

public class DizimoDAO extends GenericDAO<Dizimo> {

	public void salvarRegistro(Dizimo dizimo)  throws Exception{
		
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;

		try {
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			/*
			ps = con.prepareStatement("UPDATE dizimo SET  data = ?, formaPagamento = ?, oferta = ?,	valor = ?, membro = ?, membro0v = ?, membro1 = ?, membro10 = ?, membro10v = ?, membro11 = ?, membro11v = ?,	membro12 = ?, membro12v = ?, membro13 = ?, membro13v = ?, membro14 = ?, membro14v = ?, membro15 = ?, membro15v = ?, membro16 = ?, membro16v = ?, membro17 = ?, membro17v = ?,	membro18 = ?, membro18v = ?, membro19 = ?, membro19v = ?,	membro1v = ?, membro2 = ?, membro2v = ?, membro3 = ?, membro3v = ?,	membro4 = ?, membro4v = ?, membro5 = ?,		membro5v = ?,	membro6 = ?,		membro6v = ?, membro7 = ?, membro7v = ?, membro8 = ?, membro8v = ?, membro9 = ?, membro9v = ?, total = ?, outros = ?, ordem = ?, instituicao = ?, membroRespo = ? " + " WHERE codigo = ?", Statement.RETURN_GENERATED_KEYS);
			setPreparedStatement(dizimo, ps);
			ps.executeUpdate();
			
			// Recupera a id
			ResultSet rs = ps.getGeneratedKeys();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt(1);
			}
			*/

			ps = con.prepareStatement("INSERT INTO controlecaixa (codDoc, descricao, data, valorTotal) VALUES (?,?,?,?)");
			
			ps.setDouble(1, dizimo.getOrdem());
			ps.setString(2, dizimo.getDescricao());
			ps.setDate(3, new java.sql.Date(dizimo.getData().getTime()));
			ps.setDouble(4, dizimo.getTotal());
			//ps.setInt(10, id); //Recupera o id FK

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erro ao registrar conta do dízimo: "+e);
		}finally {
			con.close();
		}
	}
	
	/*
	private void setPreparedStatement(Dizimo dizimo, PreparedStatement ps) throws SQLException{
		
		ps.setDate(1, new java.sql.Date(dizimo.getData().getTime()));
		ps.setString(2, dizimo.getFormaPagamento());
		ps.setDouble(3, dizimo.getOferta() == null ? 0 : 0);
		ps.setDouble(4, dizimo.getValor() == null ? 0 : 0);		
		ps.setObject(5, dizimo.getMembro().getCodigo());
		ps.setDouble(6, dizimo.getMembro0v() == null ? 0 : 0);		
		ps.setString(7, dizimo.getMembro1());		
		ps.setString(8, dizimo.getMembro10());
		ps.setDouble(9, dizimo.getMembro10v() == null ? 0 : 0);			
		ps.setString(10, dizimo.getMembro11());
		ps.setDouble(11, dizimo.getMembro11v() == null ? 0 : 0);		
		ps.setString(12, dizimo.getMembro12());
		ps.setDouble(13, dizimo.getMembro12v() == null ? 0 : 0);		
		ps.setString(14, dizimo.getMembro13());
		ps.setDouble(15, dizimo.getMembro13v() == null ? 0 : 0);		
		ps.setString(16, dizimo.getMembro14());
		ps.setDouble(17, dizimo.getMembro14v() == null ? 0 : 0);		
		ps.setString(18, dizimo.getMembro15());
		ps.setDouble(19, dizimo.getMembro15v() == null ? 0 : 0);		
		ps.setString(20, dizimo.getMembro16());
		ps.setDouble(21, dizimo.getMembro16v() == null ? 0 : 0);		
		ps.setString(22, dizimo.getMembro17());
		ps.setDouble(23, dizimo.getMembro17v() == null ? 0 : 0);		
		ps.setString(24, dizimo.getMembro18());
		ps.setDouble(25, dizimo.getMembro18v() == null ? 0 : 0);		
		ps.setString(26, dizimo.getMembro19());
		ps.setDouble(27, dizimo.getMembro19v() == null ? 0 : 0);
		ps.setDouble(28, dizimo.getMembro1v() == null ? 0 : 0);		
		ps.setString(29, dizimo.getMembro2());
		ps.setDouble(30, dizimo.getMembro2v() == null ? 0 : 0);		
		ps.setString(31, dizimo.getMembro3());
		ps.setDouble(32, dizimo.getMembro3v() == null ? 0 : 0);		
		ps.setString(33, dizimo.getMembro4());
		ps.setDouble(34, dizimo.getMembro4v() == null ? 0 : 0);		
		ps.setString(35, dizimo.getMembro5());
		ps.setDouble(36, dizimo.getMembro5v() == null ? 0 : 0);		
		ps.setString(37, dizimo.getMembro6());
		ps.setDouble(38, dizimo.getMembro6v() == null ? 0 : 0);		
		ps.setString(39, dizimo.getMembro7());
		ps.setDouble(40, dizimo.getMembro7v() == null ? 0 : 0);		
		ps.setString(41, dizimo.getMembro8());
		ps.setDouble(42, dizimo.getMembro8v() == null ? 0 : 0);		
		ps.setString(43, dizimo.getMembro9());
		ps.setDouble(44, dizimo.getMembro9v() == null ? 0 : 0);		
		ps.setDouble(45, dizimo.getTotal() == null ? 0 : 0);
		ps.setDouble(46, dizimo.getOutros() == null ? 0 : 0);		
		ps.setLong(47, dizimo.getOrdem());
		ps.setObject(48, dizimo.getInstituicao().getCodigo());		
		ps.setString(49, dizimo.getMembroRespo());
		ps.setLong(50, dizimo.getCodigo());		
	}
	*/
}
