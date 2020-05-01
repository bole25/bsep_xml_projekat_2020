package com.xml_rent_a_car.util;

import com.xml_rent_a_car.model.MyUserDetails;
import com.xml_rent_a_car.service.application.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try{
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println("TOKEN EXPIRED, class JwtRequestFilter");
            }
        }

        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            MyUserDetails myUserDetails = (MyUserDetails) this.myUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, myUserDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(myUserDetails, username, jwtUtil.getAuthorities(jwt));
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                Collection<GrantedAuthority> col = jwtUtil.getAuthorities(jwt);
                logger.info("user: " + username + " role: " + jwtUtil.getAuthorities(jwt).toArray()[0]);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
