package com.shortenUrl.miniUrl.paging;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.shortenUrl.miniUrl.model.ShortUrlData;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class UserDataComparator {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<ShortUrlData>> map = new HashMap<>();

    static {
        map.put(new Key("shortUrl", Direction.asc), Comparator.comparing(ShortUrlData::getShortUrl));
        map.put(new Key("shortUrl", Direction.desc), Comparator.comparing(ShortUrlData::getShortUrl)
                                                           .reversed());

        map.put(new Key("longUrl", Direction.asc), Comparator.comparing(ShortUrlData::getLongUrl));
        map.put(new Key("longUrl", Direction.desc), Comparator.comparing(ShortUrlData::getLongUrl)
                                                            .reversed());

        map.put(new Key("shortenTimes", Direction.asc), Comparator.comparing(ShortUrlData::getShortenTimes));
        map.put(new Key("shortenTimes", Direction.desc), Comparator.comparing(ShortUrlData::getShortenTimes)
                                                            .reversed());

        map.put(new Key("accessTimes", Direction.asc), Comparator.comparing(ShortUrlData::getAccessTimes));
        map.put(new Key("accessTimes", Direction.desc), Comparator.comparing(ShortUrlData::getAccessTimes)
                                                             .reversed());

        map.put(new Key("belongUser", Direction.asc), Comparator.comparing(ShortUrlData::getBelongUser));
        map.put(new Key("belongUser", Direction.desc), Comparator.comparing(ShortUrlData::getBelongUser)
                                                             .reversed());
                                                           
    }

    public static Comparator<ShortUrlData> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private UserDataComparator() {
    }
}