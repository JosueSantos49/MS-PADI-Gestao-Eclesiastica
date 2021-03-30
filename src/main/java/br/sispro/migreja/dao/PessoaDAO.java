package br.sispro.migreja.dao;

import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.util.EmailUtils;

public class PessoaDAO extends GenericDAO<Pessoa>{
	
	//Ao cadastrar a pessoa será disparado um e-mail de confirmação por e-mail
	public void enviarConfirmacaoCadastroPorEmail(Pessoa pessoa) {
		
		pessoa.setEmail(pessoa.getEmail());

		if (pessoa.getCodigo() == null) {
					
			String nome = pessoa.getNome();
			String emaill = pessoa.getEmail();

			EmailUtils.enviaEmailConfirmaCadastro(pessoa, nome, emaill);
		}
	}
}
