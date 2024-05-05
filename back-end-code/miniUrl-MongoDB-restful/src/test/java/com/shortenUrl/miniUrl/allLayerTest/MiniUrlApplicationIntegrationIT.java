package com.shortenUrl.miniUrl.allLayerTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.repository.ShortUrlDataRepository;
import com.shortenUrl.miniUrl.repository.UserRepository;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MiniUrlApplicationIntegrationIT 
{


	@Autowired
	private WebTestClient webClient;

    @Autowired
    private ShortUrlDataRepository urlDB;

    @Autowired
    private UserRepository userDB;


	@Test
	public void verifyGetHomePage() 
    {
		webClient.get().uri( "/")
		.exchange()
		.expectStatus().isOk();

	}

    @Test
	public void verifyGetLoginPage() 
    {
		webClient.get().uri( "/login")
		.exchange()
		.expectStatus().isOk();

	}

    @Test
	public void verifyGetRegisterPage() 
    {
		webClient.get().uri( "/registration")
		.exchange()
		.expectStatus().isOk();

	}

    @Test
	public void verifyLogoutGetRedirect() 
    {
		webClient.post().uri( "/logout")
		.exchange()
		.expectStatus().is3xxRedirection();

	}


    @Test
    void verifySubmitVliadUrlToShortenOKAndDataSaveIntoDb()
    {


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("longUrl", "www.testlonglong.com");

        webClient.post().uri( "/shortUrl")
        .bodyValue(formData)
		.exchange()
		.expectStatus().isOk();


        List<ShortUrlData> objectList = urlDB.findByLongUrl("www.testlonglong.com");

        assertTrue(objectList.size() > 0);

    }


    @Test
    void verifySubmitValidFormForRegisterUserReturnOKAndSaveDataInDb()
    {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userName", "test");
        formData.add("password", "test");
        formData.add("confirmPassword", "test");

        webClient.post().uri( "/registration")
        .bodyValue(formData)
		.exchange()
		.expectStatus().isOk();


        RegisterUser user = userDB.findByUserName("test");

        assertTrue(user != null);

    }




}
