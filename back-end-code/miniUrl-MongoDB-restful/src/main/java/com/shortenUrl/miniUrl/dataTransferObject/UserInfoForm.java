package com.shortenUrl.miniUrl.dataTransferObject;

import com.shortenUrl.miniUrl.validation.PasswordMatches;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@PasswordMatches
@ToString
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class UserInfoForm 
{
    @NotNull
    @NotEmpty
    private String userName;
    

    
    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String confirmPassword;
    

}
