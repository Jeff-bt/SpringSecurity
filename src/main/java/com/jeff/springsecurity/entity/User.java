package com.jeff.springsecurity.entity;

import com.jeff.springsecurity.entity.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String login;

    private String password;

    private UserRoleEnum role;

    public User (String login, String password, UserRoleEnum role){
        this.login = login;
        this.password = password;
        this.role = role;
    }

    //Esse método define qual será as permissões que, com base na role(papel),
    //a instancia dessa classe terá:
    // "Role ADMIN" --> admin e user
    // "Role USER"  --> user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRoleEnum.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return  List.of(
                    new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    //Aqui representa o username, pode ser o email/login
    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //O usuário não está bloqueado
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //A credencial não está expirada
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //O usuário está ativo
    @Override
    public boolean isEnabled() {
        return true;
    }
}
