package com.arqsoft.medici.domain.dto;

public class VendedorDomainDTO {
	
	public VendedorDomainDTO(String mail, String razonSocial) {
		super();
		this.mail = mail;
		this.razonSocial = razonSocial;
	}
	public VendedorDomainDTO() {

	}
	private String mail;
	private String razonSocial;
	
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


}
