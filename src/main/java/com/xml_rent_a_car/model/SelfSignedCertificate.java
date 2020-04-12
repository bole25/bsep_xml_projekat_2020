package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;
import com.xml_rent_a_car.model.enumeration.CertificateEnum;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class SelfSignedCertificate extends Certificate {

    @OneToMany(fetch = FetchType.LAZY)
    private Set<IntermediateCertificate> children;

    public SelfSignedCertificate(String parentAlias, CertificateEnum type, Boolean valid, String alias) {
        super(parentAlias,type,valid,alias);
        this.children = new HashSet<>();
    }

    public SelfSignedCertificate() {
        super();
        this.children = new HashSet<>();
    }

    public Set<IntermediateCertificate> getChildren() {
        return children;
    }

    public void setChildren(Set<IntermediateCertificate> children) {
        this.children = children;
    }

    //Funkcija koja vraca true ukoliko je uspjesno dodat child ili vraca false ako je doslo do greske
    public Boolean addChild(IntermediateCertificate child) {
       try {
           this.children.add(child);
           return Boolean.TRUE;
       }
       catch (Exception e) {
           return Boolean.FALSE;
       }
    }
    //Funkcija koja brise i vraca true ako je uspjesno obrisan child ili vraca false ako je doslo do greske
    public Boolean removeChild(IntermediateCertificate child){
        try {
            this.children.remove(child);
            return Boolean.TRUE;
        }
        catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
