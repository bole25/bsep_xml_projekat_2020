package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

public class IntermediateCertificate extends Certificate {
    private Set<Certificate> children;

    public IntermediateCertificate(String parentID, Boolean valid, SubjectData subjectData, IssuerData issuerData, X509Certificate x509Certificate) {
        super(parentID, valid, subjectData, issuerData, x509Certificate);
        this.children = new HashSet<>();
    }

    public IntermediateCertificate() {
        super();
        this.children = new HashSet<>();
    }

    public Set<Certificate> getChildren() {
        return children;
    }

    public void setChildren(Set<Certificate> children) {
        this.children = children;
    }
}
