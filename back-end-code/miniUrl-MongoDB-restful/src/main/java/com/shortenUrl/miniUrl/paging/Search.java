package com.shortenUrl.miniUrl.paging;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Search {

    private String value;
    private String regexp;
}