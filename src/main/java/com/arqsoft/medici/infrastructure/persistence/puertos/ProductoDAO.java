package com.arqsoft.medici.infrastructure.persistence.puertos;

import com.arqsoft.medici.domain.dto.FiltroBuscadorProductoDomain;
import com.arqsoft.medici.infrastructure.rest.dto.ProductosPaginado;

public interface ProductoDAO {
	
	public ProductosPaginado buscarProductos(FiltroBuscadorProductoDomain filtro);

}
