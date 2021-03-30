package br.pro.sispro.migreja.util;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean
public class ConverterBooleanString implements Converter {

	   public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	      try {
	         if (((Boolean)arg2) == true) {
	            return "Sim";
	         } else
	            return "Não";
	      } catch (Exception e) {
	         throw new ConverterException (e);
	      }
	   }

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		 try {
	         if (arg2.equalsIgnoreCase("sim")) {
	            return new Boolean(true);
	         } else
	            return new Boolean(false);
	      } catch (Exception e) {
	            throw new ConverterException (e);
	      }
	}
}
