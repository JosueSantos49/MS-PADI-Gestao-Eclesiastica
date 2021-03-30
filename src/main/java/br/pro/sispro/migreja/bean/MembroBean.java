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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.pro.sispro.migreja.domain.Instituicao;
import br.pro.sispro.migreja.domain.Membro;
import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.InstituicaoDAO;
import br.sispro.migreja.dao.MembroDAO;
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
public class MembroBean implements Serializable {

	private Membro membro;
	private List<Membro> membros;

	private List<Pessoa> pessoas;
	private List<Instituicao> instituicaos;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public List<Instituicao> getInstituicaos() {
		return instituicaos;
	}

	public void setInstituicaos(List<Instituicao> instituicaos) {
		this.instituicaos = instituicaos;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}

	public List<Membro> getMembros() {
		return membros;
	}

	public void setMembros(List<Membro> membros) {
		this.membros = membros;
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
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o membro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		try {
			membro = new Membro();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo membro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void salvar() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			//############# RECUPERANDO VALORES DO FORM ##################
			Long pessoa_selecionada = getMembro().getPessoa().getCodigo();
			
			//Verifico se a pessoa selecionada já, é membro
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			ps = con.prepareStatement("SELECT pessoa FROM membro WHERE pessoa = '" +pessoa_selecionada+ "'");
			
			ResultSet verifica_pessoa_selecionada = ps.executeQuery();
			
			if(verifica_pessoa_selecionada.next()) {
				
				Messages.addGlobalError("Já existe um membro cadastrado com a pessoa informada!");					
				
			}else {
				
				//################ PERSISTIR OS DADOS NO BANCO #####################
				MembroDAO membroDAO = new MembroDAO();
				membroDAO.salvar(membro);
				Long codigoRegistro = membro.getCodigo(); //Recupera o ID do Registro do banco de dados

				membro = new Membro();
				membros = membroDAO.listar();

				PessoaDAO pessoaDAO = new PessoaDAO();
				pessoas = pessoaDAO.listar();
				
				InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
				instituicaos = instituicaoDAO.listar();	
				
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
						ps.setString(4, "membro");
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

				Messages.addGlobalInfo("Membro salvo com sucesso!");
				
			}
						

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um membro!");
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
			
			membro = (Membro) evento.getComponent().getAttributes().get("membroSelecionado");

			MembroDAO membroDAO = new MembroDAO();
			membroDAO.excluir(membro);
			Long codigoRemovido= membro.getCodigo();//Recupera o ID do Registro removido

			membros = membroDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
					
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "membro");
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
			
			Messages.addGlobalInfo("Membro excluído: " + membro.getPessoa().getNome());			
						
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um membro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
		
		try {
			
			membro = (Membro) evento.getComponent().getAttributes().get("membroSelecionado");

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
		}
		catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
		
	}

	public void editar(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
				
		try {
			
			MembroDAO membroDAO = new MembroDAO();
			membroDAO.editar(membro);
			Long codigoRegistro = membro.getCodigo();//Recupera o ID do Registro do banco de dados
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
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
					ps.setString(4, "membro");
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
			
			Messages.addGlobalInfo("O membro foi editado com sucesso!");		
			

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			membro = (Membro) evento.getComponent().getAttributes().get("membroSelecionado");			
						
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o membro!");
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
			
			String caminho = Faces.getRealPath("/reports/membro.jasper");
						
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
		MembroDAO membroDAO = new MembroDAO();
		membros = membroDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/membro.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.membros));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Membro.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		MembroDAO membroDAO = new MembroDAO();
		membros = membroDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/membro.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.membros));
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
		MembroDAO membroDAO = new MembroDAO();
		membros = membroDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/membro.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.membros));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Membro.doc");
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
		MembroDAO membroDAO = new MembroDAO();
		membros = membroDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/membro.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.membros));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Membro.ppt");
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
