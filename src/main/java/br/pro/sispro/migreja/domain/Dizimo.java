package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "dizimo")
public class Dizimo extends GenericDomain {

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(nullable = true)
	private Double valor;

	@Column(nullable = true)
	private Double oferta;

	@Column(nullable = true)
	private Long ordem;

	@Column(length = 60, nullable = true)
	private String formaPagamento;

	@Column(length = 100, nullable = true)
	private String membroRespo;

	@ManyToOne
	@JoinColumn(name = "instituicao", nullable = false)
	private Instituicao instituicao = new Instituicao();

	@ManyToOne
	@JoinColumn(name = "membro", nullable = true)
	private Membro membro = new Membro();
	
	@Column(length = 100, nullable = true)
	private String membro1;

	@Column(length = 100, nullable = true)
	private String membro2;

	@Column(length = 100, nullable = true)
	private String membro3;

	@Column(length = 100, nullable = true)
	private String membro4;

	@Column(length = 100, nullable = true)
	private String membro5;

	@Column(length = 100, nullable = true)
	private String membro6;

	@Column(length = 100, nullable = true)
	private String membro7;

	@Column(length = 100, nullable = true)
	private String membro8;

	@Column(length = 100, nullable = true)
	private String membro9;

	@Column(length = 100, nullable = true)
	private String membro10;

	@Column(length = 100, nullable = true)
	private String membro11;

	@Column(length = 100, nullable = true)
	private String membro12;

	@Column(length = 100, nullable = true)
	private String membro13;

	@Column(length = 100, nullable = true)
	private String membro14;

	@Column(length = 100, nullable = true)
	private String membro15;

	@Column(length = 100, nullable = true)
	private String membro16;

	@Column(length = 100, nullable = true)
	private String membro17;

	@Column(length = 100, nullable = true)
	private String membro18;

	@Column(length = 100, nullable = true)
	private String membro19;

	@Column(length = 100, nullable = true)
	private String membro20;

	// Valores Dízimos

	@Column(nullable = true)
	private Double membro0v;

	@Column(nullable = true)
	private Double membro1v;

	@Column(nullable = true)
	private Double membro2v;

	@Column(nullable = true)
	private Double membro3v;

	@Column(nullable = true)
	private Double membro4v;

	@Column(nullable = true)
	private Double membro5v;

	@Column(nullable = true)
	private Double membro6v;

	@Column(nullable = true)
	private Double membro7v;

	@Column(nullable = true)
	private Double membro8v;

	@Column(nullable = true)
	private Double membro9v;

	@Column(nullable = true)
	private Double membro10v;

	@Column(nullable = true)
	private Double membro11v;

	@Column(nullable = true)
	private Double membro12v;

	@Column(nullable = true)
	private Double membro13v;

	@Column(nullable = true)
	private Double membro14v;

	@Column(nullable = true)
	private Double membro15v;

	@Column(nullable = true)
	private Double membro16v;

	@Column(nullable = true)
	private Double membro17v;

	@Column(nullable = true)
	private Double membro18v;

	@Column(nullable = true)
	private Double membro19v;

	@Column(nullable = true)
	private Double membro20v;

	@Column(nullable = true)
	private Double total;

	@Column(nullable = true)
	private Double outros;
	
	@Column(length = 150, nullable = true)
	private String descricao;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getOferta() {
		return oferta;
	}

	public void setOferta(Double oferta) {
		this.oferta = oferta;
	}

	public Double getOutros() {
		return outros;
	}

	public void setOutros(Double outros) {
		this.outros = outros;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}

	public void setMembro1(String membro1) {
		this.membro1 = membro1;
	}

	public void setMembro2(String membro2) {
		this.membro2 = membro2;
	}

	public void setMembro3(String membro3) {
		this.membro3 = membro3;
	}

	public void setMembro4(String membro4) {
		this.membro4 = membro4;
	}

	public void setMembro5(String membro5) {
		this.membro5 = membro5;
	}

	public void setMembro6(String membro6) {
		this.membro6 = membro6;
	}

	public void setMembro7(String membro7) {
		this.membro7 = membro7;
	}

	public void setMembro8(String membro8) {
		this.membro8 = membro8;
	}

	public void setMembro9(String membro9) {
		this.membro9 = membro9;
	}

	public void setMembro10(String membro10) {
		this.membro10 = membro10;
	}

	public void setMembro11(String membro11) {
		this.membro11 = membro11;
	}

	public void setMembro12(String membro12) {
		this.membro12 = membro12;
	}

	public void setMembro13(String membro13) {
		this.membro13 = membro13;
	}

	public void setMembro14(String membro14) {
		this.membro14 = membro14;
	}

	public void setMembro15(String membro15) {
		this.membro15 = membro15;
	}

