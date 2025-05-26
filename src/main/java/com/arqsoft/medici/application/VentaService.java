package com.arqsoft.medici.application;


import com.arqsoft.medici.domain.dto.RegistrarVentaDomainDTO;
import com.arqsoft.medici.domain.dto.VentaDomainDTO;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.UsuarioNoEncontradoException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;


public interface VentaService {

	VentaDomainDTO procesarVenta(RegistrarVentaDomainDTO request) throws InternalErrorException, ValidacionException, ProductoInexistenteException, UsuarioNoEncontradoException;

}
