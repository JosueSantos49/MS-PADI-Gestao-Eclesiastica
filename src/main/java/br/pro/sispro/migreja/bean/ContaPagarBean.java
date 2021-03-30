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

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.pro.sispro.migreja.domain.ContaPagar;
import br.pro.sispro.migreja.domain.ControleCaixa;
import br.pro.sispro.migreja.domain.Instituicao;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.ContaPagarDAO;
import br.sispro.migreja.dao.ControleCaixaDAO;
import br.sispro.migreja.dao.InstituicaoDAO;
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
public class ContaPagarBean implements Serializable {

	private ContaPagar contaPagar;
	private List<ContaPagar> contaPagars;	
	private List<Instituicao> instituicaos;
	private List<ControleCaixa> controleCaixas;	
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public List<Instituicao> getInstituicaos() {
		return instituicaos;
	}

	public void setInstituicaos(List<Instituicao> instituicaos) {
		this.instituicaos = instituicaos;
	}
	
	public ContaPagar getContaPagar() {
		return contaPagar;
	}

	public void setContaPagar(ContaPagar contaPagar) {
		this.contaPagar = contaPagar;
	}

	public List<ContaPagar> getContaPagars() {
		return contaPagars;
	}

	public void setContaPagars(List<ContaPagar> contaPagars) {
		this.contaPagars = contaPagars;
	}	

	public List<ControleCaixa> getControleCaixas() {
		return controleCaixas;
	}

	public void setControleCaixas(List<ControleCaixa> controleCaixas) {
		this.controleCaixas = controleCaixas;
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
			
			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagars = contaPagarDAO.listar();
						
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o conta a pagar!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//##################### ABRIR O FORM CONTA A PAGAR ################################
	public void novo() {
		try {
			contaPagar = new ContaPagar();

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();	
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();	
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova conta!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//######################## PROCESSA O FORM CONTA A PAGAR ###########################
	//A conta a pagar será salva na tabela contapagar, mas ainda não será registrada
	public void salvar() throws Exception{
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagarDAO.salvar(contaPagar);
			Long codigoRegistro = contaPagar.getCodigo();//Recupera o ID do Registro do banco de dados

			contaPagar = new ContaPagar();
			contaPagars = contaPagarDAO.listar();

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();		
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();	
			
			//Se ouve a inserção no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a inserção. 
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
					ps.setString(4, "contapagar");
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

			Messages.addGlobalInfo("Conta a Pagar salva com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma conta!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//##################### ABRIR O FORM REGISTRAR PAGAMENTO ################################
	public void registrarPagamento(ActionEvent evento){
	
		try{
			contaPagar = (ContaPagar) evento.getComponent().getAttributes().get("contaSelecionada");					
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();	
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a conta a pagar, para registro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}		
	}
	
	//################ PROCESSA O REGISTRO DO PAGAMENTO NO BANCO ####################
	//Pega a conta salva na tabela contapagar e registra na tabela controlecaixa efetiva o pagamento no controle do caixa
	public void salvarRegistroPgto() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			//############## PROCESSA E SALVA A CONTA NO CONTROLE DO CAIXA #########################
			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagarDAO.salvarRegistro(contaPagar);
			Long codigoRegistro = contaPagar.getCodigo();//Recupera o ID do Registro do banco de dados
									
			contaPagar = new ContaPagar();
			contaPagars = contaPagarDAO.listar();

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();	
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();	
			
			//Se ouve a inserção no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a inserção. 
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
					ps.setString(4, "controlecaixa");
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
				
				Messages.addGlobalInfo("Registro de conta salvo com sucesso!");
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o registro da conta!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	

	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			contaPagar = (ContaPagar) evento.getComponent().getAttributes().get("contaSelecionada");

			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagarDAO.excluir(contaPagar);
			Long codigoRemovido= contaPagar.getCodigo();//Recupera o ID do Registro removido	

			contaPagars = contaPagarDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "contapagar");
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
	
			Messages.addGlobalInfo("Conta a pagar excluído!");			

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover uma conta!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################	
	public void novoEditar(ActionEvent evento) {
		
		try {			
			
			contaPagar = (ContaPagar) evento.getComponent().getAttributes().get("contaSelecionada");

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a conta a pagar!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//################## PROCESSA A EDIÇÃO DA CONTA A PAGAR ##########################
	public void editar(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagarDAO.editar(contaPagar);
			Long codigoRegistro = contaPagar.getCodigo();//Recupera o ID do Registro do banco de dados
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();
			
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
					ps.setString(4, "contapagar");
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
			
			Messages.addGlobalInfo("A conta a pagar foi editada com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar editar a conta a pagar!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			contaPagar = (ContaPagar) evento.getComponent().getAttributes().get("contaSelecionada");	
									
			ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
			contaPagars = contaPagarDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a conta a pagar!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void imprimir() {
		try {
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			String nomeDepartamentoo = (String) filtros.get("nomeDepartamento");
			String presidenteDepartamento = (String) filtros.get("presidente");
			
			String caminho = Faces.getRealPath("/reports/departamento.jasper");
						
			Map<String, Object> parametros = new HashMap<>();
						
			if(nomeDepartamentoo == null){
				parametros.put("nomeDepartamento", "%%");
			}else{
				parametros.put("nomeDepartamento", "%" + nomeDepartamentoo + "%");
			}
			if(presidenteDepartamento == null){
				parametros.put("presidente", "%%");
			}else{
				parametros.put("presidente", "%"+ presidenteDepartamento + "%");
			}
						
			Connection conexao = HibernateUtil.getConexao();
			
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			JasperPrintManager.printReport(relatorio, true);
			
			
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar imprimir a instituição!");			
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
		contaPagars = contaPagarDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/contaPagar.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.contaPagars));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=contaPagar.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
		contaPagars = contaPagarDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/contaPagar.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.contaPagars));
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
		ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
		contaPagars = contaPagarDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/contaPagar.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.contaPagars));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=contaPagar.doc");
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
		ContaPagarDAO contaPagarDAO = new ContaPagarDAO();
		contaPagars = contaPagarDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/contaPagar.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.contaPagars));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=contaPagar.ppt");
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
