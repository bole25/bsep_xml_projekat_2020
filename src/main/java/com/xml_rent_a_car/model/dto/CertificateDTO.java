package com.xml_rent_a_car.model.dto;

import java.security.cert.X509Certificate;

import com.xml_rent_a_car.model.enumeration.CertificateEnum;

public class CertificateDTO {

	private String issuerName;
	private String subjectName;
	private String alias;
	private String type;
	private String parentAlias;
	
	
	public CertificateDTO() {
		super();
	}

	public CertificateDTO(String issuerName, String subjectName, String alias) {
		super();
		this.issuerName = issuerName;
		this.subjectName = subjectName;
	}
	public CertificateDTO(X509Certificate c, String alias) {
		issuerName = c.getIssuerX500Principal().getName();
		subjectName = c.getSubjectX500Principal().getName();
		this.alias = alias;
	}
	
	public CertificateDTO(String issuerName,String subjectName,String alias,String type,String parentId) {
		this.issuerName = issuerName;
		this.subjectName = subjectName;
		this.alias = alias;
		this.type = type;
		this.parentAlias = parentId;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentAlias() {
		return parentAlias;
	}

	public void setParentAlias(String parentAlias) {
		this.parentAlias = parentAlias;
	}

	
	
	
}
