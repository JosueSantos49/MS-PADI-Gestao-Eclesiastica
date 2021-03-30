package br.pro.sispro.migreja.dao;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Fabricante;
import br.pro.sispro.migreja.domain.Produto;
import br.sispro.migreja.dao.FabricanteDAO;
import br.sispro.migreja.dao.ProdutoDAO;

public class ProdutoDAOTest {
	@Test
	@Ignore
	public void salvar() {
		
		FabricanteDAO fabricanteDAO = new FabricanteDAO();	
		Fabricante fabricante = fabricanteDAO.buscar(1L);
		
		Produto produto = new Produto();
		produto.setDescricao("Produto 2");
		produto.setQuantidade(new Short((short) 100));
		produto.setPreco(new BigDecimal(200.00));
		produto.setFabricante(fabricante);
		
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoDAO.salvar(produto);
	}

	@Test
	@Ignore
	public void listar() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		List<Produto> resultado = produtoDAO.listar();

		System.out.println("Total de registros encontrados: " + resultado.size());

		// Mostrar
		for (Produto produto : resultado) {
			System.out.println("Código do produto: " + produto.getCodigo());
			System.out.println("Descrição do produto: " + produto.getDescricao());
			System.out.println("Quantidade de produto: " + produto.getQuantidade());
			System.out.println("Preço do produto: " + produto.getPreco());
			System.out.println("Descrição do Fabricante: " + produto.getFabricante().getDescricao());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void buscar() {
		Long codigo = 2L;

		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.buscar(codigo);

		if (produto == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Código do produto: " + produto.getCodigo());
			System.out.println("Descrição do produto: " + produto.getDescricao());
			System.out.println("Quantidade de produto: " + produto.getQuantidade());
			System.out.println("Preço do produto: " + produto.getPreco());
			System.out.println("Descrição do Fabricante: " + produto.getFabricante().getDescricao());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void excluir() {
		Long codigo = 1L;
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.buscar(codigo);

		if (produto == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			produtoDAO.excluir(produto);
			System.out.println("Código do produto: " + produto.getCodigo());
			System.out.println("Descrição do produto: " + produto.getDescricao());
			System.out.println("Quantidade de produto: " + produto.getQuantidade());
			System.out.println("Preço do produto: " + produto.getPreco());
			System.out.println("Descrição do Fabricante: " + produto.getFabricante().getDescricao());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void editar() {
				
		Long codigoProduto = 2L;
		Long codigoFabricante = 1L;
		
		FabricanteDAO fabricanteDAO = new FabricanteDAO();
		Fabricante fabricante = fabricanteDAO.buscar(codigoFabricante); 
		
		System.out.println("Código do Fabricante: " + fabricante.getCodigo());
		System.out.println("Descrição do Fabricante: " + fabricante.getDescricao());
		System.out.println();
		
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.buscar(codigoProduto);

		if (produto == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Registro editado antes: ");
			System.out.println("Código do produto: " + produto.getCodigo());
			System.out.println("Descrição do produto: " + produto.getDescricao());
			System.out.println("Quantidade de produto: " + produto.getQuantidade());
			System.out.println("Preço do produto: " + produto.getPreco());
			System.out.println("Descrição do Fabricante: " + produto.getFabricante().getDescricao());
			System.out.println();
			
			produto.setDescricao("Produto 50");	
			produto.setQuantidade(new Short((short) 1100));
			produto.setPreco(new BigDecimal(250.00));
			produtoDAO.editar(produto);
						
			System.out.println("Registro editado depois: ");
			System.out.println("Código do produto: " + produto.getCodigo());
			System.out.println("Descrição do produto: " + produto.getDescricao());
			System.out.println("Quantidade de produto: " + produto.getQuantidade());
			System.out.println("Preço do produto: " + produto.getPreco());
			System.out.println("Descrição do Fabricante: " + produto.getFabricante().getDescricao());
			System.out.println();
		}
	}
}
