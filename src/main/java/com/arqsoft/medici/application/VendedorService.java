package com.arqsoft.medici.application;

import com.arqsoft.medici.domain.Vendedor;
import com.arqsoft.medici.domain.dto.VendedorDatosDomainDTO;
import com.arqsoft.medici.domain.dto.VendedorDomainDTO;
import com.arqsoft.medici.domain.exceptions.FormatoEmailInvalidoException;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.VendedorExistenteException;
import com.arqsoft.medici.domain.exceptions.VendedorNoEncontradoException;

public interface VendedorService {

	void crearVendedor(VendedorDomainDTO request) throws InternalErrorException, FormatoEmailInvalidoException, VendedorExistenteException;

	void modificarVendedor(VendedorDomainDTO request) throws InternalErrorException, VendedorNoEncontradoException;
	
	void eliminarVendedor(String mail) throws InternalErrorException, VendedorNoEncontradoException;
	
	Vendedor obtenerVendedorEntidad(String mailVendedor) throws InternalErrorException, VendedorNoEncontradoException;

	void actualizarVendedor(Vendedor vendedor);

	VendedorDatosDomainDTO obtenerVendedor(String mail) throws VendedorNoEncontradoException;



}
