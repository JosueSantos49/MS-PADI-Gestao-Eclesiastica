package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.Dizimo;
import br.pro.sispro.migreja.domain.Instituicao;
import br.pro.sispro.migreja.domain.Membro;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.sispro.migreja.dao.DizimoDAO;
import br.sispro.migreja.dao.InstituicaoDAO;
import br.sispro.migreja.dao.MembroDAO;
import br.sispro.migreja.dao.TemporariaDAO;
import jersey.repackaged.com.google.common.base.MoreObjects;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings("deprecation")
@WebListener
@ManagedBean
@ViewScoped
public class DizimoBean implements ServletContextListener {

	private Dizimo dizimo;
	private List<Dizimo> dizimos;
	private List<Membro> membros;
	private List<Instituicao> instituicaos;
	private List<Temporaria> temporarias;
	
	private List<Integer> linhas = new ArrayList<Integer>();
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public Dizimo getDizimo() {
		return dizimo;
	}

	public void setDizimo(Dizimo dizimo) {
		this.dizimo = dizimo;
	}

	public List<Dizimo> getDizimos() {
		return dizimos;
	}

	public void setDizimos(List<Dizimo> dizimos) {
		this.dizimos = dizimos;
	}

	public List<Membro> getMembros() {
		return membros;
	}

	public void setMembros(List<Membro> membros) {
		this.membros = membros;
	}
	
	public List<Instituicao> getInstituicaos() {
		return instituicaos;
	}

	public void setInstituicaos(List<Instituicao> instituicaos) {
		this.instituicaos = instituicaos;
	}

	public List<Temporaria> getTemporarias() {
		return temporarias;
	}

	public void setTemporarias(List<Temporaria> temporarias) {
		this.temporarias = temporarias;
	}

