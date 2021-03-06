package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;
import com.xml_rent_a_car.model.enumeration.CertificateEnum;
import com.xml_rent_a_car.model.enumeration.TemplateCertificat;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.*;

import java.security.cert.X509Certificate;

@Entity
@Table
@Inheritance(strategy = JOINED)
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String parentAlias;

    @Column
    private CertificateEnum type;

    @Column
    private Boolean valid = Boolean.TRUE;

    @Column(unique = true)
    private String alias;

    @Column
    private TemplateCertificat template;
    
    public Certificate() {

    }

    public Certificate(String parentAlias, CertificateEnum type, Boolean valid, String alias) {
        this.parentAlias = parentAlias;
        this.type = type;
        this.valid = valid;
        this.alias = alias;
        this.template = TemplateCertificat.USER; // let it be default template
    }
    
    public Certificate(String parentAlias, CertificateEnum type, Boolean valid, String alias, TemplateCertificat template) {
        this.parentAlias = parentAlias;
        this.type = type;
        this.valid = valid;
        this.alias = alias;
        this.template = template;
    }
    
    public Certificate(String parentAlias, CertificateEnum type, Boolean valid, String alias, String template) {
        this.parentAlias = parentAlias;
        this.type = type;
        this.valid = valid;
        this.alias = alias;
        if(template.equals("User")) {
        	this.template = TemplateCertificat.USER;
        } else if(template.equals("Computer")) {
        	this.template = TemplateCertificat.COMPUTER;
        } else {
        	this.template = TemplateCertificat.COMPANY;
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentAlias() {
        return parentAlias;
    }

    public void setParentAlias(String parentAlias) {
        this.parentAlias = parentAlias;
    }

    public CertificateEnum getType() {
        return type;
    }

    public void setType(CertificateEnum type) {
        this.type = type;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

	public TemplateCertificat getTemplate() {
		return template;
	}

	public void setTemplate(TemplateCertificat template) {
		this.template = template;
	}
    
}
