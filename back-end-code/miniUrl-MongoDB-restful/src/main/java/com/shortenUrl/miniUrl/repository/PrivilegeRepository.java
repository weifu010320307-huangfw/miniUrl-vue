package com.shortenUrl.miniUrl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shortenUrl.miniUrl.model.Privilege;

public interface PrivilegeRepository extends MongoRepository<Privilege, String> 
{
    Privilege findByName(String name);
}
