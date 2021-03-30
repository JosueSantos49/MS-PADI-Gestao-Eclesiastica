package br.pro.sispro.migreja.bean;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.pro.sispro.migreja.domain.Cidade;
import br.pro.sispro.migreja.domain.Estado;
import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.domain.Temporaria;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.EmailUtils;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.pro.sispro.migreja.util.IndexController;
import br.sispro.migreja.dao.CidadeDAO;
import br.sispro.migreja.dao.EstadoDAO;
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
public class PessoaBean implements Serializable {

	private Pessoa pessoa;
	private List<Pessoa> pessoas;
	private List<Pessoa> listaPessoas = new ArrayList<Pessoa>();

	private Estado estado;
	private List<Estado> estados;
	private List<Cidade> cidades;// armazenar cidade no BD
	
	private List<Temporaria> temporarias;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();
	
	private Cidade cidade = new Cidade();
	
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
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

	public List<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public void setListaPessoas(List<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
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
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
						
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//####################### ABRIR FORM CADASTRAR ##########################
	public void novo() {
		try {
			pessoa = new Pessoa();
			
			estado = new Estado();

			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar("nome");

			cidades = new ArrayList<>(); // Lista de cidade vazia

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova pessoa!");
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
				String cpf_digitado = getPessoa().getCpf();			
				
				//############################## VALIDAÇÃO PARA NÃO DUPLICAR REGISTRO DE CONTA A PAGAR ##################################  
				//Verifico se o CPF já existe na tabela PESSOA 	
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				ps = con.prepareStatement("SELECT cpf FROM pessoa WHERE cpf = '" +cpf_digitado+ "'");			
				ResultSet verifica_cpf = ps.executeQuery();
							
				if(verifica_cpf.next()) {
					
					Messages.addGlobalError("Já existe uma pessoa com o CPF informado!");					
					
				}else {				
					
					//################### O CPF NÃO EXISTE PERSISTE NO BANCO #####################
					PessoaDAO pessoaDAO = new PessoaDAO();				
					pessoaDAO.salvar(pessoa);
					Long codigoRegistro = pessoa.getCodigo(); //Recupera o ID do Registro do banco de dados
					
					//################ CONFIRMAÇÃO DE CADASTRO POR E-MAIL #################
					pessoaDAO.enviarConfirmacaoCadastroPorEmail(pessoa);
					
					pessoas = pessoaDAO.listar("nome");
					
					pessoa = new Pessoa();
					
					estado = new Estado();
					
					EstadoDAO estadoDAO = new EstadoDAO();
					estados = estadoDAO.listar("nome");
					
					cidades = new ArrayList<>(); 
					
					/*
					//Se tiver foto cadastrar, e se não tiver ignorar.
					if(pessoa.getCaminho() != null){
						
						PessoaDAO pessoaDAO = new PessoaDAO();				
						Pessoa pessoaRetorno = pessoaDAO.merge(pessoa);
						
						if(pessoaRetorno != null){
							pessoaDAO.enviarConfirmacaoCadastroPorEmail(pessoa);
						}
						
						Path origem = Paths.get(pessoa.getCaminho());
						Path destino = Paths.get("C:/Upload/" + pessoaRetorno.getCodigo() + ".png");
						Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
	
						pessoa = new Pessoa();
						pessoas = pessoaDAO.listar();
	
						CidadeDAO cidadeDAO = new CidadeDAO();
						cidades = cidadeDAO.listar();
	
						Messages.addGlobalInfo("Pessoa salva com sucesso!");
	
					}
					if(pessoa.getCaminho() == null || pessoa.getCaminho().isEmpty()){
						
						PessoaDAO pessoaDAO = new PessoaDAO();
						Pessoa pessoaRetorno = pessoaDAO.merge(pessoa);
						
						if(pessoaRetorno != null){
							pessoaDAO.enviarConfirmacaoCadastroPorEmail(pessoa);
						}
						
						pessoa = new Pessoa();
						pessoas = pessoaDAO.listar();
	
						CidadeDAO cidadeDAO = new CidadeDAO();
						cidades = cidadeDAO.listar();
	
						Messages.addGlobalInfo("Pessoa salva com sucesso!");
					}*/
					
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
							ps.setString(4, "pessoa");
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
						
						Messages.addGlobalInfo("Pessoa salva com sucesso!");
					}
																						
				}										
						
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma pessoa!");
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
			
			pessoa = (Pessoa) evento.getComponent().getAttributes().get("pessoaSelecionada");

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoaDAO.excluir(pessoa);
			Long codigoRemovido= pessoa.getCodigo();//Recupera o ID do Registro removido
			
			/*
			//Excluir a pessoa se tiver arquivo, se nao existir iguinora
			Path arquivo = Paths.get("C:/Upload/" + pessoa.getCodigo() + ".png");
			Files.deleteIfExists(arquivo);*/

			pessoas = pessoaDAO.listar();
			
			if(codigoRemovido != null) {
				
				//################### INSERIR NA TABELA TEMPORÁRIA #######################						
				try {
										
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					
					//Salvar os dados na tabela temporária		
					ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
															
					ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
					ps.setString(2, funcaoGlobal.pegaIpRede());				
					ps.setString(3, "remover");
					ps.setString(4, "pessoa");
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
				
				Messages.addGlobalInfo("Pessoa excluída: " + pessoa.getNome());				
			}
			
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover uma pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ ABRIR O FORM DE EDIÇÃO ###########################
	//Carregar a informação selecionada
	public void novoEditar(ActionEvent evento) {
		
		try {
			
			pessoa = (Pessoa) evento.getComponent().getAttributes().get("pessoaSelecionada");
			
			/*
			//Quando editar criar o caminho do arquivo
			pessoa.setCaminho("C:/Upload/" + pessoa.getCodigo() + ".png");*/

			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar("nome");

			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.listar();
						
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
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
			
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoaDAO.editar(pessoa);
			Long codigoRegistro = pessoa.getCodigo();//Recupera o ID do Registro do banco de dados
			
			/*
			//Quando editar criar o caminho do arquivo
			pessoa.setCaminho("C:/Upload/" + pessoa.getCodigo() + ".png");*/
			
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar("nome");
						
			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.listar();
			
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
					ps.setString(4, "pessoa");
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
				
				Messages.addGlobalInfo("A pessoa foi editada com sucesso!");
			}						

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma cidade!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		
		try {
			
			pessoa = (Pessoa) evento.getComponent().getAttributes().get("pessoaSelecionada");	
									
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a pessoa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	//############################ POPULA E FILTRA AS CIDADES #############################
	// Na view deve configurar o Ajax atualizar o ID do form com cidade em um SELECT
	public void popular() {
		try {
			if (estado != null) {
				
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
				
				//EstadoDAO estadoDAO = new EstadoDAO();
				//estados = estadoDAO.buscarEstado(estado.getCodigo());
				
			} else {
				cidades = new ArrayList<Cidade>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void enviaEmailPessoa() {
		try {
			pessoa = new Pessoa();
			EmailUtils.enviaEmailPessoa(pessoa);

		} catch (EmailException ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro! Occoreu um erro ao enviar a mensagem.", "Erro"));
			Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
	}
	
	public void imprimir() {
		try {
			//Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			String nomePessoa = (String) filtros.get("nome");
			String cpfPessoa = (String) filtros.get("cpf");
			
			String caminho = Faces.getRealPath("/reports/pessoa.jasper");
						
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
		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoas = pessoaDAO.listar();
		
		CidadeDAO cidadeDAO = new CidadeDAO();
		cidades = cidadeDAO.listar();
		
		cidade = new Cidade();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String nome = (String) filtros.get("nome");
		String cpf = (String) filtros.get("cpf");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/pessoa.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("nome", "%"+nome+"%");	
		parametros.put("cpf", "%"+cpf+"%");
		parametros.put("cidade_codigo", cidade.getCodigo());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.pessoas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Pessoa.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoas = pessoaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/pessoa.jasper"));		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, new JRBeanCollectionDataSource(this.pessoas));
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
		
		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoas = pessoaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/pessoa.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),null, new JRBeanCollectionDataSource(this.pessoas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Pessoa.doc");
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
		
		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoas = pessoaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/pessoa.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),null, new JRBeanCollectionDataSource(this.pessoas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Pessoa.ppt");
		ServletOutputStream outStream = response.getOutputStream();
		
		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
		
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	//Upload de arquivo 
	public void upload(FileUploadEvent event){		
		
		try {
			UploadedFile arquivoUpload = event.getFile();
			Path arquivoTemp = Files.createTempFile(null, null);
			Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
			pessoa.setCaminho(arquivoTemp.toString());
			
			System.out.println("Caminho do arquivo (Foto): " + pessoa.getCaminho());			
			Messages.addGlobalInfo("Upload realizado com sucesso!");
			
			String nome = event.getFile().getFileName();
			String tipo = event.getFile().getContentType();
			long tamanho = event.getFile().getSize();
			
			Messages.addGlobalInfo("Nome: " + nome + "Tipo: " + tipo + "Tamanho: " + tamanho);
			
		} catch (IOException erro) {
			Messages.addGlobalInfo("Ocorreu um erro ao tentar realizar o upload de arquivo");
			erro.printStackTrace();
		}		
	}
}
