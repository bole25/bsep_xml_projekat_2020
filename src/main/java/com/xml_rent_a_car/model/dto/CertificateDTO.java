package com.xml_rent_a_car.model.dto;

import java.security.cert.X509Certificate;

public class CertificateDTO {

	private String issuerName;
	private String subjectName;
	
	public CertificateDTO() {
		super();
	}

	public CertificateDTO(String issuerName, String subjectName) {
		super();
		this.issuerName = issuerName;
		this.subjectName = subjectName;
	}
	public CertificateDTO(X509Certificate c) {
		issuerName = c.getIssuerX500Principal().getName();
		subjectName = c.getSubjectX500Principal().getName();
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}	
		
}
