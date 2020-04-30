package com.xml_rent_a_car.service.application;

import com.xml_rent_a_car.model.MyUserDetails;
import com.xml_rent_a_car.model.User;
import com.xml_rent_a_car.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException("User with the email"+ s + "doesn't exist");
        }

        return new MyUserDetails(user);
    }
}
