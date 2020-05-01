package com.xml_rent_a_car.service.application;

import com.xml_rent_a_car.model.MyUserDetails;
import com.xml_rent_a_car.model.authentication.AuthenticationRequest;
import com.xml_rent_a_car.model.authentication.AuthenticationResponse;
import com.xml_rent_a_car.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public LoginService() {
    }

    //TODO implementirati kreiranje odgovora pri logovanju
    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest auth_req){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth_req.getUsername(), auth_req.getPassword())
            );
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            System.out.println("bad_credentials");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        MyUserDetails myUD = (MyUserDetails) myUserDetailsService
                .loadUserByUsername(auth_req.getUsername());
        String jwt = jwtUtil.generateToken(myUD);
        return new ResponseEntity<>(
                new AuthenticationResponse(jwt,myUD.getUser()), HttpStatus.OK);
    }
}
