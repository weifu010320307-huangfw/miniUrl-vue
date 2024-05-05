package com.shortenUrl.miniUrl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.Role;
import com.shortenUrl.miniUrl.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService 
{
    private final UserRepository userRepository;

    

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
     {
  


        RegisterUser user = userRepository.findByUserName(userName);
        if (user == null) {
            System.out.println("CustomUserDetailsService.loadUserByUsername() No user found with username: " + userName);
            throw new UsernameNotFoundException("No user found with username: " + userName);
        }


        System.out.println("CustomUserDetailsService.loadUserByUsername() get roles: " + user.getRoles());
        
        return new org.springframework.security.core.userdetails.User(
            user.getUserName(), user.getPassword(), user.isEnabled(), true, true, 
            true, getAuthorities(user.getRoles()));
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Role> roles) {
 
        return getGrantedAuthorities(roles);
    }



    private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
