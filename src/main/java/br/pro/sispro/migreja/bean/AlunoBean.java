package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
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
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Aluno;
import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.domain.Turma;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.sispro.migreja.dao.AlunoDAO;
import br.sispro.migreja.dao.PessoaDAO;
import br.sispro.migreja.dao.TemporariaDAO;
import br.sispro.migreja.dao.TurmaDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings({ "serial", "deprecation" })
@ManagedBean
@ViewScoped
public class AlunoBean implements Serializable {

	private Aluno aluno;
	private List<Aluno> alunos;
	private List<Pessoa> pessoas;
	private List<Turma> turmas;	
	private Temporaria temporaria;
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();
	
	HtmlInputText alunoInput = new HtmlInputText();
	
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	
	public List<Temporaria> getTemporarias() {
		return temporarias;
	}

	public void setTemporarias(List<Temporaria> temporarias) {
		this.temporarias = temporarias;
	}
	
	public Temporaria getTemporaria() {
		return temporaria;
	}

	public void setTemporaria(Temporaria temporaria) {
		this.temporaria = temporaria;
	}

	@PostConstruct
	public void listar() {
		try {
			
			AlunoDAO alunoDAO = new AlunoDAO();
			alunos = alunoDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o aluno!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//##################### ABRIR O FORM NOVO REGISTRO ################################
	public void novo() {
		try {
			aluno = new Aluno();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			TurmaDAO turmaDAO = new TurmaDAO();
			turmas = turmaDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um nova pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//######################## PROCESSA O FORM CADASTRAR ALUNO ###########################
	public void salvar() throws Exception{
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;		
				
		try {
			
			//############# RECUPERANDO VALORES DO FORM ##################
			Long pessoa_selecionada = getAluno().getPessoa().getCodigo();
			
			//Verifico se a pessoa selecionada já, está cadastrada na turma
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			ps = con.prepareStatement("SELECT pessoa FROM aluno WHERE pessoa = '" +pessoa_selecionada+ "'");
			
			ResultSet verifica_pessoa_selecionada = ps.executeQuery();
			
			if(verifica_pessoa_selecionada.next()) {
				
				Messages.addGlobalError("Já existe um aluno cadastrado na turma com a pessoa informada!");					
				
			}else {
			
				//################## SALVA NO BANCO ####################
				AlunoDAO alunoDAO = new AlunoDAO();
				alunoDAO.salvar(aluno);
				Long codigoRegistro = aluno.getCodigo();//Recupera o ID do Registro do banco de dados
	
				//################### ATUALIZA A LISTA DE ALUNOS ####################
				aluno = new Aluno();
				alunos = alunoDAO.listar();
	
				//################### ATUALIZA A LISTA DE PESSOAS ####################
				PessoaDAO pessoaDAO = new PessoaDAO();
				pessoas = pessoaDAO.listar();
				
				//################### ATUALIZA A LISTA DE TURMAS ####################
				TurmaDAO turmaDAO = new TurmaDAO();
				turmas = turmaDAO.listar();	
				
				//Se ouve a inserção no banco de dados vai gerar a PK (ID do Registro). 
				//Se PK for diferente de Nulo ouve a inserção. 
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
						ps.setString(4, "aluno");
						ps.setLong(5, codigoRegistro);//chave estrangeira
						ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
						
						ps.executeUpdate();
						
						temporaria = new Temporaria();
						
						//################### ATUALIZA A LISTA DE LOG ####################
						TemporariaDAO temporariaDAO = new TemporariaDAO();
						temporarias = temporariaDAO.listar();	
						
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
	
				Messages.addGlobalInfo("Aluno salvo com sucesso!");
			
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um aluno!");
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
			
			aluno = (Aluno) evento.getComponent().getAttributes().get("alunoSelecionado");

			AlunoDAO alunoDAO = new AlunoDAO();
			alunoDAO.excluir(aluno);
			Long codigoRemovido= aluno.getCodigo();//Recupera o ID do Registro removido			
			
			alunos = alunoDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "aluno");
					ps.setLong(5, codigoRemovido);//chave estrangeira (o código é removido, mas salvo na tabela temporária para LOG)
					ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
					
					ps.executeUpdate();
										
					//################### ATUALIZA A LISTA DE LOG ####################
					TemporariaDAO temporariaDAO = new TemporariaDAO();
					temporarias = temporariaDAO.listar();	
					
					ps.close();
					
				} catch (RuntimeException erro) {
					Messages.addGlobalError("Erro! Não conseguiu inserir na tabela temporária!");
					erro.printStackTrace();
					Messages.addGlobalError(erro.getMessage());
				}finally {
					con.close();
				}
				//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
			}
			
			Messages.addGlobalInfo("Registro removido: " + aluno.getPessoa().getNome());
		
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um produto!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
		
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
		
		try {	
			
			aluno = (Aluno) evento.getComponent().getAttributes().get("alunoSelecionado");
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			TurmaDAO turmaDAO = new TurmaDAO();
			turmas = turmaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o aluno!");
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
				
				AlunoDAO alunoDAO = new AlunoDAO();
				alunoDAO.editar(aluno);
				Long codigoRegistro = aluno.getCodigo();//Recupera o ID do Registro do banco de dados

				PessoaDAO pessoaDAO = new PessoaDAO();
				pessoas = pessoaDAO.listar();
				
				TurmaDAO turmaDAO = new TurmaDAO();
				turmas = turmaDAO.listar();	
				
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
						ps.setString(4, "aluno");
						ps.setLong(5, codigoRegistro);//chave estrangeira
						ps.setDate(6, (Date) funcaoGlobal.pegadataAtual());
						
						ps.executeUpdate();
						
						//################### ATUALIZA A LISTA DE LOG ####################
						TemporariaDAO temporariaDAO = new TemporariaDAO();
						temporarias = temporariaDAO.listar();	
						
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
				
				Messages.addGlobalInfo("O Aluno foi editado com sucesso!");
			
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
			System.out.println("Ocorreu um erro ao tentar selecionar uma pessoa (Editar Aluno): "+erro);
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
		
	
	public void visualizar(ActionEvent evento) {
		try {
			aluno = (Aluno) evento.getComponent().getAttributes().get("alunoSelecionado");	
												
			AlunoDAO alunoDAO = new AlunoDAO();
			alunos = alunoDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o aluno!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		AlunoDAO alunoDAO = new AlunoDAO();
		alunos = alunoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/aluno.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.alunos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Aluno.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		AlunoDAO alunoDAO = new AlunoDAO();
		alunos = alunoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/aluno.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.alunos));
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		ServletOutputStream outStream = response.getOutputStream();
		outStream.write(bytes, 0 , bytes.length);
		outStream.flush();
		outStream.close();
			
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportarDOC(ActionEvent actionEvent) throws JRException, IOException{		
		AlunoDAO alunoDAO = new AlunoDAO();
		alunos = alunoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/aluno.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.alunos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Aluno.doc");
		ServletOutputStream outStream = response.getOutputStream();
		
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
		
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void exportarPPT(ActionEvent actionEvent) throws JRException, IOException{
		AlunoDAO alunoDAO = new AlunoDAO();
		alunos = alunoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/aluno.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.alunos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Aluno.ppt");
		ServletOutputStream outStream = response.getOutputStream();
		
		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
		
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();			
	}
}
