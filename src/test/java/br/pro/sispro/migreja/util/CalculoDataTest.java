package br.pro.sispro.migreja.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
//importar na classe
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalculoDataTest {

	// metodo que retorna o intervalo de dias entre duas datas
	public static String contaDias(String dataInicialBR, String dataFinalBR) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		Date dataInicio = df.parse(dataInicialBR);
		Date dataFim = df.parse(dataFinalBR);
		long dt = (dataFim.getTime() - dataInicio.getTime()) + 3600000;
		Long diasCorridosAnoLong = (dt / 86400000L);
		Integer diasDecorridosInt = Integer.valueOf(diasCorridosAnoLong.toString());
		String diasDecorridos = String.valueOf(diasDecorridosInt); // Sem numeros formatados;
		return diasDecorridos;
	}

	// método para pegar a data do dia
	@SuppressWarnings("unused")
	public static String getDataDiaBr() {
		GregorianCalendar calendario = new GregorianCalendar();
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		int mes = calendario.get(Calendar.MONTH) + 1;
		int ano = calendario.get(Calendar.YEAR);
		String dataIguana = String.valueOf(dia + "/" + mes + "/" + ano);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String diaIguana = df.format(new Date());
		return diaIguana;
	}

	// Metodo para calcular a idade
	public static BigDecimal calculaIdade(String dataDoMeuNascimento) throws ParseException {
		BigDecimal qtdDias = new BigDecimal(contaDias(dataDoMeuNascimento, getDataDiaBr()));
		BigDecimal ano = new BigDecimal(365.25);
		BigDecimal idade = qtdDias.divide(ano, 0, RoundingMode.DOWN);
		return idade;
	}
	// **** para usar é só chamar o metodo calculaIdade e passar uma String com  a data do nascimento
	public static void main(String[] args) throws ParseException {
		System.out.println("Data do dia: " + getDataDiaBr());
		System.out.println("Intervalo de dias entre duas idades: " + contaDias("29/09/1988", "12/08/2016"));
		System.out.println("Idade:" + calculaIdade("29/09/1988"));
		
		System.out.println();
	}
}
