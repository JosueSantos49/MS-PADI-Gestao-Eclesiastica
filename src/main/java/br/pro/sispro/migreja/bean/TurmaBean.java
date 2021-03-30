package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;

import br.pro.sispro.migreja.domain.Arquivo;
import br.pro.sispro.migreja.domain.Curso;
import br.pro.sispro.migreja.domain.Disciplina;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.domain.Turma;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.sispro.migreja.dao.CursoDAO;
import br.sispro.migreja.dao.DisciplinaDAO;
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
public class TurmaBean implements Serializable {

	private Turma turma;
	private List<Turma> turmas;
	private List<Curso> cursos;
	private List<Disciplina> disciplinas;
	private String arquivoSelecionado;
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}
	
	public String getArquivoSelecionado() {
		return arquivoSelecionado;
	}

	public void setArquivoSelecionado(String arquivoSelecionado) {
		this.arquivoSelecionado = arquivoSelecionado;
	}

	public List<Temporaria> getTemporarias() {
		return temporarias;
	}

	public void setTemporarias(List<Temporaria> temporarias) {
		this.temporarias = temporarias;
	}

	@PostConstruct
	public void listar() {
		try {
			TurmaDAO turmaDAO = new TurmaDAO();
			turmas = turmaDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a turma!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		try {
			turma = new Turma();

			CursoDAO cursoDAO = new CursoDAO();
			cursos = cursoDAO.listar();

			DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
			disciplinas = disciplinaDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma novo curso e disciplina!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void salvar() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			TurmaDAO turmaDAO = new TurmaDAO();
			turmaDAO.salvar(turma);
			Long codigoRegistro = turma.getCodigo(); //Recupera o ID do Registro do banco de dados

			turma = new Turma();
			turmas = turmaDAO.listar();

			CursoDAO cursoDAO = new CursoDAO();
			cursos = cursoDAO.listar();

			DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
			disciplinas = disciplinaDAO.listar();
			
			arquivoSelecionado = null;
			
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
					ps.setString(4, "turma");
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
				
				Messages.addGlobalInfo("Turma salva com sucesso!");
			}	

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma turma!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			turma = (Turma) evento.getComponent().getAttributes().get("turmaSelecionada");

			TurmaDAO turmaDAO = new TurmaDAO();
			turmaDAO.excluir(turma);
			Long codigoRemovido= turma.getCodigo();//Recupera o ID do Registro removido

			turmas = turmaDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
										
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "turma");
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
					System.out.println("Erro! Não conseguiu inserir na tabela temporária: "+erro);
				}finally {
					con.close();
				}
				//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
				
				Messages.addGlobalInfo("Curso excluído: " + turma.getCurso().getNomeCurso());
				
			}
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um turma!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
				
		try {
					
			turma = (Turma) evento.getComponent().getAttributes().get("turmaSelecionada");

			CursoDAO cursoDAO = new CursoDAO();
			cursos = cursoDAO.listar();

			DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
			disciplinas = disciplinaDAO.listar();	
								
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar abrir o modal de editar!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
				
	}

	public void editar(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			TurmaDAO turmaDAO = new TurmaDAO();
			turmaDAO.editar(turma);
			Long codigoRegistro = turma.getCodigo();//Recupera o ID do Registro do banco de dados

			CursoDAO cursoDAO = new CursoDAO();
			cursos = cursoDAO.listar();
				
			DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
			disciplinas = disciplinaDAO.listar();
			
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
					ps.setString(4, "turma");
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
				
				Messages.addGlobalInfo("A turma foi editada com sucesso!");
			}	
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar editar a turma!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			turma = (Turma) evento.getComponent().getAttributes().get("turmaSelecionada");	
												
			TurmaDAO turmaDAO = new TurmaDAO();
			turmas = turmaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a turma!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void fileUploadHandler(FileUploadEvent event) throws IOException{
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(event.getFile().getFileName());
		arquivo.setStream(event.getFile().getInputstream());
		turma.setArquivo(arquivo);
		
		arquivoSelecionado = arquivo.getNome();
	}
	
	public void arquivoSelecionado(){
		
	}
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		TurmaDAO turmaDAO = new TurmaDAO();
		turmas = turmaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/turma.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.turmas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Tuma.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		TurmaDAO turmaDAO = new TurmaDAO();
		turmas = turmaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/turma.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.turmas));
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
		TurmaDAO turmaDAO = new TurmaDAO();
		turmas = turmaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/turma.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.turmas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Turma.doc");
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
		TurmaDAO turmaDAO = new TurmaDAO();
		turmas = turmaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/turma.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.turmas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Turma.ppt");
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
