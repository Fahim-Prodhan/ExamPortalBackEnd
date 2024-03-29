package com.exam.controller;

import com.exam.config.JwtUtils;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.model.User;
import com.exam.service.impl.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private JwtUtils jwtUtils;


    //generate token

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        try{

            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        }catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("User not Found");
        }

        //authenticate

        UserDetails userDetails= this.userDetailsServiceImp.loadUserByUsername(jwtRequest.getUsername());
        String token=this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));

    }


    private void authenticate(String username, String password) throws Exception {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        }catch (DisabledException e) {
            throw new Exception("User Disabled" + e.getMessage());
        }catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials" + e.getMessage());
        }
    }


    //return the current user detail
    @GetMapping("/current-user")
    public User getCurrentUser(Principal principal) {

    return ((User)this.userDetailsServiceImp.loadUserByUsername(principal.getName()));

    }

}
