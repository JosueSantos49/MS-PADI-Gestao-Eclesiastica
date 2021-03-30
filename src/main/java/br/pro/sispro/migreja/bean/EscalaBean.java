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
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.pro.sispro.migreja.domain.Escala;
import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.EscalaDAO;
import br.sispro.migreja.dao.PessoaDAO;
import br.sispro.migreja.dao.TemporariaDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings({ "serial", "deprecation" })
@ManagedBean
@ViewScoped
public class EscalaBean implements Serializable {

	private Escala escala;
	private List<Escala> escalas;
	private List<Pessoa> pessoas;
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal(); 
	
	@PersistenceContext
	private EntityManager entityManager;
		
	HtmlInputText escalaInput = new HtmlInputText();

	public Escala getEscala() {
		return escala;
	}

	public void setEscala(Escala escala) {
		this.escala = escala;
	}

	public List<Escala> getEscalas() {
		return escalas;
	}

	public void setEscalas(List<Escala> escalas) {
		this.escalas = escalas;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}
		
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
			
			EscalaDAO escalaDAO = new EscalaDAO();
			escalas = escalaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar escala!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//############### ABRIR O FORM DE CADASTRO ########################
	public void novo() {
		
		try {
			
			escala = new Escala();
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova escala!");
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
			String funcao = getEscala().getFuncao();
			String atividade = getEscala().getAtividade();
						
			//############# VALIDAÇÃO DE CAMPOS ############
			if(funcao != null && funcao.isEmpty()) {
				Messages.addGlobalError("Informar a função!");				
				return;
			}
			if(atividade != null && atividade.isEmpty()) {
				Messages.addGlobalError("Informar a atividade!");				
				return;
			}
			
			//################## SALVA A ESCALA NO BANCO ####################
			EscalaDAO escalaDAO = new EscalaDAO();
			escalaDAO.salvar(escala);						
			Long codigoRegistro = escala.getCodigo();//Recupera o ID do Registro do banco de dados
			
			//################### ATUALIZA A LISTA DE ESCALAS####################
			escala = new Escala();					
			escalas = escalaDAO.listar();
			
			//###################### ATUALIZA A LISTA DE PESSOAS ###################
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			//Se ouve a inserção da ESCALA no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a inserção. 
			//Cadastrar na tabela temporária os dados. 
			if(codigoRegistro != null) {
			
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária para salvar no log do gatilho			
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
										
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "inserir");
					ps.setString(4, "escala");
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
				
				Messages.addGlobalInfo("Escala salva com sucesso!");
			}
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma escala!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			escala = (Escala) evento.getComponent().getAttributes().get("escalaSelecionada");

			EscalaDAO escalaDAO = new EscalaDAO();
			escalaDAO.excluir(escala);
			Long codigoRemovido= escala.getCodigo();//Recupera o ID do Registro removido	

			escalas = escalaDAO.listar();
			
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
				
				Messages.addGlobalInfo("Escala excluida!");
			}
					
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover a escala!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
		
		try {
			
			//Verificar a escala selecionada
			escala = (Escala) evento.getComponent().getAttributes().get("escalaSelecionada");
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a escala!");
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
			
			//############# RECUPERANDO VALORES DO FORM ##################
			String funcao = getEscala().getFuncao();
			String atividade = getEscala().getAtividade();			
			
			//############# VALIDAÇÃO DE CAMPOS############
			if(funcao != null && funcao.isEmpty()) {
				Messages.addGlobalError("Informar a função!");				
				return;
			}
			if(atividade != null && atividade.isEmpty()) {
				Messages.addGlobalError("Informar a atividade!");				
				return;
			}
			
			EscalaDAO escalaDAO = new EscalaDAO();
			escalaDAO.editar(escala);
			Long codigoRegistro = escala.getCodigo();
						
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
			
			Messages.addGlobalInfo("A Escala foi editada com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma escala!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			escala = (Escala) evento.getComponent().getAttributes().get("escalaSelecionada");	
									
			EscalaDAO escalaDAO = new EscalaDAO();
			escalas = escalaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a escala do membro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void imprimir() {

		try {
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			String nomePessoa = (String) filtros.get("nome");
			String cpfPessoa = (String) filtros.get("cpf");
			
			String caminho = Faces.getRealPath("/reports/escala.jasper");
						
			Map<String, Object> parametros = new HashMap<>();
						
			if(nomePessoa == null){
				parametros.put("nome", "%%");
			}else{
				parametros.put("nome", "%" + nomePessoa + "%");
			}
			if(cpfPessoa == null){
				parametros.put("cpf", "%%");
			}else{
				parametros.put("cpf", "%"+ cpfPessoa + "%");
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
	
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		EscalaDAO escalaDAO = new EscalaDAO();
		escalas = escalaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/escala.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.escalas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Escala.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		EscalaDAO escalaDAO = new EscalaDAO();
		escalas = escalaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/escala.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.escalas));
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
		EscalaDAO escalaDAO = new EscalaDAO();
		escalas = escalaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/escala.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.escalas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Escala.doc");
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
		EscalaDAO escalaDAO = new EscalaDAO();
		escalas = escalaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/escala.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.escalas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Escala.ppt");
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
