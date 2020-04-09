package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

public class RootCertificate extends Certificate {
    private Set<Certificate> children;

    public RootCertificate(String parentID, Boolean valid, SubjectData subjectData, IssuerData issuerData, X509Certificate x509Certificate) {
        super(parentID, valid, subjectData, issuerData, x509Certificate);
        this.children = new HashSet<>();
    }

    public RootCertificate() {
        super();
        this.children = new HashSet<>();
    }

    public Set<Certificate> getChildren() {
        return children;
    }

    public void setChildren(Set<Certificate> children) {
        this.children = children;
    }

    //Funkcija koja vraca true ukoliko je uspjesno dodat child ili vraca false ako je doslo do greske
    public Boolean addChild(Certificate child) {
       try {
           this.children.add(child);
           return Boolean.TRUE;
       }
       catch (Exception e) {
           return Boolean.FALSE;
       }
    }
    //Funkcija koja brise i vraca true ako je uspjesno obrisan child ili vraca false ako je doslo do greske
    public Boolean removeChild(Certificate child){
        try {
            this.children.remove(child);
            return Boolean.TRUE;
        }
        catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
