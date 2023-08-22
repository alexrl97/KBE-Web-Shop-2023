package com.kbe.web_shop.dto.user;

import com.kbe.web_shop.config.constants.Role;

public class SignInResponseDto {

    private String status;
    private String token;

    private Role role;

    public SignInResponseDto(String status, String token, Role role) {
        this.status = status;
        this.token = token;
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
