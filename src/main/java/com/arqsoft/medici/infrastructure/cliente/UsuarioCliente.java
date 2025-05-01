package com.arqsoft.medici.infrastructure.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "usuario-client", url = "http://localhost:8085")
//@RequestMapping("/usuario")
public interface UsuarioCliente {
	
	 @GetMapping(path = "/usuario/{email}", 
	 produces = MediaType.APPLICATION_JSON_VALUE)
	 public UsuarioResponseDTO obtenerUsuario(@PathVariable(value = "email") String mail);

}
