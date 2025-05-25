package com.arqsoft.medici.infrastructure.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.arqsoft.medici.application.ProductoService;
import com.arqsoft.medici.domain.dto.FiltroBuscadorProductoDomain;
import com.arqsoft.medici.domain.dto.ProductoDomainDTO;
import com.arqsoft.medici.domain.dto.ProductoDomainResponseDTO;
import com.arqsoft.medici.domain.dto.ProductosVendedorDomainDTO;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.domain.exceptions.VendedorNoEncontradoException;
import com.arqsoft.medici.infrastructure.rest.dto.FiltroBuscadorProducto;
import com.arqsoft.medici.infrastructure.rest.dto.ProductoDTO;
import com.arqsoft.medici.infrastructure.rest.dto.ProductoResponseDTO;
import com.arqsoft.medici.infrastructure.rest.dto.ProductosVendedorDTO;
import com.arqsoft.medici.infrastructure.rest.puertos.ProductoController;


@RestController
public class ProductoControllerImpl implements ProductoController {
	
	@Autowired
	private ProductoService productoService;
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public ProductoResponseDTO crearProducto(ProductoDTO dto) {
    	
    	try {
    		
    		ProductoDomainDTO request = modelMapper.map(dto, ProductoDomainDTO.class);
    		ProductoDomainResponseDTO response = productoService.crearProducto(request);
    		ProductoResponseDTO resonsedto = modelMapper.map(response, ProductoResponseDTO.class);

			return resonsedto;
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			
		} catch (VendedorNoEncontradoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el vendedor "+dto.getMailVendedor()+".", e);
			
		} catch (ValidacionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
			
		} catch (ProductoInexistenteException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }
    
	@Override
	public void modificarProducto(ProductoDTO dto) {
    	
    	try {
    		
    		ProductoDomainDTO request = modelMapper.map(dto, ProductoDomainDTO.class);
			productoService.modificarProducto(request);
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

		} catch (ProductoInexistenteException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }
    
	@Override
	public void eliminarProducto(String id, String mail) {

		try {
			
			productoService.eliminarProducto(id, mail);
			
		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

		} catch (ProductoInexistenteException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    }
    
	@Override
	public ProductosVendedorDTO obtenerProductosVendedor(String mail, Integer page, Integer elementos) {
    	
    	ProductosVendedorDTO response = new ProductosVendedorDTO();
		try {
			
			ProductosVendedorDomainDTO responseDTO = productoService.obtenerProductosVendedor(mail, page, elementos);
		    response = modelMapper.map(responseDTO, ProductosVendedorDTO.class);

		} catch (InternalErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}
    	
    	return response;
    }
	
	@Override
	public ProductosVendedorDTO obtenerProductosFiltro(FiltroBuscadorProducto dto) {
		
		ProductosVendedorDTO response = new ProductosVendedorDTO();
		
		try {
			
			FiltroBuscadorProductoDomain request = modelMapper.map(dto, FiltroBuscadorProductoDomain.class);
			ProductosVendedorDomainDTO responseDTO = productoService.obtenerProductosFiltrados(request);
		    response = modelMapper.map(responseDTO, ProductosVendedorDTO.class);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubo un error, por favor vuelva a probar mas adelante.", e);
			
		}

		return response;
	}
    

	public ProductoService getProductoService() {
		return productoService;
	}

	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}

}
