package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*Saida: salvar contas a pagar na tabela contarpagar, e registrar o pagamento na tabela controle caixa.*/

@SuppressWarnings("serial")
@Entity
@Table(name = "controlecaixa")
public class ControleCaixa extends GenericDomain{

	@Column(nullable = true)
	private Long codDoc;
	
	@Column(length = 200, nullable = true)
	private String descricao;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(nullable = true)
	private Double valorTotal;
	
	@Column(nullable = true)
	private Double valorPago;		
		
	@Column(nullable = true)
	private Double entradaDia;
	
	@Column(nullable = true)
	private Double saidaDia;
	
	@Column(nullable = true)
	private Double saldoMes;
	
	@Column(nullable = true)
	private Double saldoCaixa;

	public Long getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(Long codDoc) {
		this.codDoc = codDoc;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Double getEntradaDia() {
		return entradaDia;
	}

	public void setEntradaDia(Double entradaDia) {
		this.entradaDia = entradaDia;
	}

	public Double getSaidaDia() {
		return saidaDia;
	}

	public void setSaidaDia(Double saidaDia) {
		this.saidaDia = saidaDia;
	}

	public Double getSaldoMes() {
		return saldoMes;
	}

	public void setSaldoMes(Double saldoMes) {
		this.saldoMes = saldoMes;
	}

	public Double getSaldoCaixa() {
		return saldoCaixa;
	}

	public void setSaldoCaixa(Double saldoCaixa) {
		this.saldoCaixa = saldoCaixa;
	}
	
	
}
