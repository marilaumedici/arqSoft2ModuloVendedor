package com.arqsoft.medici.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductosVendedorDomainDTO {
	
	private Integer paginaActual;
	private Integer totalPaginas;
	private long totalResultados;
	private List<ProductoDomainResponseDTO> productos = new ArrayList<ProductoDomainResponseDTO>();
	
	public ProductosVendedorDomainDTO(Integer paginaActual, Integer totalPaginas, long totalResultados, List<ProductoDomainResponseDTO> productos) {
		super();
		this.paginaActual = paginaActual;
		this.totalPaginas = totalPaginas;
		this.totalResultados = totalResultados;
		this.productos = productos;
	}
	
	public ProductosVendedorDomainDTO() {

	}
	
	public ProductosVendedorDomainDTO(Integer paginaActual, Integer totalPaginas, long totalResultados) {
		super();
		this.paginaActual = paginaActual;
		this.totalPaginas = totalPaginas;
		this.totalResultados = totalResultados;
	}

	public List<ProductoDomainResponseDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoDomainResponseDTO> productos) {
		this.productos = productos;
	}

	public Integer getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(Integer paginaActual) {
		this.paginaActual = paginaActual;
	}

	public Integer getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(Integer totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public long getTotalResultados() {
		return totalResultados;
	}

	public void setTotalResultados(long totalResultados) {
		this.totalResultados = totalResultados;
	}

}
