package com.shortenUrl.miniUrl.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.unbescape.html.HtmlEscape;

import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.repository.ShortUrlDataRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ShortUrlService 
{

    public void generateBelongUserCookie(HttpServletRequest request, HttpServletResponse response)
    {
      Optional<String> belongUserName = readCookie("belongUser", request.getCookies());

      if(request.getUserPrincipal() == null &&  belongUserName.isPresent() == false)
      {

        String uuid = UUID.randomUUID().toString();


        Cookie belongUserCookie = new Cookie("belongUser", uuid);
        belongUserCookie.setMaxAge(10*60);
        response.addCookie(belongUserCookie);
       
      }
    }

    public Optional<String> readCookie(String key, Cookie[] allCookies) 
    {
      
      return allCookies != null ? Arrays.stream(allCookies)
        .filter(c -> key.equals(c.getName()))
        .map(Cookie::getValue)
        .findAny() : null;
    }

    public String getRedirectUrl(HttpServletRequest request, ShortUrlDataRepository repository)
    {

        String longUrl = "";
        String shortUrl = request.getRequestURL().toString();

        List<ShortUrlData> shortUrlList = repository.findByShortUrl(shortUrl);
        if(shortUrlList.size() > 0)
        {
            ShortUrlData existUrl = shortUrlList.get(0);
            longUrl = existUrl.getLongUrl();
            existUrl.setAccessTimes(existUrl.getAccessTimes() + 1);
            repository.save(existUrl);

        }


        return longUrl;

    }

    public String getBelongUser(HttpServletRequest request)
    {
      String belongUser = "";
      if(request.getUserPrincipal() == null)
      {
  
        Optional<String> name = readCookie("belongUser", request.getCookies());
        if(name != null && name.isPresent())
        {
          belongUser = name.get();
        }
        
        
      }
      else
      {
  
        belongUser = request.getUserPrincipal().getName();
      }
  
      return belongUser;
    }

    public String processUrlData(ShortUrlData shortUrlData, HttpServletRequest request, ShortUrlDataRepository repository)
    {
        String shortUrl = "";
        String longUrl = shortUrlData.getLongUrl();

        // escaping text
        longUrl = HtmlEscape.escapeHtml5(longUrl);
        shortUrlData.setLongUrl(longUrl);

        List<ShortUrlData> shortUrlList = repository.findByLongUrl(longUrl);
        if(shortUrlList.size() > 0)
        {
    
          ShortUrlData existUrl = shortUrlList.get(0);
          shortUrl = existUrl.getShortUrl();

          int shortenTimes = existUrl.getShortenTimes() + 1;
          existUrl.setShortenTimes(shortenTimes);
          repository.save(existUrl);

       
          BeanUtils.copyProperties(existUrl, shortUrlData);
          
        


          System.out.println("Long url exist, shortUrl is " + shortUrl);
    
        }
        else
        {
    
          String baseAddress = getBaseAddress(request); 
          shortUrl = baseAddress + "/" + shortenUrl(longUrl);
    
          shortUrlData.setLongUrl(longUrl);
          shortUrlData.setShortUrl(shortUrl);
          shortUrlData.setAccessTimes(0);
          shortUrlData.setShortenTimes(1);

          String belongUserName = getBelongUser(request);

          System.out.println("ShortUrlTool.processUrlData(), belongUserName = " + belongUserName);
          shortUrlData.setBelongUser(belongUserName);
    
          repository.save(shortUrlData);
        }

        

        return shortUrl;
    }
    
    

    public String getBaseAddress(HttpServletRequest request)
    {
        String requestUrl = request.getRequestURL().toString();
        System.out.println("web server: " + requestUrl);
        URI uri;
        String baseAddress = "";
    
        try 
        {
          uri = new URI(requestUrl);
          baseAddress = uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort();
              
        } 
        catch (URISyntaxException e) 
        {
          
          e.printStackTrace();
        }
    
        System.out.println("baseAddress: " + baseAddress);

        return baseAddress;

    }
    
    public String shortenUrl(String longUrl)
    {


        final String urlCode = "abcdefghijklmnopqrstuvwxyz0123456789";

        int urlLength = longUrl.length();

        Random randomNum = new Random();

        


        String shortenResult = "";

        int shortUrlLength = 6;
        for(int i = 0; i < shortUrlLength; i++)
        {
            int selectIndex = randomNum.nextInt(urlLength);

            char tempChar = longUrl.charAt(selectIndex);
            if(Character.isLetterOrDigit(tempChar))
            {

                shortenResult += tempChar;
            }
            else
            {
                selectIndex = randomNum.nextInt(urlCode.length());
                shortenResult += (urlCode.charAt(selectIndex));
            }
            
        }


        return shortenResult;

    }
}
