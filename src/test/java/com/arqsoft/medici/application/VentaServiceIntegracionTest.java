package com.arqsoft.medici.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.arqsoft.medici.infrastructure.cliente.NotificacionCliente;
import com.arqsoft.medici.infrastructure.cliente.UsuarioCliente;
import com.arqsoft.medici.infrastructure.persistence.ProductoRepository;
import com.arqsoft.medici.infrastructure.persistence.VentaRepository;
import com.arqsoft.medici.infrastructure.cliente.UsuarioDTO;
import com.arqsoft.medici.domain.dto.VendedorDomainDTO;
import com.arqsoft.medici.domain.dto.VentaDomainDTO;
import com.arqsoft.medici.domain.exceptions.FormatoEmailInvalidoException;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.UsuarioNoEncontradoException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.domain.exceptions.VendedorExistenteException;
import com.arqsoft.medici.domain.exceptions.VendedorNoEncontradoException;
import com.arqsoft.medici.domain.utils.ProductoCategoria;
import com.arqsoft.medici.domain.utils.ProductoEstado;
import com.arqsoft.medici.domain.dto.ProductoDomainResponseDTO;
import com.arqsoft.medici.domain.dto.ProductoDomainDTO;
import com.arqsoft.medici.domain.dto.RegistrarVentaDomainDTO;
import com.arqsoft.medici.domain.Producto;

@SpringBootTest
public class VentaServiceIntegracionTest {
	
	@Autowired
    private VentaServiceImpl ventaService;
	
	@Autowired
	private VendedorServiceImpl vendedorService;
	
	@Autowired
	private ProductoServiceImpl productoService;
	
	@Autowired
	private UsuarioCliente usuarioClient;
	
	@Autowired
	private NotificacionCliente notificacionClient;
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private VentaRepository ventaRepository;
	
	
	@Test
	public void testCrearVentaCorrecta() throws InternalErrorException, FormatoEmailInvalidoException, VendedorExistenteException, VendedorNoEncontradoException, ValidacionException, ProductoInexistenteException, UsuarioNoEncontradoException {

		String vendedorMail = "vendedortest@prueba.com";
		String compradorMail = "compradortest@prueba.com";
		
		//Se crea el usuario comprador
		UsuarioDTO usuarioTest = new UsuarioDTO("Testy", "Testy", compradorMail);
		usuarioClient.crearUsuario(usuarioTest);
		//Se crea el usuario vendedor
		VendedorDomainDTO vendedorTest = new VendedorDomainDTO(vendedorMail, "Vendedor Prueba");
		vendedorService.crearVendedor(vendedorTest);
		
		ProductoDomainDTO productoTest = new ProductoDomainDTO("Donas de chocolate", "Caja de 6 donas de chocolate", 12000, 50, ProductoCategoria.ALIMENTOS, vendedorMail);
		ProductoDomainResponseDTO productoResponse = productoService.crearProducto(productoTest);
		
		RegistrarVentaDomainDTO solicitarventa =  new RegistrarVentaDomainDTO(productoResponse.getCodigoProducto(), compradorMail, 2);
		VentaDomainDTO  venta = ventaService.procesarVenta(solicitarventa);
		
		Producto productoBDD = productoService.obtenerProductoByID(productoResponse.getCodigoProducto());
		
	    assertEquals(48, productoBDD.getStock());
	    assertEquals(ProductoEstado.DISPONIBLE, productoBDD.getEstado());
	    assertEquals(2, venta.getCantidadComprada());
	    assertEquals(2 * 12000, venta.getPrecioFinal());
	    assertEquals(productoResponse.getCodigoProducto(), venta.getProductoId());
	    assertEquals(compradorMail, venta.getMailComprador());
	    assertEquals(vendedorMail, venta.getMailVendedor());

	    ventaRepository.deleteById(venta.getVentaId());
	    productoRepository.deleteById(productoBDD.getProductoId());
	    usuarioClient.eliminarUsuario(compradorMail);
	    vendedorService.eliminarVendedor(vendedorMail);
	}
	
