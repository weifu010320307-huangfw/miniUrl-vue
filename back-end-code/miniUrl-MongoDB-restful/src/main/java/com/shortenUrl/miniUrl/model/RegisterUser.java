package com.shortenUrl.miniUrl.model;

import jakarta.validation.constraints.NotNull;

import java.util.Collection;

import org.springframework.data.annotation.Id;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class RegisterUser 
{

    @Id
    public String id;

    
    @NotNull
    @NotEmpty
    private String userName;
    

    
    @NotNull
    @NotEmpty
    private String password;
    
    private boolean enabled;
    private boolean tokenExpired;


    private Collection<Role> roles;


    
}
