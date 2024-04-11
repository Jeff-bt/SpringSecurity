package com.jeff.springsecurity.controller;

import com.jeff.springsecurity.entity.DTO.AuthenticationDTO;
import com.jeff.springsecurity.entity.DTO.LoginReponseDTO;
import com.jeff.springsecurity.entity.DTO.RegisterDTO;
import com.jeff.springsecurity.entity.User;
import com.jeff.springsecurity.repository.UserRepository;
import com.jeff.springsecurity.security.SecurityFilter;
import com.jeff.springsecurity.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//Essa class vai criar os endpoints de autenticação
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;


    //Esse endpoint faz a autenticação
    @PostMapping("/login")
    public ResponseEntity<LoginReponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginReponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if(this.repository.findByLogin(data.login()) != null){
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity validate(){
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RegisterDTO response = new RegisterDTO(user.getLogin(), user.getPassword(), user.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        String token = SecurityFilter.recoverToken(request);
        tokenService.addToBlacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
