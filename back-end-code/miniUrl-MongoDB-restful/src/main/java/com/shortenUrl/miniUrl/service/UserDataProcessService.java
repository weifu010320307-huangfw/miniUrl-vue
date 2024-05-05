package com.shortenUrl.miniUrl.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.paging.Column;
import com.shortenUrl.miniUrl.paging.Order;
import com.shortenUrl.miniUrl.paging.Page;
import com.shortenUrl.miniUrl.paging.PagingRequest;
import com.shortenUrl.miniUrl.paging.UserDataComparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDataProcessService 
{
    private static final Comparator<ShortUrlData> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<ShortUrlData> getPage(List<ShortUrlData> ShortUrlData, PagingRequest pagingRequest) {
        List<ShortUrlData> filtered = ShortUrlData.stream()
                                           .sorted(sortUserData(pagingRequest))
                                           .filter(filterUserData(pagingRequest))
                                           .skip(pagingRequest.getStart())
                                           .limit(pagingRequest.getLength())
                                           .collect(Collectors.toList());

        long count = ShortUrlData.stream()
                              .filter(filterUserData(pagingRequest))
                              .count();

        Page<ShortUrlData> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        log.info("pagingRequest.count = " + count);

        return page;
    }



    private Predicate<ShortUrlData> filterUserData(PagingRequest pagingRequest) 
    {
        

        if (pagingRequest.getSearch() == null || !StringUtils.hasLength(pagingRequest.getSearch()
                                                                                  .getValue())) 
        {
            return userData -> true;
        }

        String value = pagingRequest.getSearch()
                                    .getValue();


        return userData -> userData.getShortUrl()
                                   .toLowerCase()
                                   .contains(value)
                || userData.getLongUrl()
                           .toLowerCase()
                           .contains(value)
                || userData.getAccessTimes()
                        .toString()
                        .contains(value)
                || userData.getShortenTimes()
                           .toString()
                           .contains(value)
                || userData.getBelongUser()
                .toString().toLowerCase()
                .contains(value);

    }

    private Comparator<ShortUrlData> sortUserData(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {

            if(pagingRequest.getOrder().size() == 0 || pagingRequest.getColumns().size() == 0)
            {
                return EMPTY_COMPARATOR;

            }

            Order order = pagingRequest.getOrder()
                                       .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                                         .get(columnIndex);

            Comparator<ShortUrlData> comparator = UserDataComparator.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }
}
