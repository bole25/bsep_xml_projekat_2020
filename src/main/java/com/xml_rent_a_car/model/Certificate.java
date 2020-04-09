package com.xml_rent_a_car.model;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;

import java.security.cert.X509Certificate;

public class Certificate {

    private String parentID;
    private Boolean valid = Boolean.TRUE;
    private SubjectData subjectData;
    private IssuerData issuerData;
    private X509Certificate x509Certificate;

    public Certificate() {

    }



    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public SubjectData getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(SubjectData subjectData) {
        this.subjectData = subjectData;
    }

    public IssuerData getIssuerData() {
        return issuerData;
    }

    public void setIssuerData(IssuerData issuerData) {
        this.issuerData = issuerData;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public Certificate(String parentID, Boolean valid, SubjectData subjectData, IssuerData issuerData, X509Certificate x509Certificate) {
        this.parentID = parentID;
        this.valid = valid;
        this.subjectData = subjectData;
        this.issuerData = issuerData;
        this.x509Certificate = x509Certificate;
    }
}
