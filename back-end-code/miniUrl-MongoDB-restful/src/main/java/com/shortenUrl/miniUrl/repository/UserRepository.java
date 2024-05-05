package com.shortenUrl.miniUrl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shortenUrl.miniUrl.model.RegisterUser;



public interface UserRepository extends MongoRepository<RegisterUser, String> 
{
    RegisterUser findByUserName(String userName);
    
}
