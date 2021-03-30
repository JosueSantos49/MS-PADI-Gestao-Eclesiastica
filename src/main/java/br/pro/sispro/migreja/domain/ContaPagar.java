package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*Saida: salvar contas a pagar na tabela contarpagar, e registrar o pagamento na tabela controle caixa.*/

@SuppressWarnings("serial")
@Entity
@Table(name = "contapagar")
public class ContaPagar extends GenericDomain{

	@Column(nullable = false)
	private Long codDoc; 
	
	@Column(length = 200, nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@Column(nullable = true)
	private Double valorPago;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataPgto;
	
	@Column(length = 200, nullable = false)
	private String fornecedor;
	
	@Column(length = 18, nullable = false)
	private String cnpj;
	
	@Column(nullable = true)
	private Long parcela;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Column(nullable = true)
	private Double valorParcela;
	
	@Column(length = 200, nullable = true)
	private String pagamento;

	@ManyToOne
	@JoinColumn(name="instituicao",nullable = false)
	private Instituicao instituicao = new Instituicao();
	
	/*@ManyToOne
	@JoinColumn(name="controlecaixa",nullable = false)
	private ControleCaixa controleCaixa = new ControleCaixa();*/

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

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Date getDataPgto() {
		return dataPgto;
	}

	public void setDataPgto(Date dataPgto) {
		this.dataPgto = dataPgto;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}


	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Long getParcela() {
		return parcela;
	}

	public void setParcela(Long parcela) {
		this.parcela = parcela;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getPagamento() {
		return pagamento;
	}

	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}
	
}
