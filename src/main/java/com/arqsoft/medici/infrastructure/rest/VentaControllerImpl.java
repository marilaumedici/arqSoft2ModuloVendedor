package com.arqsoft.medici.infrastructure.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.arqsoft.medici.application.VentaService;
import com.arqsoft.medici.domain.dto.RegistrarVentaDomainDTO;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.UsuarioNoEncontradoException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.infrastructure.rest.dto.RegistrarVentaDTO;
import com.arqsoft.medici.infrastructure.rest.puertos.VentaController;


@RestController
public class VentaControllerImpl implements VentaController {
	
	@Autowired
	private VentaService ventaService;
	private ModelMapper modelMapper = new ModelMapper();

	 @Override
	 public void registrarVenta(RegistrarVentaDTO dto) {
		 
		 try {
			
			RegistrarVentaDomainDTO request = modelMapper.map(dto, RegistrarVentaDomainDTO.class);
			ventaService.procesarVenta(request);
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

		} catch (ValidacionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);

		} catch (ProductoInexistenteException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);

		} catch (UsuarioNoEncontradoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el usuario "+dto.getMailComprador()+".", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
	 }

	public VentaService getVentaService() {
		return ventaService;
	}

	public void setVentaService(VentaService ventaService) {
		this.ventaService = ventaService;
	}

}
