package com.arqsoft.medici.infrastructure.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.arqsoft.medici.application.VendedorService;
import com.arqsoft.medici.domain.dto.VendedorDatosDomainDTO;
import com.arqsoft.medici.domain.dto.VendedorDomainDTO;
import com.arqsoft.medici.domain.exceptions.FormatoEmailInvalidoException;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.VendedorExistenteException;
import com.arqsoft.medici.domain.exceptions.VendedorNoEncontradoException;
import com.arqsoft.medici.infrastructure.rest.dto.VendedorDTO;
import com.arqsoft.medici.infrastructure.rest.dto.VendedorDatosDTO;
import com.arqsoft.medici.infrastructure.rest.puertos.VendedorController;


@RestController
public class VendedorControllerImpl implements VendedorController {
	
	@Autowired
	private VendedorService vendedorService;
	private ModelMapper modelMapper = new ModelMapper();
	
    @Override
	public void crearVendedor(VendedorDTO dto) {

    	try {
    		
    		VendedorDomainDTO request = modelMapper.map(dto, VendedorDomainDTO.class);
			vendedorService.crearVendedor(request);
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			
		} catch (FormatoEmailInvalidoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email debe poseer un formato valido.", e);
			
		} catch (VendedorExistenteException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vendedor "+dto.getMail()+" ya existe.", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }
    
    @Override
	public void modificarVendedor(VendedorDTO dto) {
    	
    	try {
    		
    		VendedorDomainDTO request = modelMapper.map(dto, VendedorDomainDTO.class);
			vendedorService.modificarVendedor(request);
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			
		} catch (VendedorNoEncontradoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el vendedor "+dto.getMail()+".", e);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }
    
    @Override
	public void eliminarVendedor(String mail) {

			try {
				
				vendedorService.eliminarVendedor(mail);
				
			} catch (InternalErrorException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
				
			} catch (VendedorNoEncontradoException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el vendedor "+mail+".", e);

			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
				
			}
    }
	
    
    @Override
	public VendedorDatosDTO obtenerVendedor(String mail) {
		
    	try {
    		
    		VendedorDatosDomainDTO response = vendedorService.obtenerVendedor(mail);
    		VendedorDatosDTO dto = modelMapper.map(response, VendedorDatosDTO.class);
    		return dto;
			
		} catch (VendedorNoEncontradoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el vendedor "+mail+".", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }

	public VendedorService getVendedorService() {
		return vendedorService;
	}

	public void setVendedorService(VendedorService vendedorService) {
		this.vendedorService = vendedorService;
	}

}
