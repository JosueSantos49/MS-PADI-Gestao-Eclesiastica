
package br.pro.sispro.migreja.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name="foto")
public class Foto extends GenericDomain{
        
    @Column(length = 80, nullable = true)
    private String nome;
    
    @Lob
	@Column(nullable = true)
    private byte[] arquivo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora",nullable = true)
    private Date dataHora;    
        
    @Column(length = 150, nullable = true)    
    private String local;
    
    @Column(length = 150, nullable = true)
    private String nomemembro;
    
    @Column(length = 150, nullable = true)
    private String posicao;
    	
    @Column(length = 20, nullable = true)
    private String dtbatismo;
    
    @Column(length = 30, nullable = true)
    private String dtordenacao;
    
    @Column(length = 30, nullable = true)
    private String dtemissao;
    
    @Column(length = 30, nullable = true)
    private String dtnascimento;
    
    @Column(length = 150, nullable = true)
    private String civil;
        
    @Column(length = 200, nullable = true)
    private String filiacao;
    
    @Column(length = 200, nullable = true)
    private String endereco;    
    
    @Column(length = 20, nullable = false)
    private String rg;
    
    @Column(length = 20, nullable = false)
    private String cpf;
    
    @Column(length = 20, nullable = false)
    private String cnpj;

    public Foto() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }     
    
    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }  
    

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getNomemembro() {
		return nomemembro;
	}

	public void setNomemembro(String nomemembro) {
		this.nomemembro = nomemembro;
	}

	public String getPosicao() {
		return posicao;
	}

	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}

	public String getDtbatismo() {
		return dtbatismo;
	}

	public void setDtbatismo(String dtbatismo) {
		this.dtbatismo = dtbatismo;
	}

	public String getDtordenacao() {
		return dtordenacao;
	}

	public void setDtordenacao(String dtordenacao) {
		this.dtordenacao = dtordenacao;
	}

	public String getDtemissao() {
		return dtemissao;
	}

	public void setDtemissao(String dtemissao) {
		this.dtemissao = dtemissao;
	}

	public String getDtnascimento() {
		return dtnascimento;
	}

	public void setDtnascimento(String dtnascimento) {
		this.dtnascimento = dtnascimento;
	}

	public String getCivil() {
		return civil;
	}

	public void setCivil(String civil) {
		this.civil = civil;
	}

	public String getFiliacao() {
		return filiacao;
	}

	public void setFiliacao(String filiacao) {
		this.filiacao = filiacao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}	

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }	

    @SuppressWarnings("unused")
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Foto other = (Foto) obj;
        return true;
    }    
    
}
