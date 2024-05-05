package com.shortenUrl.miniUrl.serviceTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.repository.ShortUrlDataRepository;
import com.shortenUrl.miniUrl.service.ShortUrlService;

import jakarta.servlet.http.HttpServletRequest;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceTest 
{



    @Mock
    private ShortUrlDataRepository urlDB;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ShortUrlService urlService;

    @Test
    void shouldAddOneMoreShortenTimeForExistUrlData()
    {

        List<ShortUrlData> urlDatas = new ArrayList<>();

        ShortUrlData urlData = new ShortUrlData(null, "longlongurl.com", null, 0, 0, null);

        urlDatas.add(urlData);

        Mockito.when(urlDB.findByLongUrl(any(String.class))).thenReturn(urlDatas);

        urlService.processUrlData(urlData, request, urlDB);

        assertTrue(urlData.getShortenTimes() == 1);
    }


    
}
