package com.arqsoft.medici.application;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.arqsoft.medici.domain.Venta;
import com.arqsoft.medici.domain.dto.RegistrarVentaDomainDTO;
import com.arqsoft.medici.domain.Producto;
import com.arqsoft.medici.domain.Vendedor;
import com.arqsoft.medici.domain.exceptions.InternalErrorException;
import com.arqsoft.medici.domain.exceptions.ProductoInexistenteException;
import com.arqsoft.medici.domain.exceptions.UsuarioNoEncontradoException;
import com.arqsoft.medici.domain.exceptions.ValidacionException;
import com.arqsoft.medici.infrastructure.cliente.NotificacionCliente;
import com.arqsoft.medici.infrastructure.cliente.NotificacionRequestDTO;
import com.arqsoft.medici.infrastructure.cliente.UsuarioCliente;
import com.arqsoft.medici.infrastructure.cliente.UsuarioResponseDTO;
import com.arqsoft.medici.infrastructure.persistence.VentaRepository;
import io.micrometer.common.util.StringUtils;

@Service
public class VentaServiceImpl  implements VentaService {
	
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private VendedorService vendedorService;
	
	@Autowired
	private VentaRepository ventaRepository;
	
	@Autowired
	private UsuarioCliente usuarioClient;
	
	@Autowired
	private NotificacionCliente notificacionClient;
	
	private ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

	@Override
	public void procesarVenta(RegistrarVentaDomainDTO request) throws InternalErrorException, ValidacionException, ProductoInexistenteException, UsuarioNoEncontradoException {
		
		if(StringUtils.isBlank(request.getProductoId())) {
			throw new InternalErrorException("El id del producto no puede estar vacio.");
			
		}
		if(StringUtils.isBlank(request.getMailComprador())) {
			throw new InternalErrorException("El mail del comprador no puede estar vacio.");
			
		}
		
		//Se llama al componente de usuario para verificar si el usuario ingresado existe
		UsuarioResponseDTO usuario = usuarioClient.obtenerUsuario(request.getMailComprador());
		
		Producto producto = productoService.obtenerProductoByID(request.getProductoId());
		
		Vendedor vendedor = producto.getVendedor();
		
		productoService.descontarStock(producto, request.getCantidad());
		
		Date date = Date.from(zonedDateTime.toInstant());
		
		Venta venta = new Venta(request.getProductoId(), vendedor.getMail(), request.getMailComprador(), date,
				producto.getPrecio(), producto.getPrecio() * request.getCantidad(), request.getCantidad());
		
		ventaRepository.insert(venta);
	
		enviarMailsNotificacion(usuario, producto, vendedor);
	}

	private void enviarMailsNotificacion(UsuarioResponseDTO usuario, Producto producto, Vendedor vendedor) {
		
		try {
			
			// envio notificaciones al vendedor
			NotificacionRequestDTO notificacionVendedorDTO = new NotificacionRequestDTO(); 
			notificacionVendedorDTO.setEmail("marilaumedici@gmail.com");
			notificacionVendedorDTO.setEmail(vendedor.getMail());
			notificacionVendedorDTO.setNombreUsuario(vendedor.getRazonSocial());
			notificacionVendedorDTO.setNombreProducto(producto.getNombre());
			
			notificacionClient.sendNotificacionVendedor(notificacionVendedorDTO);
			
			// envio notificaciones al usuario
			NotificacionRequestDTO notificacionUsuarioDTO = new NotificacionRequestDTO(); 
			notificacionUsuarioDTO.setEmail("marilaumedici@gmail.com");
			notificacionUsuarioDTO.setEmail(usuario.getMail());
			notificacionUsuarioDTO.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
			notificacionUsuarioDTO.setNombreProducto(producto.getNombre());
			
			notificacionClient.sendNotificacionUsuario(notificacionUsuarioDTO);
			
		}catch(Exception e) {
			
		}
	}

	public ProductoService getProductoService() {
		return productoService;
	}

	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}

	public VendedorService getVendedorService() {
		return vendedorService;
	}

	public void setVendedorService(VendedorService vendedorService) {
		this.vendedorService = vendedorService;
	}

	public VentaRepository getVentaRepository() {
		return ventaRepository;
	}

	public void setVentaRepository(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	public void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
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


}
