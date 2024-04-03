package com.heatherpiper.model;

import com.heatherpiper.security.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, CredentialsDto represents the data a client must
    pass to the server for a login endpoint, and TokenDto represents the object that's returned from the server
    to the client from a login endpoint.
 */
public class RegisterUserDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    @ValidPassword
    private String password;

    @NotEmpty
    private String confirmPassword;

    @NotEmpty(message = "Please select a role for this user.")
    private String role;

    public RegisterUserDto() {
    }

    public RegisterUserDto(String email, String username, String password, String confirmPassword, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
