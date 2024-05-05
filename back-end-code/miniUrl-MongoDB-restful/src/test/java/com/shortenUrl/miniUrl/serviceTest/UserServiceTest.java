package com.shortenUrl.miniUrl.serviceTest;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;
import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.repository.UserRepository;
import com.shortenUrl.miniUrl.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest 
{

    @Mock
    private UserRepository userDB;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccess()
    {
        UserInfoForm userInfo = new UserInfoForm("test", "", "");
         RegisterUser user = new RegisterUser();

        Mockito.when(userDB.findByUserName(any(String.class))).thenReturn(null);
        Mockito.when(userDB.save(any(RegisterUser.class))).thenReturn(user);


        RegisterUser testUser = userService.registerNewUserAccount(userInfo);

        assertTrue(testUser != null);
    }


    @Test
    void shouldRegisterUserFailAlreadyExist()
    {
        UserInfoForm userInfo = new UserInfoForm("test", "", "");
         RegisterUser user = new RegisterUser();

        Mockito.when(userDB.findByUserName(any(String.class))).thenReturn(user);


        RegisterUser testUser = userService.registerNewUserAccount(userInfo);

        assertTrue(testUser == null);
    }

}
