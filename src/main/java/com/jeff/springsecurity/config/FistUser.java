package com.jeff.springsecurity.config;

import com.jeff.springsecurity.entity.User;
import com.jeff.springsecurity.entity.enums.UserRoleEnum;
import com.jeff.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class FistUser implements CommandLineRunner {

    @Autowired
    UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("1", "Daniel", new BCryptPasswordEncoder().encode("123"), UserRoleEnum.ADMIN);
        repository.save(user);
    }
}
