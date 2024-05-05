package com.shortenUrl.miniUrl.serviceTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.Role;
import com.shortenUrl.miniUrl.repository.UserRepository;
import com.shortenUrl.miniUrl.service.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest 
{
    @Mock
    private UserRepository userDB;

    @InjectMocks
    private CustomUserDetailsService checkUser;

    @Test
    void shouldFoundUserWithRole()
    {
        RegisterUser user = new RegisterUser(null, "test", "", false, false, 
        Set.of(new Role("ROLE_USER")));
        Mockito.when(userDB.findByUserName(any(String.class))).thenReturn(user);


        UserDetails userInfo = checkUser.loadUserByUsername(user.getUserName());

        assertTrue(userInfo != null && userInfo.getAuthorities() != null);

    }
    

    @Test
    void shouldThrowNotFoundUserException()
    {
        RegisterUser user = new RegisterUser();
        Mockito.when(userDB.findByUserName(any(String.class))).thenReturn(null);

        user.setUserName("test");

        assertThrows(UsernameNotFoundException.class, 
        () -> { checkUser.loadUserByUsername(user.getUserName()); });

    }
    
}
