package br.com.coldigogeladeiras.jdbcinterface;

import java.util.List;

import br.com.coldigogeladeiras.modelo.Marca;

public interface marcaDAO {
	public List<Marca> buscar();
}
