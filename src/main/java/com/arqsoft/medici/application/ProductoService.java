package com.arqsoft.medici.application;

import com.arqsoft.medici.domain.Producto;
import com.arqsoft.medici.domain.dto.FiltroBuscadorProductoDomain;
import com.arqsoft.medici.domain.dto.ProductoDomainDTO;
import com.arqsoft.medici.domain.dto.ProductoDomainResponseDTO;
import com.arqsoft.medici.domain.dto.ProductosVendedorDomainDTO;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.domain.exceptions.VendedorNoEncontradoException;

public interface ProductoService {
	
	public ProductoDomainResponseDTO crearProducto(ProductoDomainDTO request) throws InternalErrorException, VendedorNoEncontradoException, ValidacionException, ProductoInexistenteException;

	public void modificarProducto(ProductoDomainDTO request) throws InternalErrorException, ProductoInexistenteException;

	public void eliminarProducto(String id, String mail) throws InternalErrorException, ProductoInexistenteException;
	
	public ProductosVendedorDomainDTO obtenerProductosVendedor(String mail, Integer pagina, Integer size) throws InternalErrorException;

	/**
	 * Recibe un productoId y devuelve un producto DISPONIBLE
	 * @param productoId
	 * @return Producto
	 * @throws ProductoInexistenteException
	 */
	public Producto obtenerProductoByID(String productoId) throws ProductoInexistenteException;

	public void descontarStock(Producto producto, Integer cantidad) throws ValidacionException;

	public ProductosVendedorDomainDTO obtenerProductosFiltrados(FiltroBuscadorProductoDomain request);
	
	public Producto obtenerProductoByIDNoEstado(String productoId) throws ProductoInexistenteException;

}
