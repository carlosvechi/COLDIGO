package br.com.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;


import br.com.coldigogeladeiras.jdbcinterface.ProdutoDAO;

import br.com.coldigogeladeiras.modelo.Produto;
import com.google.gson.JsonObject;

public class JDBCProdutoDAO implements ProdutoDAO {

	private Connection conexao;
	
	//Construtor da classe. Recebe o mesmo nome da classe.
	public JDBCProdutoDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean inserir(Produto produto) {
		String comando = "INSERT INTO produtos "
				+ "(id, categoria, modelo, capacidade, valor, marcas_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement p; //PreparedStatement é a função que dará as intruções para nosso banco de dados, neste caso, o MySQL
		
		try {
			//Prepara o comando para execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos valores do produto
			p.setInt(1, produto.getId());
			p.setString(2, produto.getCategoria());
			p.setString(3, produto.getModelo());
			p.setInt(4, produto.getCapacidade());
			p.setFloat(5, produto.getValor());
			p.setInt(6, produto.getMarcaId());
			
			//Executa o comando no BD
			p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<JsonObject> buscarPorNome(String nome) {
		//Inicia a criação do comando SQL de busca
		String comando = "SELECT produtos.*, marcas.nome as marca FROM produtos "
				+ "INNER JOIN marcas ON produtos.marcas_id = marcas.id ";
		//Se o nome não estiver vazio...
		if (!nome.equals("")) {
			//o concatena no comando o WHERE buscando no nome do produto o texto da variável nome
			comando += "WHERE modelo LIKE '%" + nome + "%' "; //Pesquisa tudo que tem antes e depois do valor digitado. 
		}
		//Finaliza o comando ordenando alfabeticamente por categoria, marca e depois modelo
		comando += "ORDER BY categoria ASC, marcas.nome ASC, modelo ASC";
		List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
		JsonObject produto = null;
		
		try {
			Statement stmt = conexao.createStatement(); //Criamos um objeto stmt que receberá a conexão. Statement é um objeto que permite enviar uma instrução SQL.
			ResultSet rs = stmt.executeQuery(comando); //rs é um outro objeto que receberá a conexão, e executará o comando no MySQL
			
			while (rs.next()) { //Executará as instruções SQL no while toda vez que houver mais registros a serem lidos, pegará os dados e salvará nas variavéis. 
				
				int id = rs.getInt("id"); //O que está em parenteses é o nome da coluna do BD.
				String categoria = rs.getString("categoria");
				String modelo = rs.getString("modelo");
				int capacidade = rs.getInt("capacidade");
				float valor = rs.getFloat("valor");
				String marcaNome = rs.getString("marca");			
				if (categoria.equals("1")) {
					categoria = "Geladeira";
				}else if (categoria.equals("2")) {
					categoria = "Freezer";
				}
				
				produto = new JsonObject(); //Instanciamos o objeto produto com a array 'listaProdutos'. Ou seja, estaremos implementando objetos dentro desta array.
				produto.addProperty("id", id); //"id" = nome do campo // id = valor contido.
				produto.addProperty("categoria", categoria);
				produto.addProperty("modelo", modelo);
				produto.addProperty("capacidade", capacidade);
				produto.addProperty("valor", valor);
				produto.addProperty("marcaNome", marcaNome);
				listaProdutos.add(produto); //Adiciona o objeto na array;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return listaProdutos;
	}
	
	public boolean deletar(int id) {
		String comando = "DELETE FROM produtos WHERE id = ?";
		PreparedStatement p; //PreparedStatement é a função que dará as intruções para nosso banco de dados, neste caso, o MySQL
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Produto buscarPorId(int id) {
		String comando = "SELECT * FROM produtos WHERE produtos.id = ?";
		Produto produto = new Produto();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando); //PreparedStatement é a função que dará as intruções para nosso banco de dados, neste caso, o MySQL
			p.setInt(1,  id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				//Aqui ele puxa os dados do BD para as varivéis.
				String categoria = rs.getString("categoria");
				String modelo = rs.getString("modelo");
				int capacidade = rs.getInt("capacidade");
				float valor = rs.getFloat("valor");
				int marcaId = rs.getInt("marcas_id");
				
				//E aqui das variavéis para os atributos.
				produto.setId(id);
				produto.setCategoria(categoria);
				produto.setMarcaId(marcaId);
				produto.setModelo(modelo);
				produto.setCapacidade(capacidade);
				produto.setValor(valor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produto;
	}
	
	public boolean alterar(Produto produto) {
	
		String comando = "UPDATE produtos "
				+ "SET categoria=?, modelo=?, capacidade=?, valor=?, marcas_id=?"
				+ " WHERE id=?";
		PreparedStatement p; //PreparedStatement é a função que dará as intruções para nosso banco de dados, neste caso, o MySQL
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, produto.getCategoria());
			p.setString(2, produto.getModelo());
			p.setInt(3, produto.getCapacidade());
			p.setFloat(4, produto.getValor());
			p.setInt(5, produto.getMarcaId());
			p.setInt(6,  produto.getId());
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
