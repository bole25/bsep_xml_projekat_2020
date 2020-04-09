package com.xml_rent_a_car.model.dto;

public class SubjectDataDTO {
    private String cn;
    private String surname;
    private String givenname;
    private String o;
    private String ou;
    private String c;
    private String e;

    public SubjectDataDTO() {
    }

    public SubjectDataDTO(String cn, String surname, String givenname, String o, String ou, String c, String e) {
        this.cn = cn;
        this.surname = surname;
        this.givenname = givenname;
        this.o = o;
        this.ou = ou;
        this.c = c;
        this.e = e;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "SubjectDataDTO{" +
                "cn='" + cn + '\'' +
                ", surname='" + surname + '\'' +
                ", givenname='" + givenname + '\'' +
                ", o='" + o + '\'' +
                ", ou='" + ou + '\'' +
                ", c='" + c + '\'' +
                ", e='" + e + '\'' +
                '}';
    }
}
