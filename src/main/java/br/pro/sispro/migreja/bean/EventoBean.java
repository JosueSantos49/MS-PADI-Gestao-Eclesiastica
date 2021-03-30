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

import br.pro.sispro.migreja.domain.Departamento;
import br.pro.sispro.migreja.domain.Evento;
import br.pro.sispro.migreja.domain.Membro;
import br.pro.sispro.migreja.util.HibernateUtil;
import br.sispro.migreja.dao.DepartamentoDAO;
import br.sispro.migreja.dao.EventoDAO;
import br.sispro.migreja.dao.MembroDAO;
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
public class EventoBean implements Serializable {

	private Evento eventto;
	private List<Evento> eventtos;

	private List<Departamento> departamentos;
	private List<Membro> membros;	

	public Evento getEventto() {
		return eventto;
	}

	public void setEventto(Evento eventto) {
		this.eventto = eventto;
	}

	public List<Evento> getEventtos() {
		return eventtos;
	}

	public void setEventtos(List<Evento> eventtos) {
		this.eventtos = eventtos;
	}

	public List<Membro> getMembros() {
		return membros;
	}

	public void setMembros(List<Membro> membros) {
		this.membros = membros;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	@PostConstruct
	public void listar() {
		try {
			EventoDAO eventoDAO = new EventoDAO();
			eventtos = eventoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o evento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		try {
			eventto = new Evento();

			DepartamentoDAO departamentoDAO = new DepartamentoDAO();
			departamentos = departamentoDAO.listar();
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo evento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void salvar() {
		try {
			EventoDAO eventoDAO = new EventoDAO();
			eventoDAO.merge(eventto);

			eventto = new Evento();
			eventtos = eventoDAO.listar();

			DepartamentoDAO departamentoDAO = new DepartamentoDAO();
			departamentos = departamentoDAO.listar();
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();			

			Messages.addGlobalInfo("Evento salvo com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar um evento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			eventto = (Evento) evento.getComponent().getAttributes().get("eventtoSelecionado");

			EventoDAO eventoDAO = new EventoDAO();
			eventoDAO.excluir(eventto);

			eventtos = eventoDAO.listar();

			Messages.addGlobalInfo("Nome do evento: " + eventto.getNomeEvento());
			System.out.println();
			Messages.addGlobalInfo("Instituição: " + eventto.getDepartamento().getInstituicao().getRazaosocial());
			System.out.println();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um evento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void editar(ActionEvent evento) {
		try {
			eventto = (Evento) evento.getComponent().getAttributes().get("eventtoSelecionado");

			DepartamentoDAO departamentoDAO = new DepartamentoDAO();
			departamentos = departamentoDAO.listar();
			
			MembroDAO membroDAO = new MembroDAO();
			membros = membroDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um departamento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void visualizar(ActionEvent evento) {
		try {
			eventto = (Evento) evento.getComponent().getAttributes().get("eventtoSelecionado");	
			
			System.out.println("Evento: "+eventto);
									
			EventoDAO eventoDAO = new EventoDAO();
			eventtos = eventoDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o evento!");
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
			
			String caminho = Faces.getRealPath("/reports/evento.jasper");
						
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
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		EventoDAO eventoDAO = new EventoDAO();
		eventtos = eventoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/evento.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.eventtos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Membro.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		EventoDAO eventoDAO = new EventoDAO();
		eventtos = eventoDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/evento.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.eventtos));
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
		EventoDAO eventoDAO = new EventoDAO();
		eventtos = eventoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/evento.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.eventtos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Evento.doc");
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
		EventoDAO eventoDAO = new EventoDAO();
		eventtos = eventoDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/evento.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.eventtos));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=Evento.ppt");
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