	public void setMembro16(String membro16) {
		this.membro16 = membro16;
	}

	public void setMembro17(String membro17) {
		this.membro17 = membro17;
	}

	public void setMembro18(String membro18) {
		this.membro18 = membro18;
	}

	public void setMembro19(String membro19) {
		this.membro19 = membro19;
	}

	public void setMembro20(String membro20) {
		this.membro20 = membro20;
	}

	
	public Double getMembro0v() {
		return membro0v;
	}

	public void setMembro0v(Double membro0v) {
		this.membro0v = membro0v;
	}

	public Double getMembro1v() {
		return membro1v;
	}

	public void setMembro1v(Double membro1v) {
		this.membro1v = membro1v;
	}

	public Double getMembro2v() {
		return membro2v;
	}

	public void setMembro2v(Double membro2v) {
		this.membro2v = membro2v;
	}

	public Double getMembro3v() {
		return membro3v;
	}

	public void setMembro3v(Double membro3v) {
		this.membro3v = membro3v;
	}

	public Double getMembro4v() {
		return membro4v;
	}

	public void setMembro4v(Double membro4v) {
		this.membro4v = membro4v;
	}

	public Double getMembro5v() {
		return membro5v;
	}

	public void setMembro5v(Double membro5v) {
		this.membro5v = membro5v;
	}

	public Double getMembro6v() {
		return membro6v;
	}

	public void setMembro6v(Double membro6v) {
		this.membro6v = membro6v;
	}

	public Double getMembro7v() {
		return membro7v;
	}

	public void setMembro7v(Double membro7v) {
		this.membro7v = membro7v;
	}

	public Double getMembro8v() {
		return membro8v;
	}

	public void setMembro8v(Double membro8v) {
		this.membro8v = membro8v;
	}

	public Double getMembro9v() {
		return membro9v;
	}

	public void setMembro9v(Double membro9v) {
		this.membro9v = membro9v;
	}

	public Double getMembro10v() {
		return membro10v;
	}

	public void setMembro10v(Double membro10v) {
		this.membro10v = membro10v;
	}

	public Double getMembro11v() {
		return membro11v;
	}

	public void setMembro11v(Double membro11v) {
		this.membro11v = membro11v;
	}

	public Double getMembro12v() {
		return membro12v;
	}

	public void setMembro12v(Double membro12v) {
		this.membro12v = membro12v;
	}

	public Double getMembro13v() {
		return membro13v;
	}

	public void setMembro13v(Double membro13v) {
		this.membro13v = membro13v;
	}

	public Double getMembro14v() {
		return membro14v;
	}

	public void setMembro14v(Double membro14v) {
		this.membro14v = membro14v;
	}

	public Double getMembro15v() {
		return membro15v;
	}

	public void setMembro15v(Double membro15v) {
		this.membro15v = membro15v;
	}

	public Double getMembro16v() {
		return membro16v;
	}

	public void setMembro16v(Double membro16v) {
		this.membro16v = membro16v;
	}

	public Double getMembro17v() {
		return membro17v;
	}

	public void setMembro17v(Double membro17v) {
		this.membro17v = membro17v;
	}

	public Double getMembro18v() {
		return membro18v;
	}

	public void setMembro18v(Double membro18v) {
		this.membro18v = membro18v;
	}

	public Double getMembro19v() {
		return membro19v;
	}

	public void setMembro19v(Double membro19v) {
		this.membro19v = membro19v;
	}

	public Double getMembro20v() {
		return membro20v;
	}

	public void setMembro20v(Double membro20v) {
		this.membro20v = membro20v;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public String getMembroRespo() {
		return membroRespo;
	}

	public void setMembroRespo(String membroRespo) {
		this.membroRespo = membroRespo;
	}

	public String getMembro1() {
		return membro1;
	}

	public String getMembro2() {
		return membro2;
	}

	public String getMembro3() {
		return membro3;
	}

	public String getMembro4() {
		return membro4;
	}

	public String getMembro5() {
		return membro5;
	}

	public String getMembro6() {
		return membro6;
	}

	public String getMembro7() {
		return membro7;
	}

	public String getMembro8() {
		return membro8;
	}

	public String getMembro9() {
		return membro9;
	}

	public String getMembro10() {
		return membro10;
	}

	public String getMembro11() {
		return membro11;
	}

	public String getMembro12() {
		return membro12;
	}

	public String getMembro13() {
		return membro13;
	}

	public String getMembro14() {
		return membro14;
	}

	public String getMembro15() {
		return membro15;
	}

	public String getMembro16() {
		return membro16;
	}

	public String getMembro17() {
		return membro17;
	}

	public String getMembro18() {
		return membro18;
	}

	public String getMembro19() {
		return membro19;
	}

	public String getMembro20() {
		return membro20;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
