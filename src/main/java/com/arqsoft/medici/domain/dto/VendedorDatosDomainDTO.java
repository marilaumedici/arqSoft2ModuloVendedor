package com.arqsoft.medici.domain.dto;

import com.arqsoft.medici.domain.utils.VendedorEstado;

public class VendedorDatosDomainDTO {
	
	private String mail;
	private String razonSocial;
	private VendedorEstado estado;
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public VendedorEstado getEstado() {
		return estado;
	}
	public void setEstado(VendedorEstado estado) {
		this.estado = estado;
	}

}
