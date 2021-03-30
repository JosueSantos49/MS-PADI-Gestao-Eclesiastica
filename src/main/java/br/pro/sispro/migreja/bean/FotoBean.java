package br.pro.sispro.migreja.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.omnifaces.util.Messages;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.pro.sispro.migreja.controller.FotoController;
import br.pro.sispro.migreja.domain.Foto;
import br.pro.sispro.migreja.util.Util;
import br.sispro.migreja.dao.FotoDAO;
import br.sispro.migreja.dao.PessoaDAO;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class FotoBean implements Serializable {

    private Foto foto;
    private List<Foto> fotos;
    private List<Foto> arquivos;
    private boolean abriuWebcam;
    private String arquivo;
    private File fotosalva;
    StreamedContent fotoc;

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public boolean isAbriuWebcam() {
        return abriuWebcam;
    }

    public void setAbriuWebcam(boolean abriuWebcam) {
        this.abriuWebcam = abriuWebcam;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public File getFotosalva() {
        return fotosalva;
    }

    public void setFotosalva(File fotosalva) {
        this.fotosalva = fotosalva;
    }

    public StreamedContent getFotoc() {
        return fotoc;
    }

    public void setFotoc(StreamedContent fotoc) {
        this.fotoc = fotoc;
    }

    public List<Foto> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Foto> arquivos) {
        this.arquivos = arquivos;
    }
    
    @PostConstruct
    public void init() {
    	
    	try {
			FotoDAO fotoDAO = new FotoDAO();
			fotos = fotoDAO.listar();
			
			arquivos = fotoDAO.listar();	
						
		} catch (RuntimeException erro) {
			
			System.out.println("Ocorreu um erro ao tentar listar fotos (init): " + erro);
			Messages.addGlobalError("Ocorreu um erro ao tentar listar fotos!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
			
		}
    }

    public void listarFotos() {
    	
    	fotos = new ArrayList<>();
        String hql = "select vo from foto vo where vo.nome is null order by vo.data_hora desc";
        fotos = new FotoController().listar(hql);
        
    }

    public void listarArquivos() {
    	/*
        arquivos = new ArrayList<>();
        String hql = "select vo from foto vo where vo.nome is not null order by vo.data_hora desc";
        arquivos = new FotoController().listar(hql);
        */
        
        try {
        	
        	FotoDAO fotoDAO = new FotoDAO();
			arquivos = fotoDAO.listar();
						
		} catch (RuntimeException erro) {
			
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a lista de credenciais!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
			
		}
        
    }

    public void abrirJanelaFoto() {
    	 
    	try {
    		
	        abriuWebcam = true;
	        arquivo = "";
	        foto = new Foto();
	                
	        Util.atualizarComponente("dlgfoto");
	        Util.executarJavascript("PF('dlgfoto').show();");
        
    	} catch (RuntimeException erro) {
    		
    		System.out.println("Ocorreu um erro ao tentar gerar uma nova foto (abrir Janela Foto): " + erro);
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova foto!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
    }
    
    public void salvar() {
    
    	try {

			FotoDAO fotoDAO = new FotoDAO();
            fotoDAO.merge(foto);
            
            foto = new Foto();
            fotos = fotoDAO.listar();                        
            arquivos= fotoDAO.listar();
            
			Messages.addGlobalInfo("Credencial gerada com sucesso!");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar a credencial!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
    	
    }

    public void excluir(Foto f) {
        try {
        	
        	FotoDAO fotoDAO = new FotoDAO();
            fotoDAO.excluir(f); 
            
            fotos= fotoDAO.listar();
            
            Util.criarMensagemAviso("foto excluida");
            Util.atualizarComponente("foto");

            abriuWebcam = false;
            
        } catch (Exception erro) {
            Util.criarMensagemErro(erro.toString());
            Messages.addGlobalError("Ocorreu um erro ao tentar excluir a foto!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
        }
    }

    public String gerarNome() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public void tirarFoto(CaptureEvent captureEvent) throws IOException {
    	
        arquivo = gerarNome();
        byte[] data = captureEvent.getData();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String pasta = servletContext.getRealPath("") + "resources" + File.separator + "fotossalvas";
              
        System.out.println("Tirar foto pasta: "+pasta);
        
        File vpasta = new File(pasta);
        
        System.out.println("Tirar foto vpasta: "+vpasta);
        
        if (!vpasta.exists()) {
            vpasta.setWritable(true);
            vpasta.mkdirs();
        }
        
        String novoarquivo = pasta + File.separator + arquivo + ".jpeg";        
        System.out.println("Tirar foto Novo arquivo: "+novoarquivo);
        
        fotosalva = new File(novoarquivo);
        fotosalva.createNewFile();
        FileImageOutputStream imageOutput;
        
        try {
            imageOutput = new FileImageOutputStream(fotosalva);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();

        } catch (IOException e) {
            Util.criarMensagemErro(e.toString());
        }
    }

    public void salvarFoto() {
        
    	try {
    		    		
            byte[] fotobyte = FileUtils.readFileToByteArray(fotosalva);
            
            foto.setNome("foto.jpg");            
            foto.setArquivo(fotobyte);
            foto.setDataHora(Calendar.getInstance().getTime());
            
            FotoDAO fotoDAO = new FotoDAO();
            fotoDAO.merge(foto);
          
            fotos = fotoDAO.listar();            	
            
            Util.executarJavascript("PF('dlgfoto').hide()");
            Util.criarMensagem("Foto salva com sucesso!");
            Util.atualizarComponente("formListagem");//foto
            fotosalva.delete();
            abriuWebcam = false;
          
        } catch (Exception erro) {
            Util.criarMensagemErro(erro.toString());
            System.out.println("Ocorreu um erro ao tentar salvar o arquivo: " + erro);
            Messages.addGlobalError("Ocorreu um erro ao tentar salvar o arquivo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());            
        }
    }

    public void abrirJanelaUpload() {
        foto = new Foto();
        Util.atualizarComponente("dlgfoto");
        Util.executarJavascript("PF('dlgfoto').show();");
    }

    public void enviarArquivo(FileUploadEvent event){ 
    	
        try {
        	
            arquivo = gerarNome();
            
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String pasta = servletContext.getRealPath("")+ File.separator + "resources" + File.separator + "arquivossalvos";
            
            File vpasta = new File(pasta);
            
            if (!vpasta.exists()) {
                vpasta.setWritable(true);
                vpasta.mkdirs();
            }
            String novoarquivo = pasta + File.separator + event.getFile().getFileName();
            
            File a = new File(novoarquivo);
            a.createNewFile();
            FileUtils.copyInputStreamToFile(event.getFile().getInputstream(), a);
            byte[] fotobyte = FileUtils.readFileToByteArray(a);
            
            foto.setNome(event.getFile().getFileName());
            foto.setArquivo(fotobyte);
            foto.setDataHora(Calendar.getInstance().getTime());

            //Inserindo os dados da pessoa no B.D
            FotoDAO fotoDAO = new FotoDAO();
            fotoDAO.merge(foto);
            
            //Atualizando a lista
            foto = new Foto();
            fotos = fotoDAO.listar();
            arquivos= fotoDAO.listar();            
            
            Util.executarJavascript("PF('dlgfoto').hide()");
            Util.criarMensagem("Arquivo salvo");
            Util.atualizarComponente("formListagem");
            a.delete();            

        } catch (Exception erro) {
        	
            Util.criarMensagemErro(erro.toString());
            System.out.println("Ocorreu um erro ao tentar enviar o arquivo: " + erro);
            Messages.addGlobalError("Ocorreu um erro ao tentar enviar o arquivo!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage()); 
			
        }
    }

    public StreamedContent fazerDownload(Foto f) {
        return new DefaultStreamedContent(new ByteArrayInputStream(f.getArquivo()), "application/octet-stream", f.getNome());
    }

    public void excluirArquivo(Foto f) {
        try {
        	FotoDAO fotoDAO = new FotoDAO();
            fotoDAO.excluir(f); 
            
            arquivos= fotoDAO.listar();            
           
            Util.criarMensagemAviso("arquivo excluido");
            Util.atualizarComponente("foto");
            
        } catch (Exception erro) {
            Util.criarMensagemErro(erro.toString());
            Messages.addGlobalError("Ocorreu um erro ao tentar excluir a foto!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
        }
    }
    
    public void editar(ActionEvent evento) {
		try {
			foto = (Foto) evento.getComponent().getAttributes().get("fotoSelecionada");
			
			FotoDAO fotoDAO = new FotoDAO();			
			
			arquivos= fotoDAO.listar(); 
			fotos = fotoDAO.listar();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma foto!");
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}
	}
    


}
