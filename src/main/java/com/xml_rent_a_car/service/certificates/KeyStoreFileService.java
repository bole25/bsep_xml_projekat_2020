package com.xml_rent_a_car.service.certificates;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;


import com.xml_rent_a_car.model.data.IssuerData;
import com.xml_rent_a_car.model.dto.CertificateDTO;

import java.io.FileOutputStream;



@Service
public class KeyStoreFileService {

	public KeyStore keyStore;
	
	public KeyStoreFileService() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
	 * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
	 * 
	 * @param keyStoreFile - datoteka odakle se citaju podaci
	 * @param alias - alias putem kog se identifikuje sertifikat izdavaoca
	 * @param password - lozinka koja je neophodna da se otvori key store
	 * @param keyPass - lozinka koja je neophodna da se izvuce privatni kljuc
	 * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
	 */
	public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
		try {
			//Datoteka se ucitava
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password);
			//Iscitava se sertifikat koji ima dati alias
			Certificate cert = keyStore.getCertificate(alias);
			//Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData(issuerName,privKey);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Ucitava sertifikat is KS fajla
	 */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Ucitava privatni kljuc is KS fajla
	 */
	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void loadKeyStore(String fileName, char[] password) {
		try {
			if(fileName != null) {
				keyStore.load(new FileInputStream(fileName), password);
			} else {
				//Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
				keyStore.load(null, password);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveKeyStore(String fileName, char[] password) {
		try {
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
	public Set<CertificateDTO> getAllCertificates() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		Set<CertificateDTO> certificates = new HashSet<CertificateDTO>();
			 
			 KeyStore keyStore = KeyStore.getInstance("JKS");
			 
			 //----first keystore file
			 keyStore.load(new FileInputStream("immediateCertificateKS.jks"), "keystore".toCharArray());
			 
			 // iterate over all aliases
			 Enumeration<String> es = keyStore.aliases();
			 String alias = "";
			 while(es.hasMoreElements()) {
				 alias = (String) es.nextElement();
				 Boolean hasAlias = keyStore.isKeyEntry(alias);
				 if(hasAlias) {
					 Certificate cert = readCertificate("immediateCertificateKS.jks","keystore", alias);
					 if(cert instanceof X509Certificate) {
						 X509Certificate cert509 = (X509Certificate) cert;
						 String s = cert509.getIssuerX500Principal().getName();
						 certificates.add(new CertificateDTO(cert509));
					 }
				 }
			 }
			 
			 //----second keystore file
			 keyStore.load(new FileInputStream("rootCertificateKS.jks"), "keystore".toCharArray());
			 
			 // iterate over all aliases
			 Enumeration<String> es1 = keyStore.aliases();
			 String alias1 = "";
			 while(es1.hasMoreElements()) {
				 alias1 = (String) es1.nextElement();
				 Boolean hasAlias = keyStore.isKeyEntry(alias1);
				 if(hasAlias) {
					 Certificate cert = readCertificate("rootCertificateKS.jks","keystore", alias1);
					 if(cert instanceof X509Certificate) {
						 X509Certificate cert509 = (X509Certificate) cert;
						 String s = cert509.getIssuerX500Principal().get;
						 certificates.add(new CertificateDTO(cert509));
					 }
				 }
			 }
			 
			 //----third keystore file
			 keyStore.load(new FileInputStream("endEntityCertificateKS.jks"), "keystore".toCharArray());
			 
			 // iterate over all aliases
			 Enumeration<String> es2 = keyStore.aliases();
			 String alias2 = "";
			 while(es2.hasMoreElements()) {
				 alias2 = (String) es2.nextElement();
				 Boolean hasAlias = keyStore.isKeyEntry(alias2);
				 if(hasAlias) {
					 Certificate cert = readCertificate("rootCertificateKS.jks","keystore", alias1);
					 if(cert instanceof X509Certificate) {
						 X509Certificate cert509 = (X509Certificate) cert;
						 String s = cert509.getIssuerX500Principal().getName();
						 certificates.add(new CertificateDTO(cert509));
					 }
				 }
			 }
			 
			 return certificates;
	}
}