	public List<Integer> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<Integer> linhas) {
		this.linhas = linhas;
	}
		
	@PostConstruct
	public void listar() {
		try {
			DizimoDAO dizimoDAO = new DizimoDAO();
			dizimos = dizimoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		
		try {
			
			dizimo = new Dizimo();

			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();	

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	

	public void salvar() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			DizimoDAO dizimoDAO = new DizimoDAO();
			System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
			
			if((dizimo.getOferta() == null || dizimo.getOferta().equals(""))){
				System.out.println("Oferta: null ou vazio");
			}
			if(
					(dizimo.getOferta() == null || dizimo.getOferta().equals(""))|| 
					(dizimo.getValor()  == null) || 
					(dizimo.getMembro0v() == null) || 
					(dizimo.getMembro1v() == null) || 
					(dizimo.getMembro2v() == null) || 
					(dizimo.getMembro3v() == null) || 
					(dizimo.getMembro4v() == null) || 
					(dizimo.getMembro5v() == null) || 
					(dizimo.getMembro6v() == null) || 
					(dizimo.getMembro7v() == null) || 
					(dizimo.getMembro8v() == null) || 
					(dizimo.getMembro9v() == null) || 
					(dizimo.getMembro10v() == null) || 
					(dizimo.getMembro11v() == null) || 
					(dizimo.getMembro12v() == null) || 
					(dizimo.getMembro13v() == null) || 
					(dizimo.getMembro14v() == null) || 
					(dizimo.getMembro15v() == null) || 
					(dizimo.getMembro16v() == null) || 
					(dizimo.getMembro17v() == null) || 
					(dizimo.getMembro18v() == null) || 
					(dizimo.getMembro19v() == null) || 
					(dizimo.getMembro20v() == null) ||
					(dizimo.getOutros() == null)
				){
				Double oferta = (double) MoreObjects.firstNonNull(dizimo.getOferta(), 0.0);				
				Double valor = (double) MoreObjects.firstNonNull(dizimo.getValor(), 0.0);
				Double membro0v = (double) MoreObjects.firstNonNull(dizimo.getMembro0v(), 0.0);
				Double membro1v = (double) MoreObjects.firstNonNull(dizimo.getMembro1v(), 0.0);
				Double membro2v = (double) MoreObjects.firstNonNull(dizimo.getMembro2v(), 0.0);
				Double membro3v = (double) MoreObjects.firstNonNull(dizimo.getMembro3v(), 0.0);
				Double membro4v = (double) MoreObjects.firstNonNull(dizimo.getMembro4v(), 0.0);
				Double membro5v = (double) MoreObjects.firstNonNull(dizimo.getMembro5v(), 0.0);
				Double membro6v = (double) MoreObjects.firstNonNull(dizimo.getMembro6v(), 0.0);
				Double membro7v = (double) MoreObjects.firstNonNull(dizimo.getMembro7v(), 0.0);
				Double membro8v = (double) MoreObjects.firstNonNull(dizimo.getMembro8v(), 0.0);
				Double membro9v = (double) MoreObjects.firstNonNull(dizimo.getMembro9v(), 0.0);
				Double membro10v = (double) MoreObjects.firstNonNull(dizimo.getMembro10v(), 0.0);
				Double membro11v = (double) MoreObjects.firstNonNull(dizimo.getMembro11v(), 0.0);
				Double membro12v = (double) MoreObjects.firstNonNull(dizimo.getMembro12v(), 0.0);
				Double membro13v = (double) MoreObjects.firstNonNull(dizimo.getMembro13v(), 0.0);
				Double membro14v = (double) MoreObjects.firstNonNull(dizimo.getMembro14v(), 0.0);
				Double membro15v = (double) MoreObjects.firstNonNull(dizimo.getMembro15v(), 0.0);
				Double membro16v = (double) MoreObjects.firstNonNull(dizimo.getMembro16v(), 0.0);
				Double membro17v = (double) MoreObjects.firstNonNull(dizimo.getMembro17v(), 0.0);
				Double membro18v = (double) MoreObjects.firstNonNull(dizimo.getMembro18v(), 0.0);
				Double membro19v = (double) MoreObjects.firstNonNull(dizimo.getMembro19v(), 0.0);
				Double membro20v =(double) MoreObjects.firstNonNull( dizimo.getMembro20v(), 0.0);
				Double outros =(double) MoreObjects.firstNonNull( dizimo.getOutros(), 0.0);
			
				double total = (oferta + valor + membro0v +  membro1v + membro2v + membro3v+ membro4v+	 membro5v+ membro6v+  membro7v+  membro8v+	 membro9v+ membro10v+ membro11v+ membro12v+ membro13v+ membro14v+ membro15v+  membro16v+ membro17v+ membro18v+ membro19v+ membro20v+outros);
				System.out.println("Total soma: "+total);
				
				dizimo.setTotal(total);			
				dizimoDAO.salvar(dizimo);
				Long codigoRegistro = dizimo.getCodigo(); //Recupera o ID do Registro do banco de dados
	
				dizimo = new Dizimo();
				dizimos = dizimoDAO.listar();
	
				MembroDAO membroDAO = new MembroDAO();
				membros = membroDAO.listar();	
				
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
						ps.setString(4, "dizimo");
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
					
					Messages.addGlobalInfo("Dízimo salvo com sucesso!");
				}	
				
			}
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void registrarDizimo(ActionEvent evento){
		
		try{
			
			dizimo = (Dizimo) evento.getComponent().getAttributes().get("dizimoSelecionado");			
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a conta a pagar, para registro!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}		
	}
	
	public void salvarRegistroDizimo() throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			DizimoDAO dizimoDAO = new DizimoDAO();
			dizimoDAO.salvarRegistro(dizimo);
			Long codigoRegistro = dizimo.getCodigo(); //Recupera o ID do Registro do banco de dados
									
			dizimo = new Dizimo();
			dizimos = dizimoDAO.listar();

			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();	
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
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
				
				Messages.addGlobalInfo("Registro de dízimo salvo com sucesso!");
			}
			

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o registro do dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			dizimo = (Dizimo) evento.getComponent().getAttributes().get("dizimoSelecionado");

			DizimoDAO dizimoDAO = new DizimoDAO();
			dizimoDAO.excluir(dizimo);
			Long codigoRemovido= dizimo.getCodigo();//Recupera o ID do Registro removido

			dizimos = dizimoDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
										
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "dizimo");
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
				
				Messages.addGlobalInfo("Dízimo excluído: " + dizimo.getDescricao());				
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novoEditar(ActionEvent evento) {
		
		try {
			
			dizimo = (Dizimo) evento.getComponent().getAttributes().get("dizimoSelecionado");

			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();	

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void editar(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			DizimoDAO dizimoDAO = new DizimoDAO();
			
			//##################### ATUALIZA O VALOR TOTAL DO DÍZIMO ################################
			System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
			
			if((dizimo.getOferta() == null || dizimo.getOferta().equals(""))){
				System.out.println("Oferta: null ou vazio");
			}
			if(
					(dizimo.getOferta() == null || dizimo.getOferta().equals(""))|| 
					(dizimo.getValor()  == null) || 
					(dizimo.getMembro0v() == null) || 
					(dizimo.getMembro1v() == null) || 
					(dizimo.getMembro2v() == null) || 
					(dizimo.getMembro3v() == null) || 
					(dizimo.getMembro4v() == null) || 
					(dizimo.getMembro5v() == null) || 
					(dizimo.getMembro6v() == null) || 
					(dizimo.getMembro7v() == null) || 
					(dizimo.getMembro8v() == null) || 
					(dizimo.getMembro9v() == null) || 
					(dizimo.getMembro10v() == null) || 
					(dizimo.getMembro11v() == null) || 
					(dizimo.getMembro12v() == null) || 
					(dizimo.getMembro13v() == null) || 
					(dizimo.getMembro14v() == null) || 
					(dizimo.getMembro15v() == null) || 
					(dizimo.getMembro16v() == null) || 
					(dizimo.getMembro17v() == null) || 
					(dizimo.getMembro18v() == null) || 
					(dizimo.getMembro19v() == null) || 
					(dizimo.getMembro20v() == null) ||
					(dizimo.getOutros() == null)
				){
				Double oferta = (double) MoreObjects.firstNonNull(dizimo.getOferta(), 0.0);				
				Double valor = (double) MoreObjects.firstNonNull(dizimo.getValor(), 0.0);
				Double membro0v = (double) MoreObjects.firstNonNull(dizimo.getMembro0v(), 0.0);
				Double membro1v = (double) MoreObjects.firstNonNull(dizimo.getMembro1v(), 0.0);
				Double membro2v = (double) MoreObjects.firstNonNull(dizimo.getMembro2v(), 0.0);
				Double membro3v = (double) MoreObjects.firstNonNull(dizimo.getMembro3v(), 0.0);
				Double membro4v = (double) MoreObjects.firstNonNull(dizimo.getMembro4v(), 0.0);
				Double membro5v = (double) MoreObjects.firstNonNull(dizimo.getMembro5v(), 0.0);
				Double membro6v = (double) MoreObjects.firstNonNull(dizimo.getMembro6v(), 0.0);
				Double membro7v = (double) MoreObjects.firstNonNull(dizimo.getMembro7v(), 0.0);
				Double membro8v = (double) MoreObjects.firstNonNull(dizimo.getMembro8v(), 0.0);
				Double membro9v = (double) MoreObjects.firstNonNull(dizimo.getMembro9v(), 0.0);
				Double membro10v = (double) MoreObjects.firstNonNull(dizimo.getMembro10v(), 0.0);
				Double membro11v = (double) MoreObjects.firstNonNull(dizimo.getMembro11v(), 0.0);
				Double membro12v = (double) MoreObjects.firstNonNull(dizimo.getMembro12v(), 0.0);
				Double membro13v = (double) MoreObjects.firstNonNull(dizimo.getMembro13v(), 0.0);
				Double membro14v = (double) MoreObjects.firstNonNull(dizimo.getMembro14v(), 0.0);
				Double membro15v = (double) MoreObjects.firstNonNull(dizimo.getMembro15v(), 0.0);
				Double membro16v = (double) MoreObjects.firstNonNull(dizimo.getMembro16v(), 0.0);
				Double membro17v = (double) MoreObjects.firstNonNull(dizimo.getMembro17v(), 0.0);
				Double membro18v = (double) MoreObjects.firstNonNull(dizimo.getMembro18v(), 0.0);
				Double membro19v = (double) MoreObjects.firstNonNull(dizimo.getMembro19v(), 0.0);
				Double membro20v =(double) MoreObjects.firstNonNull( dizimo.getMembro20v(), 0.0);
				Double outros =(double) MoreObjects.firstNonNull( dizimo.getOutros(), 0.0);
			
				double total = (oferta + valor + membro0v +  membro1v + membro2v + membro3v+ membro4v+	 membro5v+ membro6v+  membro7v+  membro8v+	 membro9v+ membro10v+ membro11v+ membro12v+ membro13v+ membro14v+ membro15v+  membro16v+ membro17v+ membro18v+ membro19v+ membro20v+outros);
				System.out.println("Total soma do dízimo atualizar: "+total);
				
				dizimo.setTotal(total);	
				//##################### FIM ATUALIZA O VALOR TOTAL DO DÍZIMO ################################
				
				dizimoDAO.editar(dizimo);
				Long codigoRegistro = dizimo.getCodigo(); //Recupera o ID do Registro do banco de dados
				
				MembroDAO membroDAO = new MembroDAO();
				membros = membroDAO.listar();
				
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
						ps.setString(4, "dizimo");
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
					
					Messages.addGlobalInfo("O Dízimo foi editado com sucesso!");
				}
			
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um dízimo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		
		try {
			
			dizimo = (Dizimo) evento.getComponent().getAttributes().get("dizimoSelecionado");			
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		DizimoDAO dizimoDAO = new DizimoDAO();
		dizimos = dizimoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/dizimo.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.dizimos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Dizimo.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		DizimoDAO dizimoDAO = new DizimoDAO();
		dizimos = dizimoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/dizimo.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.dizimos));
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
		DizimoDAO dizimoDAO = new DizimoDAO();
		dizimos = dizimoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/dizimo.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.dizimos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Dizimo.doc");
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
		DizimoDAO dizimoDAO = new DizimoDAO();
		dizimos = dizimoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/dizimo.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.dizimos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Dizimo.ppt");
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
