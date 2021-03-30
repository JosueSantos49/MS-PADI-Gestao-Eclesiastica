
package br.pro.sispro.migreja.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class Util implements Serializable {

    public static Session pegarSessao() {
        try {
        	 return HibernateUtil.getFabricaDeSessoes().getCurrentSession();        	
        } catch (Exception e) {
            System.out.println("Erro(HibernateUtil): " + e);
        }
        return null;
    }

    public static void executarJavascript(String comando) {
        RequestContext.getCurrentInstance().execute(comando);
    }

    public static void resetarComponente(String codigo) {
        RequestContext.getCurrentInstance().reset(codigo);
    }

    public static void atualizarComponente(String codigo) {
        RequestContext.getCurrentInstance().update(codigo);
    }

    public static void criarMensagemErro(String texto) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }

    public static void criarMensagemAviso(String texto) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, texto, texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }

    public static void criarMensagem(String texto) {
        FacesMessage msg = new FacesMessage(texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }

    private String caminho;
    private String arquivo;

    public void gerarRelatorio(String jasper, Long codigo) {
        System.out.println("Codigo Gerar Relatório: "+codigo);
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ServletContext servletContext = (ServletContext) context.getContext();
        
        String arquivoJasper = "/reports/" + jasper + ".jasper";
        
        try {

            Map<String, Object> param = new HashMap<>();
            param.put("codigo", codigo);
            
            this.arquivo = gerarMostrar(request, arquivoJasper, servletContext, HibernateUtil.getConexao(), jasper, param);
            Util.atualizarComponente("relatorio");
            Util.executarJavascript("PF('dlgrelatorio').show();");

        } catch (IOException erro) {
            Util.criarMensagemErro(erro.getMessage());
            Messages.addGlobalError("Ocorreu um erro ao tentar gerar a carteira!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
        }

    }
    
    public void gerarRelatorioTodos(String jasper) {
    	
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ServletContext servletContext = (ServletContext) context.getContext();
        
        String arquivoJasper = "/reports/" + jasper + ".jasper";
        
        try {

            Map<String, Object> param = new HashMap<>();
            
            this.arquivo = gerarMostrar(request, arquivoJasper, servletContext, HibernateUtil.getConexao(), jasper, param);
            Util.atualizarComponente("relatorio");
            Util.executarJavascript("PF('dlgrelatorio').show();");

        } catch (IOException erro) {
            Util.criarMensagemErro(erro.getMessage());
            Messages.addGlobalError("Ocorreu um erro ao tentar gerar a carteira!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
        }

    }

    public String gerarMostrar(HttpServletRequest request,
            String arquivoJasper,
            ServletContext contexto,
            Connection conexao,
            String nomeArquivo,
            Map<String, Object> param) throws IOException {

        FileInputStream reportFile = new FileInputStream(contexto.getRealPath(arquivoJasper));

        byte[] bytes;

        try {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            
            String absoluteDiskPath = servletContext.getRealPath("resources/fotossalvas");
            System.out.println("Fotos Salvas: "+absoluteDiskPath);

            Locale l = new Locale("pt", "BR");
            param.put(JRParameter.REPORT_LOCALE, l);

            bytes = JasperRunManager.runReportToPdf(reportFile, param, conexao);
            
            String nome = String.valueOf(new Date().getTime());            
            System.out.println("Nome Gerar Mostrar: "+nome);            
            
            File a = new File(absoluteDiskPath + File.separator + nome + ".pdf");
            
            System.out.println("File: "+a);
            
            this.caminho = a.getAbsolutePath();
            FileUtils.writeByteArrayToFile(a, bytes);

            return nome;

        } catch (JRException e) {
            System.out.println(e);
            Util.criarMensagemErro(e.getMessage());
        } finally {
            try {
                HibernateUtil.getConexao().close();
            } catch (SQLException ex) {
                Util.criarMensagemErro(ex.getMessage());
            }
        }
        return null;

    }

    /*public static Connection pegarConexao() throws SQLException {
        Connection conexao = null;
        conexao = HibernateUtil.getConexao();
        return conexao;
    }*/
    
    public static Connection pegarConexao() throws SQLException, ClassNotFoundException {
        Connection conexao = null;
        conexao = HibernateUtil.getConexao();
        return conexao;
    }
    
    /*
      public static Connection pegarConexao() {
        Connection conexao = null;
        try {
        	             
            Class.forName("com.mysql.jdbc.Driver");

            conexao = DriverManager.getConnection("jdbc:mysql://"
                    + "localhost"
                    + "/Migreja?charSet=UTF-8",
                    "root",
                    "josue123");
             
        } catch (SQLException ex) {
            Util.criarMensagemErro(ex.getMessage());
            System.out.println("Erro ao abrir conexão: "+ ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException PegaConexao: "+ex);
        }
        return conexao;
    }
     */

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

}
