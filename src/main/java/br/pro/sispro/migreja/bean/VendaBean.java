package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.pro.sispro.migreja.domain.Cliente;
import br.pro.sispro.migreja.domain.Funcionario;
import br.pro.sispro.migreja.domain.ItemVenda;
import br.pro.sispro.migreja.domain.Produto;
import br.pro.sispro.migreja.domain.Venda;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.ClienteDAO;
import br.sispro.migreja.dao.FuncionarioDAO;
import br.sispro.migreja.dao.ProdutoDAO;
import br.sispro.migreja.dao.TemporariaDAO;
import br.sispro.migreja.dao.VendaDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class VendaBean implements Serializable {
	
	private Venda venda;
	private List<Produto> produtos;
	private List<ItemVenda> itensVenda;
	private List<Cliente> clientes;
	private List<Funcionario> funcionarios;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();
	
	private List<Venda> vendas;

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	
	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}
	


	@PostConstruct
	public void novo() {
		
		try {
			
			venda = new Venda();
			venda.setPrecoTotal(new BigDecimal("0.00"));
			
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar("descricao");// ordenacao
			
			itensVenda = new ArrayList<>();// Carrinho de compra vazio

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar carregar a tela de venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//######################## ADICIONAR ITEM NO CARRINHO DE COMPRAS #################################
	public void adicionar(ActionEvent evento) {
		
		// Verificar se um produto que estou tentando add, existe dentro de algum item
		Produto produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");

		// Guarda o valor. Se achou 1-, quer dizer que não achou item, se achou positivo quer dizer que achou item.
		int achou = -1; // Nao achou nada

		// Percorrer o Array List (vetor), que um produto e cada produto tem seus itens.
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			
			// Produto do item da linha corrente. Capturar o item da linha corrente.
			if (itensVenda.get(posicao).getProduto().equals(produto)) {
				
				achou = posicao;// deixa de ser -1 e passar a valer a posicao que deu true.
			}
		}

		// Testar a variavel achou.
		if (achou < 0) {
			
			// Se achou add um item novo. Converter o produto em item
			ItemVenda itemVenda = new ItemVenda();
			itemVenda.setPrecoParcial(produto.getPreco());
			itemVenda.setProduto(produto);
			itemVenda.setQuantidade(new Short("1"));

			// Jogar no arrayList. Adicao temporario dentro da lista
			itensVenda.add(itemVenda);
			
		} else {
			
			// Capturando o item na posicao achou
			ItemVenda itemVenda = itensVenda.get(achou);

			// Pega a quantidade antiga e soma + 1. "" converte para String e passa para Short
			itemVenda.setQuantidade(new Short(itemVenda.getQuantidade() + 1 + ""));

			// Preço do produto unitario * a quantidade.
			itemVenda.setPrecoParcial(produto.getPreco().multiply(new BigDecimal(itemVenda.getQuantidade())));
		}
		
		calcular(); // Pedir para atualizar o preço total
	}

	//###################### REMOVER O ITEM DO CARRINHO DE COMPARAS ###############################
	public void remover(ActionEvent evento) {
		
		ItemVenda itemVenda = (ItemVenda) evento.getComponent().getAttributes().get("itemSelecionado");

		// Guarda o valor. Se achou 1-, quer dizer que não achou item, se achou positivo quer dizer que achou item.
		int achou = -1; // Nao achou nada

		// Percorrer o Array List (vetor), que um produto e cada produto tem seus itens.
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			
			// Produto do item da linha corrente. Capturar o item da linha corrente.
			if (itensVenda.get(posicao).getProduto().equals(itemVenda.getProduto())) {
				
				achou = posicao;// deixa de ser -1 e passar a valer a posicao que deu true.
			}
		}

		if (achou > -1) {
			itensVenda.remove(achou);
		}
		
		calcular(); // Pedir para atualizar o preço total
	}

	//################# CALCULAR O VALOR DA COMPRA ############################
	public void calcular() {
		
		// Acumulador de Zerar o preço total
		venda.setPrecoTotal(new BigDecimal("0.00"));

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			
			ItemVenda itemVenda = itensVenda.get(posicao);

			// Somar o preço total ao preço parcial
			venda.setPrecoTotal(venda.getPrecoTotal().add(itemVenda.getPrecoParcial()));

		}
	}
	
	//############################ ABRE O MODAL INFORMANDO O FUNCIONÁRIO E CLIENTE PARA FINALIZAR A VENDA ###########################
	public void finalizar(){
		
		try {
			
			venda.setHorario(new Date());
			venda.setCliente(null);
			venda.setFuncionario(null);
			
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listarOrdenado();
			
			ClienteDAO clienteDAO = new ClienteDAO();
			clientes = clienteDAO.listarOrdenado();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar a venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//#################### PROCESSA A VENDA E PERSISTE NO BANCO DE DADOS ##############################
	public void salvar() throws Exception{
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			if(venda.getPrecoTotal().signum() == 0){
				Messages.addGlobalError("Informe pelo menos um item para venda!");
				return;
			}
			
			VendaDAO vendaDAO = new VendaDAO();
			boolean b = vendaDAO.salvar(venda, itensVenda);
			Long codigoRegistro = venda.getCodigo(); //Recupera o ID do Registro do banco de dados
			System.out.println("Resultado da venda: "+b);
			
			if(b != false){
				try {
					
					vendaDAO.registrarVenda(venda);
					
					//Se houve a inserção no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a inserção. 
					//Cadastrar na tabela temporária os dados. 
					if(codigoRegistro != null) {
						
						//################### INSERIR NA TABELA TEMPORÁRIA #######################						
						try {
							
							con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
							
							//Salvar os dados na tabela temporária		
							ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
																	
							ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
							ps.setString(2, funcaoGlobal.pegaIpRede());				
							ps.setString(3, "inserir");
							ps.setString(4, "venda");
							ps.setLong(5, codigoRegistro);//chave estrangeira
							ps.setDate(6, (java.sql.Date) funcaoGlobal.pegadataAtual());
							
							ps.executeUpdate();
												
							//################### ATUALIZA A LISTA DE LOG ####################
							TemporariaDAO temporariaDAO = new TemporariaDAO();
							temporariaDAO.listar();	
							
							ps.close();
							
						} catch (RuntimeException erro) {
							Messages.addGlobalError("Erro! Não conseguiu inserir na tabela temporária!");
							erro.printStackTrace();
							Messages.addGlobalError(erro.getMessage());
							System.out.println("Erro! Não conseguiu inserir na tabela temporária: "+erro);
						}finally {
							con.close();
						}
						//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
					}
					
					Messages.addGlobalInfo("Venda registrada com sucesso!");
					
				} catch (Exception e) {
					Messages.addGlobalError("Ocorreu um erro ao tentar registrar a venda!");
					e.printStackTrace();
					Messages.addGlobalError(e.getMessage());
				}
				
				Messages.addGlobalInfo("Venda realizada com sucesso!");
				
			}
			
			novo();//lipa a tabela após insarir um item
						
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar a venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//########################### LISTAR AS VENDAS #######################
	public void listar() {
		
		try {
			
			VendaDAO vendaDAO = new VendaDAO();
			vendas = vendaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			venda = (Venda) evento.getComponent().getAttributes().get("vendaSelecionada");

			VendaDAO vendaDAO = new VendaDAO();
			vendaDAO.excluir(venda);
			Long codigoRemovido= venda.getCodigo();//Recupera o ID do Registro removido

			vendas = vendaDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
										
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "venda");
					ps.setLong(5, codigoRemovido);//chave estrangeira (o código é removido, mas salvo na tabela temporária para LOG)
					ps.setDate(6, (java.sql.Date) funcaoGlobal.pegadataAtual());
					
					ps.executeUpdate();
										
					//################### ATUALIZA A LISTA DE LOG ####################
					TemporariaDAO temporariaDAO = new TemporariaDAO();
					temporariaDAO.listar();	
					
					ps.close();
					
				} catch (RuntimeException erro) {
					Messages.addGlobalError("Erro! Não conseguiu inserir na tabela temporária!");
					erro.printStackTrace();
					Messages.addGlobalError(erro.getMessage());
					System.out.println("Erro! Não conseguiu inserir na tabela temporária: "+erro);
				}finally {
					con.close();
				}
				//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
				
				Messages.addGlobalInfo("Venda excluída (Cliente): " + venda.getCliente().getPessoa().getNome());
				
			}
			

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover a venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void imprimir() {

		try {
			
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formulario:tabelaProduto");
			Map<String, Object> filtros = tabela.getFilters();
			
			String produto = (String) filtros.get("produto");
			String fabricante = (String) filtros.get("fabricante");
			
			String caminho = Faces.getRealPath("/reports/venda.jasper");
						
			Map<String, Object> parametros = new HashMap<>();
						
			if(produto == null){
				parametros.put("produto", "%%");
			}else{
				parametros.put("produto", "%" + produto + "%");
			}
			if(fabricante == null){
				parametros.put("fabricante", "%%");
			}else{
				parametros.put("fabricante", "%"+ fabricante + "%");
			}
						
			Connection conexao = HibernateUtil.getConexao();
			
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			JasperPrintManager.printReport(relatorio, true);
			
			
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar imprimir a pessoa!");			
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

}
