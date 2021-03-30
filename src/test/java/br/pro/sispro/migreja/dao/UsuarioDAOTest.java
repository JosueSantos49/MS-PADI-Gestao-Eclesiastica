package br.pro.sispro.migreja.dao;

import java.text.ParseException;
import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Ignore;
import org.junit.Test;

import br.pro.sispro.migreja.domain.Pessoa;
import br.pro.sispro.migreja.domain.Usuario;
import br.sispro.migreja.dao.PessoaDAO;
import br.sispro.migreja.dao.UsuarioDAO;

public class UsuarioDAOTest {
	@Test
	@Ignore
	public void salvar() throws ParseException {
		
		//Pesquisar a pessoa, para depois inserir o usuario. Nessecario ter uma pessoa cadastrada.
		PessoaDAO pessoaDAO = new PessoaDAO();		
		Pessoa pessoa = pessoaDAO.buscar(4L);
		
		System.out.println("Pessoa Encontrada ");
		System.out.println("Nome: " + pessoa.getNome());
		System.out.println("CPF: " + pessoa.getCpf());
				
		Usuario usuario = new Usuario();
		usuario.setAtivo(true);
		usuario.setPessoa(pessoa);
		usuario.setSenhaSemCriptografia("777");
		
		SimpleHash hash = new SimpleHash("md5", usuario.getSenhaSemCriptografia());
		usuario.setSenha(hash.toHex());
		
		usuario.setTipo('X');
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.salvar(usuario);
		
		System.out.println("Usuário salvo com sucesso!");
		System.out.println("Código: " + usuario.getCodigo());
		System.out.println("Ativo: " + usuario.getAtivo());
		System.out.println("Senha: " + usuario.getSenha());
		System.out.println("Tipo: " + usuario.getTipo());
		System.out.println("Pessoa: " + usuario.getPessoa().getCodigo());
	}

	@Test
	@Ignore
	public void listar() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> resultado = usuarioDAO.listar();

		System.out.println("Total de registros encontrados: " + resultado.size());

		// Mostrar
		for (Usuario usuario : resultado) {
			System.out.println("Código do Usuário: " + usuario.getCodigo());
			System.out.println("Ativo: " + usuario.getAtivo());
			System.out.println("Tipo: " + usuario.getTipo());
			System.out.println("Pessoa Código: " + usuario.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void buscar() {
		Long codigo = 2L;

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(codigo);

		if (usuario == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Código do Usuário: " + usuario.getCodigo());
			System.out.println("Ativo: " + usuario.getAtivo());
			System.out.println("Tipo: " + usuario.getTipo());
			System.out.println("Pessoa Código: " + usuario.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void excluir() {
		Long codigo = 3L;
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(codigo);

		if (usuario == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			usuarioDAO.excluir(usuario);
			System.out.println("Usuário removido: ");
			System.out.println("Código do Usuário: " + usuario.getCodigo());
			System.out.println("Ativo: " + usuario.getAtivo());
			System.out.println("Tipo: " + usuario.getTipo());
			System.out.println("Pessoa Código: " + usuario.getPessoa().getCodigo());
			System.out.println();
		}
	}

	@Test
	@Ignore
	public void editar() throws ParseException {
			
		Long codigoUsuario = 2L;
		Long codigoPessoa = 3L;		
		
		PessoaDAO pessoaDAO = new PessoaDAO();		
		Pessoa pessoa = pessoaDAO.buscar(codigoPessoa);
		
		System.out.println("Código da Pessoa: " + pessoa.getCodigo());
		System.out.println("Nome da Pessoa: " + pessoa.getNome());
		System.out.println("Id da Pessoa: " + pessoa.getId());
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(codigoUsuario);

		if (usuario == null) {
			System.out.println("Nenhum registro encontrado!");
		} else {
			System.out.println("Antigo");
			System.out.println("Código do Usuário: " + usuario.getCodigo());
			System.out.println("Ativo: " + usuario.getAtivo());
			System.out.println("Tipo: " + usuario.getTipo());
			System.out.println("Pessoa Código: " + usuario.getPessoa().getCodigo());
			System.out.println();
						
			usuario.setSenha("123");
			usuario.setAtivo(true);
			usuario.setTipo('A');
			usuarioDAO.editar(usuario);
						
			System.out.println("Novo ");
			System.out.println("Código do Usuário: " + usuario.getCodigo());
			System.out.println("Ativo: " + usuario.getAtivo());
			System.out.println("Tipo: " + usuario.getTipo());
			System.out.println("Pessoa Código: " + usuario.getPessoa().getCodigo());
			System.out.println();
		}
	}
	
	@Test
	@Ignore
	public void autenticar(){
		String cpf = "777.777.777-77";
		String senha = "777";
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.autenticar(cpf, senha);
		
		System.out.println("Usuário autenticado: " + usuario.getCodigo());
	}
}
