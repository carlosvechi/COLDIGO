package br.com.coldigogeladeiras.rest;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import javax.ws.rs.PathParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;


import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

@Path("marca") // Toda vez que alguém tentar acessar o servidor pelo endereço
				// "/TrilhaWeb/rest/marca", é essa classe que irá responder, utilizando o
				// '@Path("marca")'
public class MarcaRest extends UtilRest {

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String marcaParam) {
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean retorno = jdbcMarca.inserir(marca);
			String msg = " ";
			
			if(retorno) {
				msg = "Marca cadastrada com sucesso!";
			} else {
				msg = "Erro ao cadastrar produto.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	// Usamos '@' para criar em nossas classes, o que chamamos de 'anotações',
	// inserindo assim metadados do nosso código. Com o Jersey, usaremos várias
	// delas para informar como cada método de nossas classes REST devem se
	// comportar.

	@GET // Significa que o método a seguir será acessível apenas pelo método GET.
	@Path("/buscar") // Toda vez que alguém tentar acessar "/TrilhaWeb/rest/marca/buscar", é esse
					// método que responderá!.
	@Produces(MediaType.APPLICATION_JSON) // Significa que este método deve produzir um resultado, ou seja, deve
											// devolver algum valor ao lado cliente, no formado 'JSON'.
	public Response buscar() { // Método público, que terá implementação necessária para retornar as marcas
								// cadastradas no formato JSON.
		try {
			List<Marca> listaMarcas = new ArrayList<Marca>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscar();
			conec.fecharConexao();
			for(Marca m : listaMarcas) {
			}
			return this.buildResponse(listaMarcas);
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscarMarca")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON) //Diz que o retorno deve ser convertido em JSON.
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
		try {
			List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscarPorNome(nome);
			conec.fecharConexao();
			String json = new Gson().toJson(listaMarcas);
			return this.buildResponse(json);
		} catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		
		System.out.println(id);
		System.out.println("passei aqui");
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean retorno = jdbcMarca.deletar(id);
			
			String msg = "";
			
			if(retorno) {
				msg = "Produto excluído com sucesso!";
			} else {
				msg = "Erro ao excluír produto.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		} catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
		
		try {
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			marca = jdbcMarca.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(marca);
		} catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String marcaParam) {
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean retorno = jdbcMarca.alterar(marca);
			
			String msg = "";
			
			if (retorno) {
				msg = "Marca alterada com sucesso!";
			}else {
				msg = "Erro ao alterar marca.";
			}
			
			conec.fecharConexao();
			return this.buildResponse(msg);
		} catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
}
