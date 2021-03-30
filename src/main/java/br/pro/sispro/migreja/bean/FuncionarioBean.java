package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import br.pro.sispro.migreja.domain.Funcionario;
import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.FuncionarioDAO;
import br.sispro.migreja.dao.PessoaDAO;
import br.sispro.migreja.dao.TemporariaDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class FuncionarioBean implements Serializable {

	private Funcionario funcionario;
	private List<Funcionario> funcionarios;
	private List<Pessoa> pessoas;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	@PostConstruct
	public void listar() {
		try {
			
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o funcionário!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//####################### ABRIR FORM CADASTRAR ##########################
	public void novo() {
		try {
			funcionario = new Funcionario();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um nova pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//########################## PROCESSA OS DADOS DO FORM CADASTRAR #########################
	public void salvar() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			//############# RECUPERANDO VALORES DO FORM ##################
			Long pessoa_selecionada = getFuncionario().getPessoa().getCodigo();
			System.out.println("Pessoa selecionada: "+pessoa_selecionada);
			
			//Verifico se a pessoa, já é funcionário
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			ps = con.prepareStatement("SELECT pessoa FROM funcionario WHERE pessoa = '" +pessoa_selecionada+ "'");
			
			ResultSet verifica_pessoa_selecionada = ps.executeQuery();
			
			if(verifica_pessoa_selecionada.next()) {
				
				Messages.addGlobalError("Já existe um funcionário cadastrado com a pessoa informada!");					
				
			}else {			
			
				//################### PERSISTE NO BANCO #####################
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				funcionarioDAO.salvar(funcionario);
				Long codigoRegistro = funcionario.getCodigo(); //Recupera o ID do Registro do banco de dados				
	
				funcionario = new Funcionario();
				funcionarios = funcionarioDAO.listar();
	
				PessoaDAO pessoaDAO = new PessoaDAO();
				pessoas = pessoaDAO.listar();	
				
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
						ps.setString(4, "funcionario");
						ps.setLong(5, codigoRegistro);//chave estrangeira
						ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
						
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
	
				Messages.addGlobalInfo("Funcionário salvo com sucesso!");

			}
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um funcionário!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//########################### REMOVER O REGISTRO DO BANCO DE DADOS#################################
	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			funcionario = (Funcionario) evento.getComponent().getAttributes().get("funcionarioSelecionado");

			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarioDAO.excluir(funcionario);
			Long codigoRemovido= funcionario.getCodigo();//Recupera o ID do Registro removido

			funcionarios = funcionarioDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "funcionario");
					ps.setLong(5, codigoRemovido);//chave estrangeira (o código é removido, mas salvo na tabela temporária para LOG)
					ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
					
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

			Messages.addGlobalInfo("Funcionário excluído: " + funcionario.getPessoa().getNome());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o funcionário!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
			
		try {
				
			funcionario = (Funcionario) evento.getComponent().getAttributes().get("funcionarioSelecionado");

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();	
							
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar abrir o form de editar funcionário!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
				
	}

	//######################## PROCESSAR A EDIÇÃO NO BANCO DE DADOS ############################# 
	//Submeter os dados a serem atualizados
	public void editar(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarioDAO.editar(funcionario);
			Long codigoRegistro = funcionario.getCodigo();//Recupera o ID do Registro do banco de dados
						
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			//Se houve a edição no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a edição. 
			//Cadastrar na tabela temporária os dados. 
			if(codigoRegistro != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "editar");
					ps.setString(4, "funcionario");
					ps.setLong(5, codigoRegistro);//chave estrangeira
					ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
					
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
			
			Messages.addGlobalInfo("O funcionário foi editado com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			funcionario = (Funcionario) evento.getComponent().getAttributes().get("funcionarioSelecionado");	
									
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o funcionário!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void imprimir() {

		try {
			// Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();

			String nomePessoa = (String) filtros.get("nome");
			String cpfPessoa = (String) filtros.get("cpf");

			String caminho = Faces.getRealPath("/reports/funcionario.jasper");

			Map<String, Object> parametros = new HashMap<>();

			if (nomePessoa == null) {
				parametros.put("nome", "%%");
			} else {
				parametros.put("nome", "%" + nomePessoa + "%");
			}
			if (cpfPessoa == null) {
				parametros.put("cpf", "%%");
			} else {
				parametros.put("cpf", "%" + cpfPessoa + "%");
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
