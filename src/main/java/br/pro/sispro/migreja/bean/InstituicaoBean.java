package br.pro.sispro.migreja.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.pro.sispro.migreja.domain.Cidade;
import br.pro.sispro.migreja.domain.Estado;
import br.pro.sispro.migreja.domain.Instituicao;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.CidadeDAO;
import br.sispro.migreja.dao.EstadoDAO;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings({ "serial", "deprecation" })
@ManagedBean
@ViewScoped
public class InstituicaoBean implements Serializable {

	private Instituicao instituicao;
	private List<Instituicao> instituicaos;

	private Estado estado;
	private List<Estado> estados;
	private List<Cidade> cidades;// armazenar cidade no BD
	
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public List<Instituicao> getInstituicaos() {
		return instituicaos;
	}

	public void setInstituicaos(List<Instituicao> instituicaos) {
		this.instituicaos = instituicaos;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar("dataCadastro");
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a intituição!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//##################### ABRIR O FORM NOVO REGISTRO ################################
	public void novo() {
		try {
			instituicao = new Instituicao();

			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();

			cidades = new ArrayList<Cidade>(); // Lista de cidade vazia

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova intituição!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//######################## PROCESSA O FORM CADASTRAR NOVO REGISTRO ###########################
	public void salvar() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			//############# RECUPERANDO VALORES DO FORM ##################
			String cnpj_digitado = getInstituicao().getCnpj();			
			
			//############################## VALIDAÇÃO PARA NÃO DUPLICAR ##################################  
			//Verifico se o CNPJ já existe na tabela  	
			ps = con.prepareStatement("SELECT cnpj FROM instituicao WHERE cnpj = '" +cnpj_digitado+ "'");			
			ResultSet verifica_cnpj = ps.executeQuery();
						
			if(verifica_cnpj.next()) {
				
				Messages.addGlobalError("Já existe uma instituição com o CNPJ informado!");					
				
			}else {	
			
							
				//################## SALVA NO BANCO ####################
				InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
				instituicaoDAO.salvar(instituicao);
				Long codigoRegistro = instituicao.getCodigo();//Recupera o ID do Registro do banco de dados
	
				instituicao = new Instituicao();
				instituicaos = instituicaoDAO.listar("dataCadastro");
	
				EstadoDAO estadoDAO = new EstadoDAO();
				estados = estadoDAO.listar();
				
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.listar("nome");	
				
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
						ps.setString(4, "instituicao");
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
					}
					//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
				}
	
				Messages.addGlobalInfo("Intituição salva com sucesso!");
			
			}
			

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma instituição!");
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
			
			instituicao = (Instituicao) evento.getComponent().getAttributes().get("instituicaoSelecionada");

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaoDAO.excluir(instituicao);
			Long codigoRemovido= instituicao.getCodigo();//Recupera o ID do Registro removido

			instituicaos = instituicaoDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "instituicao");
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
				}
				//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
			}

			Messages.addGlobalInfo("Instituição removida: " + instituicao.getRazaosocial());
			System.out.println();
			Messages.addGlobalInfo("CNPJ: " + instituicao.getCnpj());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover uma intituição!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
		try {
			instituicao = (Instituicao) evento.getComponent().getAttributes().get("instituicaoSelecionada");

			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
			
			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma cidade!");
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
					
					InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
					instituicaoDAO.editar(instituicao);
					Long codigoRegistrado = instituicao.getCodigo();//Recupera o ID do Registro do banco de dados

					EstadoDAO estadoDAO = new EstadoDAO();
					estados = estadoDAO.listar();
					
					CidadeDAO cidadeDAO = new CidadeDAO();
					cidades = cidadeDAO.listar();	
					
					//Se houve a edição no banco de dados vai gerar a PK (ID do Registro). Se PK for diferente de Nulo ouve a edição. 
					//Cadastrar na tabela temporária os dados. 
					if(codigoRegistrado != null) {
						
						//################### INSERIR NA TABELA TEMPORÁRIA #######################						
						try {
							
							con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
							
							//Salvar os dados na tabela temporária		
							ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
																	
							ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
							ps.setString(2, funcaoGlobal.pegaIpRede());				
							ps.setString(3, "editar");
							ps.setString(4, "instituicao");
							ps.setLong(5, codigoRegistrado);//chave estrangeira
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
						}
						//################### FIM INSERIR NA TABELA TEMPORÁRIA #######################
					}
					
					Messages.addGlobalInfo("A Instituição foi editada com sucesso!");
				
				
			} catch (RuntimeException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
				System.out.println("Ocorreu um erro ao tentar selecionar uma pessoa (Editar Aluno): "+erro);
				erro.printStackTrace();
				Messages.addGlobalError(erro.getMessage());
			}
		}
		
	
	public void visualizar(ActionEvent evento) {
		try {
			instituicao = (Instituicao) evento.getComponent().getAttributes().get("instituicaoSelecionada");
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar("dataCadastro");
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a instituição!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void popular() {

		try {
			if (estado != null) {
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
			} else {
				cidades = new ArrayList<Cidade>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}

	}
	
	public void imprimir() {
		try {
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			String razaoInstituicao = (String) filtros.get("razaosocial");
			String cnpjInstituicao = (String) filtros.get("cnpj");
			
			String caminho = Faces.getRealPath("/reports/instituicao.jasper");
						
			Map<String, Object> parametros = new HashMap<>();
						
			if(razaoInstituicao == null){
				parametros.put("razaosocial", "%%");
			}else{
				parametros.put("razaosocial", "%" + razaoInstituicao + "%");
			}
			if(cnpjInstituicao == null){
				parametros.put("cnpj", "%%");
			}else{
				parametros.put("cnpj", "%"+ cnpjInstituicao + "%");
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
	
	
	/*public void imprimir() {
		try {
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			String razaoInstituicao = (String) filtros.get("razaosocial");
			String cnpjInstituicao = (String) filtros.get("cnpj");
			
			String caminho = Faces.getRealPath("/reports/instituicao.jasper");
						
			Map<String, Object> parametros = new HashMap<>();
						
			if(razaoInstituicao == null){
				parametros.put("razaosocial", "%%");
			}else{
				parametros.put("razaosocial", "%" + razaoInstituicao + "%");
			}
			if(cnpjInstituicao == null){
				parametros.put("cnpj", "%%");
			}else{
				parametros.put("cnpj", "%"+ cnpjInstituicao + "%");
			}
						
			Connection conexao = HibernateUtil.getConexao();			
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);					
			JasperPrintManager.printReport(relatorio, true);		
									
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar imprimir a instituição!");			
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}*/	
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaos = instituicaoDAO.listar();
			
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/instituicao.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.instituicaos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Instituicao.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaos = instituicaoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/instituicao.jasper"));		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, new JRBeanCollectionDataSource(this.instituicaos));
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		ServletOutputStream outStream = response.getOutputStream();
		outStream.write(bytes, 0 , bytes.length);
		outStream.flush();
		outStream.close();
			
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void exportarExcel(ActionEvent actionEvent) throws JRException, IOException{
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaos = instituicaoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/instituicao.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.instituicaos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Instituicao.xls");
		ServletOutputStream outStream = response.getOutputStream();
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
		
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();			
	}
		
	public void exportarDOC(ActionEvent actionEvent) throws JRException, IOException{
		
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaos = instituicaoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/instituicao.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),null, new JRBeanCollectionDataSource(this.instituicaos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Instituicao.doc");
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
		
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaos = instituicaoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/instituicao.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),null, new JRBeanCollectionDataSource(this.instituicaos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Instituicao.ppt");
		ServletOutputStream outStream = response.getOutputStream();
		
		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
		
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void imprimirRelatorio(){		
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();			
			List<Instituicao> listaIntituicao = new ArrayList<>();		
			listaIntituicao = instituicaoDAO.listarTodos();
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaIntituicao);
			
			HashMap parametros = new HashMap();	
			//Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();			
			facesContext.responseComplete();
			
			ServletContext scContext = (ServletContext)facesContext.getExternalContext().getContext();			
			JasperPrint jasperPrint = JasperFillManager.fillReport(scContext.getRealPath("/reports/instituicao.jasper"), parametros, ds);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();			
			JRPdfExporter exporter = new JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			
			byte[] bytes = baos.toByteArray();
			
			if(bytes != null && bytes.length > 0){
				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
				
				response.setContentType("application/pdf");				
				response.setHeader("Content-disposition", "inline; filename=\"relatorioPorData.pdf\"");
				response.setContentLength(bytes.length);
				
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(bytes, 0, bytes.length);
				outputStream.flush();
				outputStream.close();				
			}				
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao tentar gerar relatório da instituição!!!");
		}
	}
}
