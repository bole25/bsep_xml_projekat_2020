package com.xml_rent_a_car.controller.certificates;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;
import com.xml_rent_a_car.model.dto.CertificateDTO;
import com.xml_rent_a_car.model.dto.SubjectDataDTO;
import com.xml_rent_a_car.service.certificates.CertificateService;
import com.xml_rent_a_car.service.certificates.KeyStoreFileService;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private KeyStoreFileService keyStoreFileService;
    
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
            //System.out.println(cert.getSubjectX500Principal().getName());
            System.out.println("\n===== Sertifikat =====");
            System.out.println("-------------------------------------------------------");
            System.out.println(cert);
            System.out.println("-------------------------------------------------------");

            //TODO NEMANJA Uraditi cuvanje sertifikata u keystore u zavisnosti koji se pravi
            //password za sve keystore je keystore malim slovima
            //TODO NEMANJA Uraditi da kad se napravi certifikat, da se napravi i fajl alias.cer na nekoj putanji

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/showAll")
    public ResponseEntity<Set<CertificateDTO>> showAll(){
    	//TO DO treba pozvati metodu da vrati sve sertifikate, ovde samo radi provere vraca listu stringova
        //TO DO treba i na frontu izmjeniti u for petlji i u export klasi promeniti tip string na certifikat
    	Set<CertificateDTO> certTemp = new HashSet<CertificateDTO>();
		try {
			certTemp = keyStoreFileService.getAllCertificates();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return new ResponseEntity<>(certTemp, HttpStatus.OK);
    }
}
