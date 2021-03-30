package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
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

import br.pro.sispro.migreja.domain.ItemVenda;
import br.pro.sispro.migreja.domain.Produto;
import br.pro.sispro.migreja.domain.Venda;
import br.pro.sispro.migreja.util.Conexao;
import br.pro.sispro.migreja.util.FuncaoGlobal;
import br.sispro.migreja.dao.ItemVendaDAO;
import br.sispro.migreja.dao.TemporariaDAO;
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
public class ItemVendaBean implements Serializable {

	private ItemVenda itemVenda;
	private List<ItemVenda> itemVendas;
	private List<Produto> produtos;	
	private List<Venda> vendas;
	
	FuncaoGlobal funcaoGlobal = new FuncaoGlobal();

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}	

	public List<ItemVenda> getItemVendas() {
		return itemVendas;
	}

	public void setItemVendas(List<ItemVenda> itemVendas) {
		this.itemVendas = itemVendas;
	}

	@PostConstruct
	public void listar() {
		try {
			
			ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
			itemVendas = itemVendaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o item!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//################ REMOVENDO ITEM DO CONTROLE DO CAIXA ##############################
	public void excluir(ActionEvent evento) throws Exception {
		
		//################### ABRIR CONEXÃO COM O BANCO #########################
		Connection con = Conexao.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			itemVenda = (ItemVenda) evento.getComponent().getAttributes().get("itemVendaSelecionado");

			ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
			boolean b = itemVendaDAO.excluirItem(itemVenda);
			Long codigoRemovido= itemVenda.getCodigo();//Recupera o ID do Registro removido
			
			if(b != false){
				
				try {
					
					itemVendaDAO.excluirItemControleCaixa(itemVenda);
					
					if(codigoRemovido != null) {
						
						//################### INSERIR NA TABELA TEMPORÁRIA #######################						
						try {
												
							con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
							
							//Salvar os dados na tabela temporária		
							ps = con.prepareStatement("INSERT INTO temporaria (sessao, ip, acao, nomeTabela, codigoRegistro, data) VALUES (?,?,?,?,?,?)");
																	
							ps.setString(1, funcaoGlobal.pegarUsuarioSessao());					
							ps.setString(2, funcaoGlobal.pegaIpRede());				
							ps.setString(3, "remover");
							ps.setString(4, "itemvenda");
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
						
						Messages.addGlobalInfo("Registro excluído do controle de caixa com sucesso!");
						
					}					
										
				} catch (Exception e) {
					Messages.addGlobalError("Ocorreu um erro ao tentar remover o item do controle do caixa!");
					e.printStackTrace();
					Messages.addGlobalError(e.getMessage());
				}				
			}

			itemVendas = itemVendaDAO.listar();

			Messages.addGlobalInfo("Excluído com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar extornar a venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		
		try {
			
			itemVenda = (ItemVenda) evento.getComponent().getAttributes().get("itemVendaSelecionado");	
			
			System.out.println("Item venda: " + itemVenda);
					
			ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
			itemVendas = itemVendaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o item da venda!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
		itemVendas = itemVendaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/itemVenda.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.itemVendas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ItemVenda.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
		itemVendas = itemVendaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/itemVenda.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.itemVendas));
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
		ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
		itemVendas = itemVendaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/itemVenda.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.itemVendas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ItemVenda.doc");
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
		ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
		itemVendas = itemVendaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/itemVenda.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.itemVendas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ItemVenda.ppt");
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
