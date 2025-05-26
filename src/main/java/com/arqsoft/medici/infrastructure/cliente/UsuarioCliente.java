package com.arqsoft.medici.infrastructure.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "usuario-client", url = "http://localhost:8085")
public interface UsuarioCliente {
	
	 @GetMapping(path = "/usuario/{email}", 
	 produces = MediaType.APPLICATION_JSON_VALUE)
	 public UsuarioResponseDTO obtenerUsuario(@PathVariable(value = "email") String mail);
	 
	 @PostMapping(path = "/usuario/", 
	 consumes = MediaType.APPLICATION_JSON_VALUE, 
	 produces = MediaType.APPLICATION_JSON_VALUE)
	 public void crearUsuario(@RequestBody UsuarioDTO request);

}
