package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.enumeration.CertificateEnum;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class EndEntityCertificate extends Certificate {

    public EndEntityCertificate() {
    }

    public EndEntityCertificate(String parentAlias, CertificateEnum type, Boolean valid, String alias) {
        super(parentAlias, type, valid, alias);
    }
}
