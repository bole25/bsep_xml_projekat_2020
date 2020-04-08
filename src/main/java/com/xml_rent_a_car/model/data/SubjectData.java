package com.xml_rent_a_car.model.data;

import sun.security.x509.X500Name;

import java.security.PublicKey;
import java.util.Date;

public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500Name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;

    public SubjectData() {
    }

    public SubjectData(PublicKey publicKey, X500Name x500Name, String serialNumber, Date startDate, Date endDate) {
        this.publicKey = publicKey;
        this.x500Name = x500Name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public X500Name getX500Name() {
        return x500Name;
    }

    public void setX500Name(X500Name x500Name) {
        this.x500Name = x500Name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
