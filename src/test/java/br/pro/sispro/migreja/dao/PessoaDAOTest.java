package br.pro.sispro.migreja.dao;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Cidade;
import br.pro.sispro.migreja.domain.Pessoa;
import br.sispro.migreja.dao.CidadeDAO;
import br.sispro.migreja.dao.PessoaDAO;

public class PessoaDAOTest {
	@Test
	@Ignore 
	public void salvar() {
		
		CidadeDAO cidadeDAO = new CidadeDAO();
		Cidade cidade = cidadeDAO.buscar(2L);
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Marcos");
		pessoa.setCpf("1282618785");
		pessoa.setId("1237");
		pessoa.setNumero("28");
		pessoa.setRua("Rua X");
		pessoa.setBairro("Boa Esperança");
		pessoa.setCep("23890000");
		pessoa.setComplemento("");
		pessoa.setTelefone("26820659");
		pessoa.setCelular("976538719");
		pessoa.setEmail("conceicaojosue@outlook.com");
		pessoa.setCidade(cidade);

		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoaDAO.salvar(pessoa);
	}

	@Test
	@Ignore
	public void listar() {
		PessoaDAO pessoaDAO = new PessoaDAO();
		List<Pessoa> resultado = pessoaDAO.listar();

		System.out.println("Total de registros encontrados: " + resultado.size());

		// Mostrar
		for (Pessoa pessoa : resultado) {
			System.out.println(pessoa.getCodigo() + " - " + pessoa.getCpf() + " - " + pessoa.getId());
			System.out.println("Código da Cidade: " + pessoa.getCidade().getCodigo());
			System.out.println("Nome da Cidade: " + pessoa.getCidade().getNome());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void buscar() {
		Long codigo = 2L;
		PessoaDAO pessoaDAO = new PessoaDAO();
		Pessoa pessoa = pessoaDAO.buscar(codigo);

		if (pessoa == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println(pessoa.getCodigo() + " - " + pessoa.getCpf() + " - " + pessoa.getId());
			System.out.println("Código da Cidade: " + pessoa.getCidade().getCodigo());
			System.out.println("Nome da Cidade: " + pessoa.getCidade().getNome());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void excluir() {
		Long codigo = 2L;
		PessoaDAO pessoaDAO = new PessoaDAO();
		Pessoa pessoa = pessoaDAO.buscar(codigo);

		if (pessoa == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			pessoaDAO.excluir(pessoa);
			System.out.println("Pessoa removida: ");
			System.out.println(pessoa.getCodigo() + " - " + pessoa.getCpf() + " - " + pessoa.getId());
			System.out.println("Código da Cidade: " + pessoa.getCidade().getCodigo());
			System.out.println("Nome da Cidade: " + pessoa.getCidade().getNome());
			System.out.println();		}
	}

	@Test
	//@Ignore
	public void editar() {
		Long codigoPessoa = 1L;
		Long codigoCidade = 3L;
		
		CidadeDAO cidadeDAO = new CidadeDAO();
		Cidade cidade = cidadeDAO.buscar(codigoCidade);
		System.out.println("Código da Cidade: " + cidade.getCodigo());
		System.out.println("Nome da Cidade: " + cidade.getNome()); 
		System.out.println("Código do Estado: " + cidade.getEstado().getCodigo());
		
		PessoaDAO pessoaDAO = new PessoaDAO();
		Pessoa pessoa = pessoaDAO.buscar(codigoPessoa);

		if (pessoa == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Registro editado antes: ");
			System.out.println("Código da Cidade: " + cidade.getCodigo());
			System.out.println("Nome da Cidade: " + cidade.getNome()); 
			System.out.println("Código do Estado: " + cidade.getEstado().getCodigo());			
			System.out.println("Código da Pessoa: " + pessoa.getCodigo());
			System.out.println("Nome da Pessoa: " + pessoa.getNome()); 
			System.out.println("Código do Cpf: " + pessoa.getCpf());
			
			pessoa.setNome("Vanessa");
			pessoa.setCpf("00000000");
			pessoaDAO.editar(pessoa);
						
			System.out.println("Registro editado depois: ");
			System.out.println("Código da Cidade: " + cidade.getCodigo());
			System.out.println("Nome da Cidade: " + cidade.getNome()); 
			System.out.println("Código do Estado: " + cidade.getEstado().getCodigo());			
			System.out.println("Código da Pessoa: " + pessoa.getCodigo());
			System.out.println("Nome da Pessoa: " + pessoa.getNome()); 
			System.out.println("Código do Cpf: " + pessoa.getCpf());
		}
	}
}
