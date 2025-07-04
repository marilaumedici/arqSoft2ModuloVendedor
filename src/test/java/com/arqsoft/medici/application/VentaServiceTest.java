package com.arqsoft.medici.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.arqsoft.medici.domain.Producto;
import com.arqsoft.medici.domain.Vendedor;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.UsuarioNoEncontradoException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.domain.utils.ProductoCategoria;
import com.arqsoft.medici.infrastructure.cliente.NotificacionCliente;
import com.arqsoft.medici.infrastructure.cliente.UsuarioCliente;
import com.arqsoft.medici.infrastructure.persistence.VentaRepository;
import com.arqsoft.medici.domain.Venta;
import com.arqsoft.medici.domain.dto.RegistrarVentaDomainDTO;
import com.arqsoft.medici.infrastructure.cliente.UsuarioResponseDTO;
import com.arqsoft.medici.infrastructure.cliente.NotificacionRequestDTO;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
	
	@InjectMocks
	private VentaServiceImpl ventaService;
	
	@Mock
	private ProductoServiceImpl productoService;
	
	@Mock
	private VentaRepository ventaRepository;
	
	@Captor
	private ArgumentCaptor<Venta> ventaCaptor;
	
	@Mock
	private UsuarioCliente usuarioClient;
	
	@Captor
	private ArgumentCaptor<NotificacionRequestDTO> ventaCompradorCaptor;
	
	@Captor
	private ArgumentCaptor<NotificacionRequestDTO> ventaVendedorCaptor;
	
	@Mock
	private NotificacionCliente notificacionClient;
	
	
	private String mailVendedor1 = "naturisteros@gmail.com";
	private String razonSoacialVendedor1 = "Tienda naturista Naturisteros";
	
	private String nombreP1 = "Pistachos 400gm";
	private String descripcionP1 = "Bolsa de pistachos tostados y salados 400gm";
	private double precioP1 = 14000;
	private int stockP1 = 35;
	private String codigoP1    = "680fff46b369023700f29045";
	private ProductoCategoria ALIMENTOS = ProductoCategoria.ALIMENTOS;

	private String email_comprador    		= "agussusu@gmal.com";
	private String nombre_comprador    		= "Agus";
	private String apellido_comprador    		= "Susu";


	
	@Test
	public void procesarVentaOK() throws UsuarioNoEncontradoException, ProductoInexistenteException, ValidacionException {
		
		int cantidad = 10;
		
		UsuarioResponseDTO userDTO = new UsuarioResponseDTO(nombre_comprador,apellido_comprador, email_comprador);
		when(usuarioClient.obtenerUsuario(email_comprador)).thenReturn(userDTO);
		
		Producto producto = new Producto(codigoP1, nombreP1, descripcionP1, precioP1, stockP1, ALIMENTOS);
		Vendedor vendedor = new Vendedor(mailVendedor1, razonSoacialVendedor1);
		producto.setVendedor(vendedor);
		when(productoService.obtenerProductoByID(codigoP1)).thenReturn(producto);

		when(ventaRepository.insert(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		RegistrarVentaDomainDTO request =  new RegistrarVentaDomainDTO();
		request.setCantidad(cantidad);
		request.setMailComprador(email_comprador);
		request.setProductoId(codigoP1);
		assertDoesNotThrow(() -> { ventaService.procesarVenta(request); });

		verify(ventaRepository).insert(ventaCaptor.capture());
		
		verify(productoService).descontarStock(any(Producto.class), any(Integer.class) );

		
		Venta capturado = ventaCaptor.getValue();
		
	    assertEquals(cantidad, capturado.getCantidadComprada());
	    assertEquals(email_comprador, capturado.getMailComprador());
	    assertEquals(mailVendedor1, capturado.getMailVendedor());
	    assertEquals(cantidad * precioP1, capturado.getPrecioFinal());
	    assertEquals(precioP1, capturado.getPrecioIndividual());
	    assertEquals(codigoP1, capturado.getProductoId());
	    
	    verify(notificacionClient).sendNotificacionVendedor(ventaVendedorCaptor.capture());
	    
	    verify(notificacionClient).sendNotificacionUsuario(ventaCompradorCaptor.capture());
	    
	    NotificacionRequestDTO notiVendedorcapturado = ventaVendedorCaptor.getValue();
		
	    assertEquals(mailVendedor1, notiVendedorcapturado.getEmail());
	    assertEquals(nombreP1, notiVendedorcapturado.getNombreProducto());
	    assertEquals(razonSoacialVendedor1, notiVendedorcapturado.getNombreUsuario());
	    
	    NotificacionRequestDTO notiCompradorcapturado = ventaCompradorCaptor.getValue();

	    assertEquals(email_comprador, notiCompradorcapturado.getEmail());
	    assertEquals(nombreP1, notiCompradorcapturado.getNombreProducto());
	    assertEquals(nombre_comprador+" "+apellido_comprador, notiCompradorcapturado.getNombreUsuario());

	}
	
	@Test
	public void procesarVentaInsuficienteStock() throws UsuarioNoEncontradoException, ProductoInexistenteException, ValidacionException {
		
		int cantidad = 10;
		int stockInsuficiente = 1;
		
		when(usuarioClient.obtenerUsuario(email_comprador)).thenReturn(new UsuarioResponseDTO());
		
		Producto producto = new Producto(codigoP1, nombreP1, descripcionP1, precioP1, stockInsuficiente, ALIMENTOS);
		Vendedor vendedor = new Vendedor(mailVendedor1, razonSoacialVendedor1);
		producto.setVendedor(vendedor);
		when(productoService.obtenerProductoByID(codigoP1)).thenReturn(producto);

		doThrow(new ValidacionException("La cantidad ingresada debe ser menor o igual al stock disponible.")).when(productoService).descontarStock(producto, cantidad );
		
		RegistrarVentaDomainDTO request =  new RegistrarVentaDomainDTO();
		request.setCantidad(cantidad);
		request.setMailComprador(email_comprador);
		request.setProductoId(codigoP1);
		assertThrows(ValidacionException.class, () -> { ventaService.procesarVenta(request); });

		verify(productoService).descontarStock(any(Producto.class), any(Integer.class) );


	}
	
	
	@Test
	public void procesarVentaCantidadSolicitadaCero() throws UsuarioNoEncontradoException, ProductoInexistenteException, ValidacionException {
		
		int cantidad = 0;
		int stockInsuficiente = 1;
		
		when(usuarioClient.obtenerUsuario(email_comprador)).thenReturn(new UsuarioResponseDTO());
		
		Producto producto = new Producto(codigoP1, nombreP1, descripcionP1, precioP1, stockInsuficiente, ALIMENTOS);
		Vendedor vendedor = new Vendedor(mailVendedor1, razonSoacialVendedor1);
		producto.setVendedor(vendedor);
		when(productoService.obtenerProductoByID(codigoP1)).thenReturn(producto);

		doThrow(new ValidacionException("La cantidad ingresada debe ser mayor o igual a 1.")).when(productoService).descontarStock(producto, cantidad );
		
		RegistrarVentaDomainDTO request =  new RegistrarVentaDomainDTO();
		request.setCantidad(cantidad);
		request.setMailComprador(email_comprador);
		request.setProductoId(codigoP1);
		assertThrows(ValidacionException.class, () -> { ventaService.procesarVenta(request); });


	}
	
	@Test
	public void procesarVentaUsuarioNoEncontrado() throws UsuarioNoEncontradoException, ProductoInexistenteException, ValidacionException {
		
		int cantidad = 10;
		
		doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro el usuario.", new UsuarioNoEncontradoException())).when(usuarioClient).obtenerUsuario(email_comprador);

		RegistrarVentaDomainDTO request =  new RegistrarVentaDomainDTO();
		request.setCantidad(cantidad);
		request.setMailComprador(email_comprador);
		request.setProductoId(codigoP1);
		assertThrows(ResponseStatusException.class, () -> { ventaService.procesarVenta(request); });


	}
	
	@Test
	public void procesarVentaProductoNoEncontrado() throws UsuarioNoEncontradoException, ProductoInexistenteException, ValidacionException {
		
		int cantidad = 10;

		when(usuarioClient.obtenerUsuario(email_comprador)).thenReturn(new UsuarioResponseDTO());
		
		doThrow(new ProductoInexistenteException()).when(productoService).obtenerProductoByID(codigoP1);
		
		RegistrarVentaDomainDTO request =  new RegistrarVentaDomainDTO();
		request.setCantidad(cantidad);
		request.setMailComprador(email_comprador);
		request.setProductoId(codigoP1);
		assertThrows(ProductoInexistenteException.class, () -> { ventaService.procesarVenta(request); });


	}

	public UsuarioCliente getUsuarioClient() {
		return usuarioClient;
	}

	public void setUsuarioClient(UsuarioCliente usuarioClient) {
		this.usuarioClient = usuarioClient;
	}

	public NotificacionCliente getNotificacionClient() {
		return notificacionClient;
	}

	public void setNotificacionClient(NotificacionCliente notificacionClient) {
		this.notificacionClient = notificacionClient;
	}

	public ArgumentCaptor<NotificacionRequestDTO> getVentaCompradorCaptor() {
		return ventaCompradorCaptor;
	}

	public void setVentaCompradorCaptor(ArgumentCaptor<NotificacionRequestDTO> ventaCompradorCaptor) {
		this.ventaCompradorCaptor = ventaCompradorCaptor;
	}

	public ArgumentCaptor<NotificacionRequestDTO> getVentaVendedorCaptor() {
		return ventaVendedorCaptor;
	}

	public void setVentaVendedorCaptor(ArgumentCaptor<NotificacionRequestDTO> ventaVendedorCaptor) {
		this.ventaVendedorCaptor = ventaVendedorCaptor;
	}
	

}
