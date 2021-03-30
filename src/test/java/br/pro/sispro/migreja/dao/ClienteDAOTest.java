package br.pro.sispro.migreja.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Cliente;
import br.pro.sispro.migreja.domain.Pessoa;
import br.sispro.migreja.dao.ClienteDAO;
import br.sispro.migreja.dao.PessoaDAO;

public class ClienteDAOTest {
	@Test
	@Ignore
	public void salvar() throws ParseException {
		
		//Pesquisar a pessoa, para depois inserir o cliente. Nessecario ter uma pessoa cadastrada.
		PessoaDAO pessoaDAO = new PessoaDAO();		
		Pessoa pessoa = pessoaDAO.buscar(1L);
		
		Cliente cliente = new Cliente();
		//cliente.setDataCadastro(new Date());//Data do sistema
		cliente.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("09/06/2015"));
		cliente.setLiberado(false);
		cliente.setPessoa(pessoa);
		
		ClienteDAO clienteDAO = new ClienteDAO();
		clienteDAO.salvar(cliente);
		
		System.out.println("Cliente salvo com sucesso!");
	}

	@Test
	@Ignore
	public void listar() {
		ClienteDAO clienteDAO = new ClienteDAO();
		List<Cliente> resultado = clienteDAO.listar();

		System.out.println("Total de registros encontrados: " + resultado.size());

		// Mostrar
		for (Cliente cliente : resultado) {
			System.out.println("Código da Cliente: " + cliente.getCodigo());
			System.out.println("Data de Cadastro: " + cliente.getDataCadastro());
			System.out.println("Liberado: " + cliente.getLiberado());
			System.out.println("Pessoa Código: " + cliente.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void buscar() {
		Long codigo = 2L;

		ClienteDAO clienteDAO = new ClienteDAO();
		Cliente cliente = clienteDAO.buscar(codigo);

		if (cliente == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Código da Cliente: " + cliente.getCodigo());
			System.out.println("Data de Cadastro: " + cliente.getDataCadastro());
			System.out.println("Liberado: " + cliente.getLiberado());
			System.out.println("Pessoa Código: " + cliente.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void excluir() {
		Long codigo = 2L;
		
		ClienteDAO clienteDAO = new ClienteDAO();
		Cliente cliente = clienteDAO.buscar(codigo);

		if (cliente == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			clienteDAO.excluir(cliente);
			System.out.println("Cidade removida: ");
			System.out.println("Código da Cliente: " + cliente.getCodigo());
			System.out.println("Data de Cadastro: " + cliente.getDataCadastro());
			System.out.println("Liberado: " + cliente.getLiberado());
			System.out.println("Pessoa Código: " + cliente.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	//@Ignore
	public void editar() throws ParseException {
			
		Long codigoCliente = 1L;
		Long codigoPessoa = 3L;		
		
		PessoaDAO pessoaDAO = new PessoaDAO();		
		Pessoa pessoa = pessoaDAO.buscar(codigoPessoa);
		
		System.out.println("Código da Pessoa: " + pessoa.getCodigo());
		System.out.println("Nome da Pessoa: " + pessoa.getNome());
		System.out.println("Id da Pessoa: " + pessoa.getId());
		
		ClienteDAO clienteDAO = new ClienteDAO();
		Cliente cliente = clienteDAO.buscar(codigoCliente);

		if (cliente == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Cidade removida: ");
			System.out.println("Código da Cliente: " + cliente.getCodigo());
			System.out.println("Data de Cadastro: " + cliente.getDataCadastro());
			System.out.println("Liberado: " + cliente.getLiberado());
			System.out.println("Pessoa Código: " + cliente.getPessoa().getCodigo());
			System.out.println();
						
			cliente.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2016"));
			cliente.setLiberado(true);
			clienteDAO.editar(cliente);
						
			System.out.println("Cidade removida: ");
			System.out.println("Código da Cliente: " + cliente.getCodigo());
			System.out.println("Data de Cadastro: " + cliente.getDataCadastro());
			System.out.println("Liberado: " + cliente.getLiberado());
			System.out.println("Pessoa Código: " + cliente.getPessoa().getCodigo());
			System.out.println();
		}
	}
}
