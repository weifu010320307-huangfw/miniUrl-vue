package com.shortenUrl.miniUrl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shortenUrl.miniUrl.model.Role;


public interface RoleRepository extends MongoRepository<Role, String> 
{
    Role findByName(String name);
}
