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
public class IntermediateCertificate extends Certificate {

    @OneToMany(fetch = FetchType.LAZY)
    private Set<IntermediateCertificate> childrenIntermediate;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<EndEntityCertificate> childrenEndEntity;

    public IntermediateCertificate(String parentAlias, CertificateEnum type, Boolean valid, String alias) {
        super(parentAlias,type,valid,alias);
        this.childrenIntermediate = new HashSet<>();
        this.childrenEndEntity = new HashSet<>();
    }

    public IntermediateCertificate() {
        super();
        this.childrenEndEntity = new HashSet<>();
        this.childrenIntermediate = new HashSet<>();
    }

    public Set<IntermediateCertificate> getChildrenIntermediate() {
        return childrenIntermediate;
    }

    public void setChildrenIntermediate(Set<IntermediateCertificate> childrenIntermediate) {
        this.childrenIntermediate = childrenIntermediate;
    }

    public Set<EndEntityCertificate> getChildrenEndEntity() {
        return childrenEndEntity;
    }

    public void setChildrenEndEntity(Set<EndEntityCertificate> childrenEndEntity) {
        this.childrenEndEntity = childrenEndEntity;
    }
}
