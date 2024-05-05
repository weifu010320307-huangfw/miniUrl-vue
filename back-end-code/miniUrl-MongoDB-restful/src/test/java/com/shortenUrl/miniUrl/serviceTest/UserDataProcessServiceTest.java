package com.shortenUrl.miniUrl.serviceTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.paging.Page;
import com.shortenUrl.miniUrl.paging.PagingRequest;
import com.shortenUrl.miniUrl.paging.Search;
import com.shortenUrl.miniUrl.service.UserDataProcessService;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserDataProcessServiceTest 
{
    @InjectMocks
    private UserDataProcessService pagingService;

    @Mock
    private PagingRequest paging;



    private List<ShortUrlData> urlDataList;



    @BeforeAll
    void initializeData()
    {

        ShortUrlData testData1 = new ShortUrlData("test1", "", "", 0, 0, "test1");
        ShortUrlData testData2 = new ShortUrlData("test2", "", "", 0, 0, "test2");
        urlDataList = List.of(testData1, testData2);

    }

    @Test
    void shouldReturnExpectItemCount()
    {


        Page<ShortUrlData> pageData = pagingService.getPage(urlDataList, paging);

        assertTrue(pageData.getRecordsTotal() == 2);
    }

    @Test
    void shouldReturnExpectSearchData()
    {


        Search search = new Search();
        search.setValue("test2");

        PagingRequest paging = new PagingRequest();
        paging.setSearch(search);

        Page<ShortUrlData> pageData = pagingService.getPage(urlDataList, paging);


        assertTrue(pageData.getRecordsFiltered() == 1);

    }
    
}