	@Test
	public void testCrearVentaCorrectaAgotoProducto() throws InternalErrorException, FormatoEmailInvalidoException, VendedorExistenteException, VendedorNoEncontradoException, ValidacionException, ProductoInexistenteException, UsuarioNoEncontradoException {
		
		String vendedorMail = "vendedortest2@prueba.com";
		String compradorMail = "compradortest2@prueba.com";
		
		//Se crea el usuario comprador
		UsuarioDTO usuarioTest = new UsuarioDTO("Testy", "Testy", compradorMail);
		usuarioClient.crearUsuario(usuarioTest);
		//Se crea el usuario vendedor
		VendedorDomainDTO vendedorTest = new VendedorDomainDTO(vendedorMail, "Vendedor Prueba");
		vendedorService.crearVendedor(vendedorTest);
		
		ProductoDomainDTO productoTest = new ProductoDomainDTO("Donas de vainilla", "Caja de 6 donas de vainilla", 12000, 2, ProductoCategoria.ALIMENTOS, vendedorMail);
		ProductoDomainResponseDTO productoResponse = productoService.crearProducto(productoTest);
		
		RegistrarVentaDomainDTO solicitarventa =  new RegistrarVentaDomainDTO(productoResponse.getCodigoProducto(), compradorMail, 2);
		VentaDomainDTO  venta = ventaService.procesarVenta(solicitarventa);
		
		Producto productoBDD = productoService.obtenerProductoByIDNoEstado(productoResponse.getCodigoProducto());
		
	    assertEquals(0, productoBDD.getStock());
	    assertEquals(ProductoEstado.NO_DISPONIBLE, productoBDD.getEstado());
	    assertEquals(2, venta.getCantidadComprada());
	    assertEquals(2 * 12000, venta.getPrecioFinal());
	    assertEquals(productoBDD.getProductoId(), venta.getProductoId());
	    assertEquals(compradorMail, venta.getMailComprador());
	    assertEquals(vendedorMail, venta.getMailVendedor());

	    ventaRepository.deleteById(venta.getVentaId());
	    productoRepository.deleteById(productoBDD.getProductoId());
	    usuarioClient.eliminarUsuario(compradorMail);
	    vendedorService.eliminarVendedor(vendedorMail);
	}
	
	
	@Test
	public void testCrearVentaStockInsuficiente() throws InternalErrorException, FormatoEmailInvalidoException, VendedorExistenteException, VendedorNoEncontradoException, ValidacionException, ProductoInexistenteException, UsuarioNoEncontradoException {
	
		String vendedorMail = "vendedortest3@prueba.com";
		String compradorMail = "compradortest3@prueba.com";
		
		//Se crea el usuario comprador
		UsuarioDTO usuarioTest = new UsuarioDTO("Testy", "Testy", compradorMail);
		usuarioClient.crearUsuario(usuarioTest);
		//Se crea el usuario vendedor
		VendedorDomainDTO vendedorTest = new VendedorDomainDTO(vendedorMail, "Vendedor Prueba");
		vendedorService.crearVendedor(vendedorTest);
		
		ProductoDomainDTO productoTest = new ProductoDomainDTO("Donas de frutilla", "Caja de 6 donas de frutilla", 12000, 1, ProductoCategoria.ALIMENTOS, vendedorMail);
		ProductoDomainResponseDTO productoResponse = productoService.crearProducto(productoTest);
		
		RegistrarVentaDomainDTO solicitarventa =  new RegistrarVentaDomainDTO(productoResponse.getCodigoProducto(), compradorMail, 5);		
		assertThrows(ValidacionException.class, () -> { ventaService.procesarVenta(solicitarventa); });

	    productoRepository.deleteById(productoResponse.getCodigoProducto());
	    usuarioClient.eliminarUsuario(compradorMail);
	    vendedorService.eliminarVendedor(vendedorMail);
		
	}
	

	public NotificacionCliente getNotificacionClient() {
		return notificacionClient;
	}

	public void setNotificacionClient(NotificacionCliente notificacionClient) {
		this.notificacionClient = notificacionClient;
	}

	public UsuarioCliente getUsuarioClient() {
		return usuarioClient;
	}

	public void setUsuarioClient(UsuarioCliente usuarioClient) {
		this.usuarioClient = usuarioClient;
	}

	public VentaRepository getVentaRepository() {
		return ventaRepository;
	}

	public void setVentaRepository(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
	}

	public ProductoServiceImpl getProductoService() {
		return productoService;
	}

	public void setProductoService(ProductoServiceImpl productoService) {
		this.productoService = productoService;
	}

	public VentaServiceImpl getVentaService() {
		return ventaService;
	}

	public void setVentaService(VentaServiceImpl ventaService) {
		this.ventaService = ventaService;
	}


	public VendedorServiceImpl getVendedorService() {
		return vendedorService;
	}


	public void setVendedorService(VendedorServiceImpl vendedorService) {
		this.vendedorService = vendedorService;
	}


	public ProductoRepository getProductoRepository() {
		return productoRepository;
	}


	public void setProductoRepository(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

}
