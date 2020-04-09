package com.xml_rent_a_car.service.certificates;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;

import com.xml_rent_a_car.model.dto.SubjectDataDTO;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class CertificateService {

    public CertificateService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData){
        try {
            //Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
            //Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            //Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            //Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
            builder = builder.setProvider("BC");

            //Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            //Postavljaju se podaci za generisanje sertifiakta
            BigInteger serialNumber = new BigInteger(1, getSerialNumber());
            X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuerData.getX500Name(),
                    serialNumber,
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500Name(),
                    SubjectPublicKeyInfo.getInstance(subjectData.getPublicKey().getEncoded()));
            //Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            //Builder generise sertifikat kao objekat klase X509CertificateHolder
            //Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            //Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getSerialNumber() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("Windows-PRNG");
        } catch (NoSuchAlgorithmException e) {
            random = new SecureRandom();
        }
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return bytes;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Funkcija za kreiranje podataka o onome kome se izdaje sertifikat
    public SubjectData generateSubjectData(SubjectDataDTO subjectDataDTO) throws ParseException {

        //TODO 1 Napraviti da pocetni datum bude u trenutku kreiranja a krajnji +8 godina
        KeyPair keyPairSubject = generateKeyPair();
        SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = iso8601Formater.parse("2017-12-31");
        Date endDate = iso8601Formater.parse("2022-12-31");
        String sn="1";
        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, subjectDataDTO.getCn());
        builder.addRDN(BCStyle.SURNAME, subjectDataDTO.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, subjectDataDTO.getGivenname());
        builder.addRDN(BCStyle.O, subjectDataDTO.getO());
        builder.addRDN(BCStyle.OU, subjectDataDTO.getOu());
        builder.addRDN(BCStyle.C, subjectDataDTO.getC());
        builder.addRDN(BCStyle.E, subjectDataDTO.getE());
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "123456");

        //Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
    }

    //U ovoj funkciji su hardkodovani podaci za IssuerData
    //U trenutku kada bude realizovano cuvanje i ucitavanje sertifikata imacemo id-jeve svih vlasnika sertifikata
    //i moci cemo da koristimo njihove podatke i da kreiramo issuere
    //TODO 2 Posle realizacije cuvanja i ucitavanja prepraviti funkciju tako da se sa fronta dobija id postojeceg sertifikata i taj sertifikat koristi za issuer-a
    public IssuerData generateIssuerData(PrivateKey issuerKey){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Nikola Luburic");
        builder.addRDN(BCStyle.SURNAME, "Luburic");
        builder.addRDN(BCStyle.GIVENNAME, "Nikola");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "654321");

        return new IssuerData(builder.build(), issuerKey);
    }
}
