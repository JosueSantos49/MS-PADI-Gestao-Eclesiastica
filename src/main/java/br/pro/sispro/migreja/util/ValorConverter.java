package br.pro.sispro.migreja.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Double.class, value = "valorConverter")
public class ValorConverter implements Converter{
	
	public ValorConverter() {}

	 @Override
	    public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
	        return value == null ? null : value.toString();
	    }

	    @Override
	    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
	        if (value == null || value.isEmpty()) return null;
	        Double valor = new Double(value);

	        return valor;
	    }

}
