/*	
  	Autor: Josue da Conceicao Santos
	E-mail: conceicaojosue@outlook.com.br
	Ano: 2015
*/
package br.pro.sispro.migreja.util;

public final class Config {       

	private static final String NOME_DRIVER = "com.mysql.jdbc.Driver";	
	private static final String HOST_PORTA = "jdbc:mysql://localhost/uploadFila?autoReconnect=true";
    private static final String NOME_BANCO_DADOS = "";
    private static final String NOME_USUARIO_BANCO = "root";
    private static final String SENHA_USUARIO_BANCO = "josue123";    
    
    public static String getDriver(){
    	return NOME_DRIVER;
    }
    public static String getHost(){
        return HOST_PORTA;
    }
    
    public static String getNomeBanco(){
        return NOME_BANCO_DADOS;
    }
    
    public static String getUsuarioBanco(){
        return NOME_USUARIO_BANCO;
    }
    
    public static String getSenhaBanco(){
        return SENHA_USUARIO_BANCO;
    }       
    
}