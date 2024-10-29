package br.com.coldigogeladeiras.bd;

import java.sql.Connection;

// DUVIDAS REVER OT 17 DO BACK-END

public class Conexao {
	private Connection conexao;
	
	public Connection abrirConexao() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); //Aqui indicamos o driver JDBC.
			conexao = java.sql.DriverManager.
				getConnection("jdbc:mysql://localhost/coldigos?"
						+ "user=root&password=root&useTimezone=true&serverTimezone=UTC"); //Abrimos a conexão passando as config dela como parâmetro e a armazenamos no atributo conexao.
		
		}catch(Exception e) {
				e.printStackTrace();
		}
		return conexao; //Aqui retornamos o atributo 'conexao' para que possa ser usada por quem chamar o abrirConexao.
	}
	
	public void fecharConexao() {
		try {
			conexao.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
