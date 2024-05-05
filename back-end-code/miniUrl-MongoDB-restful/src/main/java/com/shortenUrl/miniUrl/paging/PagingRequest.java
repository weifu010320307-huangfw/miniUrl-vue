package com.shortenUrl.miniUrl.paging;

import java.util.List;

import lombok.Data;


import lombok.ToString;

@ToString
@Data
public class PagingRequest {

    private int start;
    private int length;
    private int draw;
    private List<Order> order;
    private List<Column> columns;
    private Search search;

}