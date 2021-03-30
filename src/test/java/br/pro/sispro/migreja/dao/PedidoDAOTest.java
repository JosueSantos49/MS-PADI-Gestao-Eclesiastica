package br.pro.sispro.migreja.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Cliente;
import br.pro.sispro.migreja.domain.Departamento;
import br.pro.sispro.migreja.domain.Membro;
import br.pro.sispro.migreja.domain.Pedido;
import br.pro.sispro.migreja.domain.Pessoa;
import br.sispro.migreja.dao.ClienteDAO;
import br.sispro.migreja.dao.DepartamentoDAO;
import br.sispro.migreja.dao.MembroDAO;
import br.sispro.migreja.dao.PedidoDAO;
import br.sispro.migreja.dao.PessoaDAO;

public class PedidoDAOTest {
	@Test
	//@Ignore
	public void salvar() throws ParseException {
		
		//Pesquisar a pessoa, para depois inserir o cliente. Nessecario ter uma pessoa cadastrada.
		DepartamentoDAO dao = new DepartamentoDAO();
		Departamento departamento= dao.buscar(3L);
		
		MembroDAO membroDAO = new MembroDAO();
		Membro membro = membroDAO.buscar(3L);
		
		Pedido pedido = new Pedido();

		pedido.setAssunto("Assunto");
		pedido.setDescricao("Descrição");
		pedido.setDataSolicitacao(new Date());
		pedido.setStatusPedido("Pendente");
		pedido.setPrioridade("Baixa");
		pedido.setEmail("conceicaojosue@yahoo.com.br");
		pedido.setObs("Deus é fiel!");
		pedido.setDepartamento(departamento);
		pedido.setMembro(membro);
		
		PedidoDAO pedidoDAO = new PedidoDAO();
		pedidoDAO.salvar(pedido);
		
		System.out.println("Pedido salvo com sucesso!");
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
	@Ignore
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
