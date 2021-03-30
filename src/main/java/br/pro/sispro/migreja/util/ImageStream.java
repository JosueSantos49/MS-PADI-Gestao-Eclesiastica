
package br.pro.sispro.migreja.util;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.pro.sispro.migreja.controller.FotoController;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class ImageStream implements Serializable { 

    public StreamedContent getFoto() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
           
            return new DefaultStreamedContent();
            
        } else {
            String ids = context.getExternalContext().getRequestParameterMap().get("id");
            Long id = Long.valueOf(ids);
            
            System.out.println("Foto Gerada: " + id);
            
            //Selecione a foto cujo o id é igual ao id do B.D
            String hql = "select vo from Foto vo where vo.id=" + id + "";
            System.out.println("Query Foto Gerada: " + hql);
            
            StreamedContent i = new DefaultStreamedContent(
            new ByteArrayInputStream(new FotoController().pegar(hql).getArquivo()));
            return i;
        }
    }

   
}
