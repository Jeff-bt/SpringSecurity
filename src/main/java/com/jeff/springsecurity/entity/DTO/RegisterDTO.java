package com.jeff.springsecurity.entity.DTO;

import com.jeff.springsecurity.entity.enums.UserRoleEnum;

public record RegisterDTO(String login, String password, UserRoleEnum role) {
}
