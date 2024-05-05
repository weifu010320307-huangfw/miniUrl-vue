package com.shortenUrl.miniUrl.controllerTest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortenUrl.miniUrl.configuration.ShortUrlWebSecurityConfig;
import com.shortenUrl.miniUrl.controller.ShortenUrlRestfulController;
import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;
import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.repository.ShortUrlDataRepository;
import com.shortenUrl.miniUrl.repository.UserRepository;
import com.shortenUrl.miniUrl.service.ShortUrlService;
import com.shortenUrl.miniUrl.service.UserDataProcessService;
import com.shortenUrl.miniUrl.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(controllers = ShortenUrlRestfulController.class)
@Import(ShortUrlWebSecurityConfig.class)
public class ShortenUrlControllerIT 
{

    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private UserRepository userDB;

    @MockBean
    private ShortUrlDataRepository urlDataDB;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDataProcessService userDataService;
  
    @MockBean
    private ShortUrlService urlService;


    @Test
    void verifySubmitValidFormDataForShortenReturn200() throws Exception
    {



        mockMvc.perform(post("/shortUrl")
        .param("longUrl", "longlongurl.com"))
        .andExpect(status().isOk());
        

    }

    
    @Test
    void verifySubmitInvalidFormDataForShortenReturn400() throws Exception
    {


        mockMvc.perform(post("/shortUrl")
        .param("longUrl", ""))
        .andExpect(status().isBadRequest());
        
    }

    @Test
    void verifySubmitFormDataPassToShortenUrlService() throws Exception
    {



         mockMvc.perform(post("/shortUrl")
        .param("longUrl", "longlongurl.com"))
        .andExpect(status().isOk());

        ArgumentCaptor<ShortUrlData> userCaptor = ArgumentCaptor.forClass(ShortUrlData.class);
        verify(urlService, times(1))
        .processUrlData(userCaptor.capture(), any(HttpServletRequest.class), eq(urlDataDB));
        assertTrue(userCaptor.getValue().getLongUrl() == "longlongurl.com");
        
    }


    @Test
    void verifyRedirectAnonymousUserToLoginWhenAccessUserdata() throws Exception 
    {
        mockMvc
        .perform(get("/userdata"))
        .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "duke")
    void verifyAllowAccessForAuthenticatedUserWhenAccessUserdata() throws Exception 
    {
        
        mockMvc
        .perform(get("/userdata"))
        .andExpect(status().isOk())
        .andExpect(view().name("userdata"));
    }


    @Test
    void verifySubmitValidFormForRegisterUserReturnOK() throws Exception
    {
        mockMvc.perform(post("/registration")
        .param("userName", "test")
        .param("password", "test")
        .param("confirmPassword", "test"))
        .andExpect(status().isOk());
    }


    @Test
    void verifySubmitEmptyPasswordForRegisterUserReturnBadRequest() throws Exception
    {
        mockMvc.perform(post("/registration")
        .param("userName", "test")
        .param("password", "")
        .param("confirmPassword", ""))
        .andExpect(status().isBadRequest());
    }

    @Test
    void verifyCheckUserExistencerReturnYes() throws Exception
    {
        Mockito.when(userDB.findByUserName(any(String.class))).thenReturn(new RegisterUser());

        mockMvc.perform(get("/registration/userStatus")
        .param("name", "test"))
        .andExpect(jsonPath("$.userExist").value("YES"))
        .andExpect(status().isOk());

    }
    
}
