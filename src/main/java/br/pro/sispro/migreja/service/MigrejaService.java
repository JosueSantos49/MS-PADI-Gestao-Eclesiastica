package br.pro.sispro.migreja.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

//http://localhost:8080/Migreja/rest/migreja
@Path("migreja")
public class MigrejaService {
	@GET
	public String exibir(){
		return "MS-PADI";
	}
}
