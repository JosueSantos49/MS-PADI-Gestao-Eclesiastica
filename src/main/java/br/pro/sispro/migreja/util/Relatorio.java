package br.pro.sispro.migreja.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Relatorio <Pessoa>{
	
	private HttpServletResponse response;
	private FacesContext context;
	
	public Relatorio() {
		this.context = FacesContext.getCurrentInstance();
		this.response = (HttpServletResponse) this.context.getExternalContext().getResponse();
	}	
	
	public void getRelatorio(List<Pessoa> lista){
		InputStream stream = this.getClass().getResourceAsStream("/reports/pessoa.jasper");
		Map<String, Object> parametros = new HashMap<String, Object>();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try{
			
			JasperReport jasper = (JasperReport) JRLoader.loadObject(stream);			
			JasperPrint print = JasperFillManager.fillReport(jasper, parametros, new JRBeanCollectionDataSource(lista));			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();			
			response.setContentType("application/pdf");			
			response.setContentLength(baos.size());			
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");			
			response.getOutputStream().write(baos.toByteArray());			
			response.getOutputStream().flush();			
			response.getOutputStream().close();			
			context.responseComplete();			
						
		}catch(JRException e){
			Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE,null,e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
			e.printStackTrace();
		}catch (IOException e) {
			Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE,null,e);
		}
		
	}

}
