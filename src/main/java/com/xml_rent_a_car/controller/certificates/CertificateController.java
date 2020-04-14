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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xml_rent_a_car.model.Certificate;
import com.xml_rent_a_car.model.EndEntityCertificate;
import com.xml_rent_a_car.model.IntermediateCertificate;
import com.xml_rent_a_car.model.SelfSignedCertificate;
import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.data.SubjectData;
import com.xml_rent_a_car.model.dto.CertificateDTO;
import com.xml_rent_a_car.model.dto.SubjectDataDTO;
import com.xml_rent_a_car.model.enumeration.CertificateEnum;
import com.xml_rent_a_car.model.enumeration.TemplateCertificat;
import com.xml_rent_a_car.repository.CertificateRepository;
import com.xml_rent_a_car.repository.EndEntityCertificateRepository;
import com.xml_rent_a_car.repository.IntermediateCertificateRepository;
import com.xml_rent_a_car.repository.SelfSignedCertificateRepository;
import com.xml_rent_a_car.service.certificates.CertificateService;
import com.xml_rent_a_car.service.certificates.KeyStoreFileService;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private KeyStoreFileService keyStoreFileService;

    @Autowired
    private SelfSignedCertificateRepository selfSignedCertificateRepository;

    @Autowired
    private EndEntityCertificateRepository endEntityCertificateRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private IntermediateCertificateRepository intermediateCertificateRepository;

    public CertificateController() {
    }

    @PostMapping("/create/{certificateType}/{parent}/{pomTemplate}")
    public ResponseEntity<String> createCertificate(@RequestBody SubjectDataDTO subjectDataDTO, @PathVariable String certificateType, @PathVariable String parent, @PathVariable String pomTemplate){
    	
    	TemplateCertificat template = TemplateCertificat.USER;
    	if(pomTemplate.equals("Computer")) {
    		template = TemplateCertificat.COMPUTER;
    	} else if(pomTemplate.equals("Company")) {
    		template = TemplateCertificat.COMPANY;
    	}
    	
        try {
            KeyPair keyPairIssuer = certificateService.generateKeyPair();
            SubjectData subjectData = certificateService.generateSubjectData(subjectDataDTO);

            if(certificateType.equals("selfsigned")){
                IssuerData issuerData = certificateService.generateIssuerData(keyPairIssuer.getPrivate(), subjectDataDTO);
                X509Certificate cert = certificateService.generateCertificate(subjectData, issuerData);
                String alias = keyStoreFileService.generateAlias();
                keyStoreFileService.writeCertificateToKS("rootCertificateKS.jks", alias, keyPairIssuer.getPrivate(), "keystore".toCharArray(), cert);
                SelfSignedCertificate sc = new SelfSignedCertificate(alias, CertificateEnum.SELF_SIGNED, Boolean.TRUE, alias);
                // setujem na prosledjeni template
                sc.setTemplate(template);
                selfSignedCertificateRepository.save(sc);
                return new ResponseEntity<>("", HttpStatus.OK);
            }
            else if (certificateType.equals("intermediate")){
                Boolean interm = null;
                try {
                    Certificate c = certificateRepository.getByAlias(parent);
                    if (c.getType().equals(CertificateEnum.INTERMEDIATE)){
                        interm = Boolean.TRUE;
                    }
                    else {
                        interm = Boolean.FALSE;
                    }
                }
                catch (Exception e){

                }
                try {
                    if(interm) {
                        IssuerData issuerData = keyStoreFileService.readIssuerFromStore("immediateCertificateJKS.jks", parent, "keystore".toCharArray(), "keystore".toCharArray());
                        X509Certificate cert = certificateService.generateCertificate(subjectData, issuerData);
                        String alias = keyStoreFileService.generateAlias();
                        keyStoreFileService.writeCertificateToKS("immediateCertificateKS.jks", alias, keyPairIssuer.getPrivate(), "keystore".toCharArray(), cert);
                        IntermediateCertificate ic = new IntermediateCertificate(parent, CertificateEnum.INTERMEDIATE, Boolean.TRUE, alias);
                        IntermediateCertificate par = (IntermediateCertificate) certificateRepository.getByAlias(parent);
                        intermediateCertificateRepository.save(ic);
                        par.getChildrenIntermediate().add(ic);
                        par.setTemplate(template);
                        intermediateCertificateRepository.save(par);
                        return new ResponseEntity<>("", HttpStatus.OK);
                    }
                    else {
                        IssuerData issuerData = keyStoreFileService.readIssuerFromStore("rootCertificateKS.jks", parent, "keystore".toCharArray(), "keystore".toCharArray());
                        X509Certificate cert = certificateService.generateCertificate(subjectData, issuerData);
                        String alias = keyStoreFileService.generateAlias();
                        keyStoreFileService.writeCertificateToKS("immediateCertificateKS.jks", alias, keyPairIssuer.getPrivate(), "keystore".toCharArray(), cert);
                        IntermediateCertificate ic = new IntermediateCertificate(parent, CertificateEnum.INTERMEDIATE, Boolean.TRUE, alias);
                        SelfSignedCertificate par = (SelfSignedCertificate) certificateRepository.getByAlias(parent);
                        intermediateCertificateRepository.save(ic);
                        par.getChildren().add(ic);
                        par.setTemplate(template);
                        selfSignedCertificateRepository.save(par);
                        return new ResponseEntity<>("", HttpStatus.OK);

                    }
                } catch (Exception e ){

                }

            }
            else if (certificateType.equals("endentity")){
                IssuerData issuerData = keyStoreFileService.readIssuerFromStore("immediateCertificateKS.jks", parent, "keystore".toCharArray(), "keystore".toCharArray());
                X509Certificate cert = certificateService.generateCertificate(subjectData, issuerData);
                String alias = keyStoreFileService.generateAlias();
                keyStoreFileService.writeCertificateToKS("endEntityCertificateKS.jks", alias, keyPairIssuer.getPrivate(), "keystore".toCharArray(), cert);
                EndEntityCertificate eec = new EndEntityCertificate(parent, CertificateEnum.END_ENTITY, Boolean.TRUE, alias);
                IntermediateCertificate par = (IntermediateCertificate) certificateRepository.getByAlias(parent);
                endEntityCertificateRepository.save(eec);
                par.setTemplate(template);
                par.getChildrenEndEntity().add(eec);
                intermediateCertificateRepository.save(par);
                return new ResponseEntity<>("", HttpStatus.OK);

            }

            else {

            }

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

    @GetMapping("/getValid/{type}")
    public ResponseEntity<Set<CertificateDTO>> getValid(@PathVariable String type) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        try {

        Set<CertificateDTO> temp;
        temp = keyStoreFileService.getValidCertificatesByFileName(type);
        Set<CertificateDTO> ret = new HashSet<>();
        for(CertificateDTO c : temp) {
            try{
            Certificate certificate = certificateRepository.getByAlias(c.getAlias());
            if(certificate.getValid()){
                ret.add(c);
            }
            } catch (Exception e) {

            }

        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @PostMapping("/unvalidate")
    public ResponseEntity<String> unvalidateCertificate(@RequestBody String alias) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{

    	System.out.println("Pozvana je funkcija za devalidaciju");
    	// odkomentarisi kad odbagujes zasto front ne pogadja back
    	
    	Certificate certificate = certificateRepository.getByAlias(alias);
    	
    	certificate.setValid(false);
    	if( !(certificate instanceof EndEntityCertificate)) {
    		unvalidateCertificatesChildren(certificate);
    	} else {
    		endEntityCertificateRepository.save((EndEntityCertificate)certificate);
    	}
        
    	return new ResponseEntity<>("", HttpStatus.OK);
    	
    }
    
    private void unvalidateCertificatesChildren(Certificate certificate) {
 	   	certificate.setValid(false);
    	if(certificate instanceof IntermediateCertificate) {
    		

    		IntermediateCertificate intCer = (IntermediateCertificate)certificate;
    		Set<IntermediateCertificate> childrenIntermediat = intCer.getChildrenIntermediate();
    		Set<EndEntityCertificate> childrenEndEntity = intCer.getChildrenEndEntity();
    		
    		// unvalidation of children certificates
    		for(Certificate c : childrenIntermediat) {
    			unvalidateCertificatesChildren(c);
    		}
    		// end-entity certificates have no children
    		for(EndEntityCertificate c : childrenEndEntity) {
    			c.setValid(false);
    			endEntityCertificateRepository.save(c);
    		}
            intermediateCertificateRepository.save(intCer);
    		
    	} else if(certificate instanceof SelfSignedCertificate) {
    		
    		SelfSignedCertificate selfSifnedCer = (SelfSignedCertificate)certificate;
    		Set<IntermediateCertificate> childrenIntermediat = selfSifnedCer.getChildren();

    		/// unvalidation of children certificates
    		for(Certificate c : childrenIntermediat) {
    			unvalidateCertificatesChildren(c);
    		}
    		
    		selfSignedCertificateRepository.save(selfSifnedCer);
    	
    	}
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
