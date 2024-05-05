package com.shortenUrl.miniUrl.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unbescape.html.HtmlEscape;

import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;
import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.Role;
import com.shortenUrl.miniUrl.repository.UserRepository;
import com.shortenUrl.miniUrl.utils.UserAlreadyExistException;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService 
{
    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;
    
    @Override
    public RegisterUser registerNewUserAccount(UserInfoForm userInfo)
    {



        System.out.println("registerNewUserAccount " + userInfo);

        try 
        {
            RegisterUser user = userRepository.findByUserName(userInfo.getUserName());
            if (user != null) 
            {
                 String msg = "Username already exist, try different one: " + userInfo.getUserName();
                throw new UserAlreadyExistException(msg);
                
            }

        } 
        catch (UserAlreadyExistException e) 
        {
            System.out.println("UserAlreadyExistException " + e.getMessage());
            return null;
        }
        

        // escaping username
        String username = userInfo.getUserName();

        username = HtmlEscape.escapeHtml5(username);


      
        RegisterUser newUser = new RegisterUser();

        newUser.setUserName(username);
        newUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        newUser.setEnabled(true);
        newUser.setRoles(Arrays.asList(new Role("ROLE_USER")));


        System.out.println("UserService before saving, in registerNewUserAccount..");
        return userRepository.save(newUser);


      
    }





}