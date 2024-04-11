package com.jeff.springsecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jeff.springsecurity.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

//Essa class cria o Token
@Service
public class TokenService implements TokenBlacklist {
    @Value("${api.security.token.secret}")
    private String secret;

    private Set<String> blacklist = new HashSet<>();
    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); //cria um algoritmo co base em uma senha chamada secret
            String token = JWT.create() //junto com tudo isso abaixo é criado o Token.
                    .withIssuer("spring-security") //Nome do projeto
                    .withSubject(user.getLogin())  //Pega o login do user
                    .withExpiresAt(genExpirationDate()) //Tempo de expiração do Token
                    .sign(algorithm); //Aqui vai o algoritmo
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("spring-security")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    @Override
    public  void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
