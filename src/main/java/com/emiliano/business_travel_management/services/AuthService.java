package com.emiliano.business_travel_management.services;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.exceptions.UnauthorizedException;
import com.emiliano.business_travel_management.payload.LoginDTO;
import com.emiliano.business_travel_management.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendentiService dipendentiService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Dipendente found = this.dipendentiService.findByEmail(body.email());

        if (passwordEncoder.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("credenziali errate");
        }
    }
}