package com.shortenUrl.miniUrl.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shortenUrl.miniUrl.model.ShortUrlData;

public interface ShortUrlDataRepository extends MongoRepository<ShortUrlData, String> 
{

  List<ShortUrlData> findByLongUrl(String longUrl);
  List<ShortUrlData> findByShortUrl(String shortUrl);
  List<ShortUrlData> findByBelongUser(String belongUser);

  ShortUrlData findById(long id);

}
