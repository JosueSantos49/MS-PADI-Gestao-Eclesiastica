package br.pro.sispro.migreja.util;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class MigrejaResourceConfig extends ResourceConfig {
	public MigrejaResourceConfig() {
		packages("br.pro.sispro.migreja.service");
	}
}
