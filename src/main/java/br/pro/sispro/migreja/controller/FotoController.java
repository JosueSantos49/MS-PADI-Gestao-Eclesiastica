package br.pro.sispro.migreja.controller;

import br.pro.sispro.migreja.domain.Foto;
import br.sispro.migreja.dao.HibernateDAO;

import java.util.List;
import br.pro.sispro.migreja.util.Util;

public class FotoController {

    @SuppressWarnings("rawtypes")
	private final HibernateDAO dao;

    @SuppressWarnings("rawtypes")
	public FotoController() {
        this.dao = new HibernateDAO(Util.pegarSessao());
    }

    @SuppressWarnings("unchecked")
	public boolean salvar(Foto foto) {
        try {
            this.dao.salvar(foto);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	public boolean excluir(Foto foto) {
        try {
            this.dao.excluir(foto);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
	public List<Foto> listar(String hql) {
        return this.dao.listar(hql);
    }

    public Foto pegar(String hql) {
        return (Foto) this.dao.pegar(hql);
    }

}
