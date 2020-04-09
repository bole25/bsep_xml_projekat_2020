package com.xml_rent_a_car.controller.certificates;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;
import com.xml_rent_a_car.model.dto.SubjectDataDTO;
import com.xml_rent_a_car.service.certificates.CertificateService;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/create")
    public ResponseEntity<String> createCertificate(@RequestBody SubjectDataDTO subjectDataDTO){

        try {
            KeyPair keyPairIssuer = certificateService.generateKeyPair();
            SubjectData subjectData = certificateService.generateSubjectData(subjectDataDTO);
            IssuerData issuerData = certificateService.generateIssuerData(keyPairIssuer.getPrivate());
            X509Certificate cert = certificateService.generateCertificate(subjectData, issuerData);
            System.out.println("\n===== Podaci o izdavacu sertifikata =====");
            System.out.println(cert.getIssuerX500Principal().getName());
            System.out.println("\n===== Podaci o vlasniku sertifikata =====");
            System.out.println(cert.getSubjectX500Principal().getName());
            System.out.println("\n===== Sertifikat =====");
            System.out.println("-------------------------------------------------------");
            System.out.println(cert);
            System.out.println("-------------------------------------------------------");

            //TODO 3 Uraditi cuvanje sertifikata u keystore

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
