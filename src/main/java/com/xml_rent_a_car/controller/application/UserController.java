package com.xml_rent_a_car.controller.application;

import com.xml_rent_a_car.model.authentication.AuthenticationRequest;
import com.xml_rent_a_car.model.authentication.AuthenticationResponse;
import com.xml_rent_a_car.service.application.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    LoginService loginService;

    public UserController(){

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest auth_req){
        return loginService.login(auth_req);
    }
}
