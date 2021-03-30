package br.pro.sispro.migreja.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
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

import br.pro.sispro.migreja.domain.EventoCalendario;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.EventoCalendarioDAO;
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
public class EventoCalendarioBean2 implements Serializable {

	private EventoCalendario eventoCalendario;
	private List<EventoCalendario> eventoCalendarios;

	public EventoCalendario getEventoCalendario() {
		return eventoCalendario;
	}

	public void setEventoCalendario(EventoCalendario eventoCalendario) {
		this.eventoCalendario = eventoCalendario;
	}

	public List<EventoCalendario> getEventoCalendarios() {
		return eventoCalendarios;
	}

	public void setEventoCalendarios(List<EventoCalendario> eventoCalendarios) {
		this.eventoCalendarios = eventoCalendarios;
	}

	@PostConstruct
	public void listar() {
		try {
			EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
			eventoCalendarios = eventoCalendarioDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o evento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void novo() {
		eventoCalendario = new EventoCalendario();
	}

	public void excluir(ActionEvent evento) {
		try {
			eventoCalendario = (EventoCalendario) evento.getComponent().getAttributes()
					.get("eventoCalendarioSelecionado");

			EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
			eventoCalendarioDAO.excluir(eventoCalendario);

			eventoCalendarios = eventoCalendarioDAO.listar();

			Messages.addGlobalInfo("Evento excluído com sucesso!");
			Messages.addGlobalInfo("Título: " + eventoCalendario.getTitulo());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover o estado!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void imprimir() {

		try {
			// Navegar na arvore da aplicacao
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();

			String tituloEvento = (String) filtros.get("titulo");
			String inicioEvento = (String) filtros.get("inicio");

			String caminho = Faces.getRealPath("/reports/eventoCalendario.jasper");

			Map<String, Object> parametros = new HashMap<>();

			if (tituloEvento == null) {
				parametros.put("titulo", "%%");
			} else {
				parametros.put("titulo", "%" + tituloEvento + "%");
			}
			if (inicioEvento == null) {
				parametros.put("inicio", "%%");
			} else {
				parametros.put("inicio", "%" + inicioEvento + "%");
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
		EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
		eventoCalendarios = eventoCalendarioDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/eventoCalendario.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.eventoCalendarios));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=EventoCalendario.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
		eventoCalendarios = eventoCalendarioDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/eventoCalendario.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.eventoCalendarios));
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
		EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
		eventoCalendarios = eventoCalendarioDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/eventoCalendario.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.eventoCalendarios));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=EventoCalendario.doc");
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
		EventoCalendarioDAO eventoCalendarioDAO = new EventoCalendarioDAO();
		eventoCalendarios = eventoCalendarioDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/eventoCalendario.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.eventoCalendarios));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=EventoCalendario.ppt");
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
