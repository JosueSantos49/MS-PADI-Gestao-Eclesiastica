package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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

import br.pro.sispro.migreja.domain.Estado;
import br.sispro.migreja.dao.EstadoDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings({ "serial", "deprecation" })
@ManagedBean
@ViewScoped
public class EstadoBean implements Serializable {

	private Estado estado;
	private List<Estado> estados;

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	@PostConstruct
	public void listar() {
		try {
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o estado!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		estado = new Estado();
	}

	public void salvar() {
		try {
			EstadoDAO estadoDAO = new EstadoDAO();
			estadoDAO.merge(estado);

			estado = new Estado();
			estados = estadoDAO.listar();

			Messages.addGlobalInfo("Estado salvo com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o estado!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			estado = (Estado) evento.getComponent().getAttributes().get("estadoSelecionado");

			EstadoDAO estadoDAO = new EstadoDAO();
			estadoDAO.excluir(estado);

			estados = estadoDAO.listar();

			Messages.addGlobalInfo("Nome: " + estado.getNome() + "Sigla: " + estado.getSigla());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o estado!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void editar(ActionEvent evento) {
		estado = (Estado) evento.getComponent().getAttributes().get("estadoSelecionado");
	}
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		EstadoDAO estadoDAO = new EstadoDAO();
		estados = estadoDAO.listar();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String sigla = (String) filtros.get("sigla");
		String nome = (String) filtros.get("nome");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/estado.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sigla", "%"+sigla+"%");
		parametros.put("nome", "%"+nome+"%");			
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.estados));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=jsfReporte.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		EstadoDAO estadoDAO = new EstadoDAO();
		estados = estadoDAO.listar();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String sigla = (String) filtros.get("sigla");
		String nome = (String) filtros.get("nome");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/estado.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sigla", "%"+sigla+"%");
		parametros.put("nome", "%"+nome+"%");
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.estados));
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
		EstadoDAO estadoDAO = new EstadoDAO();
		estados = estadoDAO.listar();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String sigla = (String) filtros.get("sigla");
		String nome = (String) filtros.get("nome");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/estado.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sigla", "%"+sigla+"%");
		parametros.put("nome", "%"+nome+"%");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.estados));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=jsfReporte.xls");
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
		EstadoDAO estadoDAO = new EstadoDAO();
		estados = estadoDAO.listar();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String sigla = (String) filtros.get("sigla");
		String nome = (String) filtros.get("nome");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/estado.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sigla", "%"+sigla+"%");
		parametros.put("nome", "%"+nome+"%");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.estados));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=jsfReporte.doc");
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
		EstadoDAO estadoDAO = new EstadoDAO();
		estados = estadoDAO.listar();
		
		DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
		Map<String, Object> filtros = tabela.getFilters();

		String sigla = (String) filtros.get("sigla");
		String nome = (String) filtros.get("nome");
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/estado.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		parametros.put("sigla", "%"+sigla+"%");
		parametros.put("nome", "%"+nome+"%");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.estados));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=jsfReporte.ppt");
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
