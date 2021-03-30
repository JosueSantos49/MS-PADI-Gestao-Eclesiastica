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

import org.omnifaces.util.Messages;

import br.pro.sispro.migreja.domain.ControleCaixa;
import br.sispro.migreja.dao.ControleCaixaDAO;
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
public class ControleCaixaBean implements Serializable {

	private ControleCaixa controleCaixa;
	private List<ControleCaixa> controleCaixas;	
	
	public ControleCaixa getControleCaixa() {
		return controleCaixa;
	}

	public void setControleCaixa(ControleCaixa controleCaixa) {
		this.controleCaixa = controleCaixa;
	}

	public List<ControleCaixa> getControleCaixas() {
		return controleCaixas;
	}

	public void setControleCaixas(List<ControleCaixa> controleCaixas) {
		this.controleCaixas = controleCaixas;
	}

	@PostConstruct
	public void listar() {
		try {
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixas = controleCaixaDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar o controle de caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		try {
			controleCaixa = new ControleCaixa();			
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo controle de caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void salvar() {
		try {
			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixaDAO.editar(controleCaixa);

			controleCaixa = new ControleCaixa();
			controleCaixas = controleCaixaDAO.listar();

			Messages.addGlobalInfo("Registro de Pagamento salvo com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar uma registro de pagamento!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	//http://respostas.guj.com.br/24582-soma-de-dados-da-coluna-primefaces
	public void somarEntrada(){
		Double total = new Double(0);
		
		for(int posicao = 0; posicao < controleCaixas.size(); posicao++){
				total += controleCaixa.getValorTotal();
				System.out.println("Soma das entradas: "+total);
		}
	}
	
	public void excluir(ActionEvent evento) {
		try {
			controleCaixa = (ControleCaixa) evento.getComponent().getAttributes().get("controleSelecionado");

			ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
			controleCaixaDAO.excluir(controleCaixa);

			controleCaixas = controleCaixaDAO.listar();

			Messages.addGlobalInfo("Controle do caixa excluído!");
			System.out.println();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar remover um controle do caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void editar(ActionEvent evento) {
		try {
			controleCaixa = (ControleCaixa) evento.getComponent().getAttributes().get("controleSelecionado");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar o controle do caixa!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	/*
	public void registrarPagamento(ActionEvent evento){
	
		try{
			contaPagar = (ContaPagar) evento.getComponent().getAttributes().get("contaSelecionada");
			System.out.println(contaPagar);		
			
			InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
			instituicaos = instituicaoDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar a conta a pagar, para registro!");
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
	*/
	
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException{
		ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
		controleCaixas = controleCaixaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/controleCaixa.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.controleCaixas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ControleCaixa.pdf");
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();
		
		FacesContext.getCurrentInstance().responseComplete();			
	}
	
	public void verPDF(ActionEvent actionEvent) throws Exception{
		ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
		controleCaixas = controleCaixaDAO.listar();
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/controleCaixa.jasper"));		
		
		Map<String, Object> parametros = new HashMap<>();		
		
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.controleCaixas));
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
		ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
		controleCaixas = controleCaixaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/controleCaixa.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.controleCaixas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ControleCaixa.doc");
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
		ControleCaixaDAO controleCaixaDAO = new ControleCaixaDAO();
		controleCaixas = controleCaixaDAO.listar();
				
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/controleCaixa.jasper"));
		
		Map<String, Object> parametros = new HashMap<>();
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametros, new JRBeanCollectionDataSource(this.controleCaixas));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename=ControleCaixa.ppt");
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
