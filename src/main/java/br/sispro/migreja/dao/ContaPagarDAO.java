package br.sispro.migreja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.omnifaces.util.Messages;

import com.mysql.jdbc.Statement;

import br.pro.sispro.migreja.domain.ContaPagar;
import br.pro.sispro.migreja.util.Conexao;

public class ContaPagarDAO extends GenericDAO<ContaPagar> {

	//################ PROCESSA O REGISTRO DA CONTA NO CONTROLE DO CAIXA ##########################
	@SuppressWarnings("unused")
	public void salvarRegistro(ContaPagar contaPagar) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;

		try {
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			//############################## VALIDAÇÃO PARA NÃO DUPLICAR REGISTRO DE CONTA A PAGAR ##################################  
			//Verifico se o codigo do documento da linha corrente na TB CONTA A PAGAR existe na TB CONTROLE DO CAIXA 	
			ps = con.prepareStatement("SELECT codDoc FROM controlecaixa WHERE codDoc = " + contaPagar.getCodDoc());			
			ResultSet codDocControleCaixa = ps.executeQuery();
						
			if(codDocControleCaixa.next()) {
				
				Messages.addGlobalError("Já existe um registro de conta com o código do documento informado!");
				
			}else {
								
				//###################### VERIFICA OS DADOS, SE TIVER MUDANÇA ATUALIZA ####################
				ps = con.prepareStatement("UPDATE contapagar SET codDoc = ?, descricao = ?, data = ?, valorPago = ?, dataPgto = ?, fornecedor = ?, cnpj = ?, parcela = ?, dataVencimento = ?, valorParcela = ?, pagamento = ?, instituicao = ?" + " WHERE codigo = ?", Statement.RETURN_GENERATED_KEYS);
				setPreparedStatement(contaPagar, ps);
				ps.executeUpdate();
				
				// Adicionei Statement.RETURN_GENERATED_KEYS ao prepareStatement assim ele retorna a id (se ela for auto_increment)
				/*ps = con.prepareStatement("INSERT INTO contapagar (codDoc, descricao, data, valorPago, dataPgto, fornecedor, cnpj, parcela, dataVencimento, valorParcela, pagamento, instituicao, controleCaixa)" + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	
				ps.setLong(1, contaPagar.getCodDoc());
				ps.setString(2, contaPagar.getDescricao());
				ps.setDate(3, new java.sql.Date(contaPagar.getData().getTime()));
				ps.setDouble(4, contaPagar.getValorPago());
				ps.setDate(5, new java.sql.Date(contaPagar.getDataPgto().getTime()));
				ps.setString(6, contaPagar.getFornecedor());
				ps.setString(7, contaPagar.getCnpj());
				ps.setLong(8, contaPagar.getParcela());
				ps.setDate(9, new java.sql.Date(contaPagar.getDataVencimento().getTime()));
				ps.setDouble(10, contaPagar.getValorParcela());
				ps.setString(11, contaPagar.getPagamento());
				ps.setObject(12, contaPagar.getInstituicao().getCodigo());
				ps.setObject(13, contaPagar.getControleCaixa().getCodigo());*/
				
				//ps.execute();
	
				// Recupera a id
				ResultSet rs = ps.getGeneratedKeys();
				int id = 0;
				if (rs.next()) {
					id = rs.getInt(1);				
				}
			
				ps = con.prepareStatement("INSERT INTO controlecaixa (codDoc, descricao, data, valorPago) VALUES (?,?,?,?)");
				
				ps.setLong(1, contaPagar.getCodDoc());
				ps.setString(2, contaPagar.getDescricao());
				ps.setDate(3, new java.sql.Date(contaPagar.getData().getTime()));
				ps.setDouble(4, contaPagar.getValorPago());
				//ps.setInt(10, id); //Recupera o id FK
	
				ps.executeUpdate();
				ps.close();	
				
			}			

		} catch (SQLException erro) {			
			System.out.println("Erro ao registrar conta: " + erro);
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma conta!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}finally {
			con.close();
		}
		
		
		
	}
	
	private void setPreparedStatement(ContaPagar contaPagar, PreparedStatement ps) throws SQLException{		
		ps.setLong(1, contaPagar.getCodDoc());
		ps.setString(2, contaPagar.getDescricao());
		ps.setDate(3, new java.sql.Date(contaPagar.getData().getTime()));
		ps.setDouble(4, contaPagar.getValorPago());
		ps.setDate(5, new java.sql.Date(contaPagar.getDataPgto().getTime()));
		ps.setString(6, contaPagar.getFornecedor());
		ps.setString(7, contaPagar.getCnpj());
		ps.setLong(8, contaPagar.getParcela());
		ps.setDate(9, new java.sql.Date(contaPagar.getDataVencimento().getTime()));
		ps.setDouble(10, contaPagar.getValorParcela());
		ps.setString(11, contaPagar.getPagamento());
		ps.setObject(12, contaPagar.getInstituicao().getCodigo());
		ps.setLong(13, contaPagar.getCodigo());			
	}
	
}
