package com.shortenUrl.miniUrl.repositoryTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.shortenUrl.miniUrl.configuration.MongoConfig;
import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.repository.UserRepository;

@DataMongoTest
public class UserRepositoryIT 
{

    @Autowired
    UserRepository userRepository;


    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    // @Transactional
    void whenSaved_thenFindsByUserName() throws Exception
    {
        // the data save into Db doesn't get rollback
        // @transactional only support on relica set
        userRepository.save(new RegisterUser(
                "test",
                "test", null, false, false, null));

        assertTrue(userRepository.findByUserName("test") != null);
    }
    
}
