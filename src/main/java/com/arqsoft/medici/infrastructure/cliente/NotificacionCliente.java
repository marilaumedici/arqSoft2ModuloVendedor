package com.arqsoft.medici.infrastructure.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "notificacion-client", url = "http://localhost:8090") 
public interface NotificacionCliente {

    @PostMapping(path = "/notificacion/vendedor", 
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendNotificacionVendedor(@RequestBody NotificacionRequestDTO notificacionDTO);
    
    @PostMapping(path = "/notificacion/usuario", 
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendNotificacionUsuario(@RequestBody NotificacionRequestDTO notificacionDTO);
}