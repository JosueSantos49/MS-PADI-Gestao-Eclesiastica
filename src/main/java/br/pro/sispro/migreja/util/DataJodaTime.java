package br.pro.sispro.migreja.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class DataJodaTime {

	public static void main(String[] args) {

		DateTime dataAtual = new DateTime(); // pega data e hora atual
		DateTime dataAniversario = new DateTime(DateTime.parse("1988-09-29T08:00:55Z")); // exemplo

		// pega a duração da diferença dos dois
		Duration dur = new Duration(dataAniversario, dataAtual);

		System.out.println("Duração milisegundos:" + dur.getMillis());
		System.out.println("Duração dias:" + dur.getStandardDays());
		System.out.println("Duração horas:" + dur.getStandardHours());
		System.out.println("Duração segundos:" + dur.getStandardSeconds());
	}
	
}
