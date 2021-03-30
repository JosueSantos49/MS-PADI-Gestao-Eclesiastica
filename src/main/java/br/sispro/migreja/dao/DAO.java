
package br.sispro.migreja.dao;

import java.util.List;

public interface DAO<T> {

    public boolean salvar(T t);
    
    public boolean excluir(T t);

    public List<T> listar(String hql);

    public T pegar(String hql);

}
