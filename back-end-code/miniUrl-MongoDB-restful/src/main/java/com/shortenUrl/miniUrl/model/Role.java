package com.shortenUrl.miniUrl.model;



import java.util.Collection;

import org.springframework.data.annotation.Id;


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
public class Role 
{
    @Id
    public String id;



    private String name;
    private Collection<RegisterUser> users;


    private Collection<Privilege> privileges;

    public Role(String name)
    {

        this.name = name;
    }
    
}
