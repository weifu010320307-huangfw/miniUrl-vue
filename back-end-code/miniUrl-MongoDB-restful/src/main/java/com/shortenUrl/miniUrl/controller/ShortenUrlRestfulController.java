
package com.shortenUrl.miniUrl.controller;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;

import org.bson.json.JsonObject;
import org.openqa.selenium.json.Json;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;
import com.shortenUrl.miniUrl.model.RegisterUser;
import com.shortenUrl.miniUrl.model.ShortUrlData;
import com.shortenUrl.miniUrl.paging.Page;
import com.shortenUrl.miniUrl.paging.PagingRequest;
import com.shortenUrl.miniUrl.repository.ShortUrlDataRepository;
import com.shortenUrl.miniUrl.repository.UserRepository;
import com.shortenUrl.miniUrl.service.IUserService;
import com.shortenUrl.miniUrl.service.ShortUrlService;
import com.shortenUrl.miniUrl.service.UserDataProcessService;
import com.shortenUrl.miniUrl.service.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ShortenUrlRestfulController 
{



  // @Autowired
  private final UserRepository userRepository;

  // @Autowired
  private final ShortUrlDataRepository urlDataRepository;

  // @Autowired
  // private IUserService userService;

  private final UserService userService;

  // @Autowired
  private final UserDataProcessService userDataService;

  // @Autowired
  private final ShortUrlService urlService;





  @PreAuthorize("isAuthenticated()")
  @PostMapping("/userdata")
  @ResponseBody
  // public Page<ShortUrlData> listUserData(@RequestBody PagingRequest pagingRequest, HttpServletRequest request)
  public HttpEntity<?> listUserData(@RequestBody PagingRequest pagingRequest, HttpServletRequest request)
  {

    System.out.println("ShortenUrlController.listUserData()");
    String userName = request.getUserPrincipal().getName();
    log.info(userName);




    List<ShortUrlData> urlDatas;

    RegisterUser currentUser = userRepository.findByUserName(userName);
    


    if(currentUser.getRoles().stream().filter(role -> role.getName().equals("ROLE_ADMIN") ).findAny().isPresent())
    {
      
      urlDatas = urlDataRepository.findAll();

    }
    else
    {
      urlDatas = urlDataRepository.findByBelongUser(userName);
    }




    Page<ShortUrlData> userData = userDataService.getPage(urlDatas, pagingRequest);

    log.info("ListUserData = " + userData.toString());

    
    EntityModel<Page<ShortUrlData>> represenntationModel = EntityModel.of(userData);

    represenntationModel.add(linkTo(methodOn(ShortenUrlRestfulController.class)
    .listUserData(null, null)).withSelfRel());

    return new ResponseEntity<>(represenntationModel, HttpStatus.OK);



    
  }


  @PreAuthorize("isAuthenticated()")
  @GetMapping("/userdata")
  public String getUserData()
  {

    return "userdata";
  }
  

  @GetMapping("/login")
  public String login(HttpServletRequest request)
  {


    Cookie[] allCookies = request.getCookies();
    if(allCookies != null)
    {
      for (Cookie cookie : allCookies) 
      {
  
        System.out.println("ShortenUrlController.login(), get cookies " + cookie.getName() + " = " + cookie.getValue());
      }
    }




    return "login";

  }



  @GetMapping("/registration/userStatus")
  @ResponseBody
  public String checkUserExistence(@RequestParam String name) 
  {
    
    System.out.println("ShortenUrlController::checkUserExistence..., the input username is " + name);

  

    String isUserExist = "{\"userExist\" : \"NO\"}";


    RegisterUser user = userRepository.findByUserName(name);
    if(user != null)
    {


      isUserExist = "{\"userExist\" : \"YES\"}";
    }


   return isUserExist;

  }


  @GetMapping("/registration")
  public String registration(Model model, HttpServletRequest request)
  {

    System.out.println("call registration...");  



    UserInfoForm newUser = new UserInfoForm();
    model.addAttribute("userInfo", newUser);

    return "registration";

  }

@PostMapping("/registration")
@ResponseBody

  public HttpEntity<?> registerNewUser(@ModelAttribute("userInfo") @Valid UserInfoForm userInfo,
   BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
   {
    
    


  // soutm
  System.out.println("ShortenUrlController.registerNewUserAccount()");

  
    if(errors.hasErrors())
    {
      System.out.println("registerNewUserAccount return error..");


      errors
      .getFieldErrors()
      .stream()
      .forEach(f -> System.out.println(f.getField() + ": " + f.getDefaultMessage()));

      // password match error is not in field error
      if(errors.getFieldErrors().size() == 0)
      {
        return new ResponseEntity<>(errors.getGlobalErrors(), HttpStatus.BAD_REQUEST);

      }


      return new ResponseEntity<>(errors.getFieldErrors(), HttpStatus.BAD_REQUEST);


    }


    System.out.println("call registerNewUserAccount.., method from request is " + request.getMethod());

    
    String registerResult = "{\"redirectUrl\" : \"registration\"}";
    

    RegisterUser registeredUser = userService.registerNewUserAccount(userInfo);


    if(registeredUser != null)
    {


      try 
      {
        
        request.login(userInfo.getUserName(), userInfo.getPassword());
      } 
      catch (ServletException e) 
      {
        System.out.println("ShortenUrlController.registerNewUserAccount() " + e.getMessage());
      }

      // goto url management page if register success
      registerResult = "{\"redirectUrl\" : \"userdata\"}";

      // update those url short by anonymous user with new registered user name      
      Optional<String> findCookie = urlService.readCookie("belongUser", request.getCookies());

      if(findCookie.isPresent())
      {
        String belongUser = findCookie.get();

        if(belongUser.isEmpty() == false)
        {
          List<ShortUrlData> updateBelongUser = urlDataRepository.findByBelongUser(belongUser);

          for (ShortUrlData userData : updateBelongUser) 
          {
            userData.setBelongUser(userInfo.getUserName());
          }


          urlDataRepository.saveAll(updateBelongUser);


        }
      }


    }
    else
    {
      registerResult = "{\"registerResult\" : \"User name already exist\"}";
    }

    return new ResponseEntity<>(registerResult, HttpStatus.CREATED);
}

  @GetMapping("/loginStatus")
  @ResponseBody
  public String getLoginStatus(HttpServletRequest request, HttpServletResponse response)
  {

    // vue client will get the login state every time its page refresh and at its boot
    // so create the belongUser cookie here if not exist

    urlService.generateBelongUserCookie(request, response);





    String loginStatus = "{\"isLogin\" : \"false\"}";


    if(request.getUserPrincipal() != null)
    {
  
      loginStatus = "{\"isLogin\" : \"true\"}";

    }
    

    System.out.println("ShortenUrlRestfulController.getLoginStatus(): " + loginStatus);




    return loginStatus;

  }

  
  @GetMapping("/roleHierarchy")
  public ModelAndView roleHierarcy() 
  {
      ModelAndView model = new ModelAndView();
      model.addObject("adminMessage","Admin content available");
      model.addObject("staffMessage","Staff content available");
      model.addObject("userMessage","User content available");
      model.setViewName("roleHierarchy");
      return model;
  }



  @GetMapping("/")
  public HttpEntity<?> getAllEndpoint()
  {
    
    // ShortUrlData notUse = new ShortUrlData();
    // EntityModel<ShortUrlData> represenntationModel = EntityModel.of(notUse);

    RepresentationModel represenntationModel = new RepresentationModel();


		represenntationModel.add(linkTo(methodOn(ShortenUrlRestfulController.class)
    .getAllEndpoint()).withSelfRel());

    represenntationModel.add(linkTo(methodOn(ShortenUrlRestfulController.class)
    .createShortUrl(null, null, null, null)).withRel("createShortUrl"));


    return new ResponseEntity<>(represenntationModel, HttpStatus.OK);

  }



  @GetMapping("/{shortUrl}")
  public RedirectView processRedirect(@PathVariable(required = true) String shortUrl, HttpServletRequest request)
  {


    




    
    String longUrl = urlService.getRedirectUrl(request, urlDataRepository);



    System.out.println("call processRedirect..shortUrl is " + shortUrl + " redirect to " + longUrl);

    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(longUrl);



    return redirectView;

  }



  @PostMapping("/shortUrl")
  @ResponseBody
  public HttpEntity<?> createShortUrl(@Valid @RequestBody ShortUrlData userInput, 
  BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
  {
  



    System.out.println("ShortenUrlController.submitUrlForm() = " + userInput.getLongUrl());


    

    if(bindingResult.hasErrors())
    {

      bindingResult
      .getFieldErrors()
      .stream()
      .forEach(f -> System.out.println(f.getField() + ": " + f.getDefaultMessage()));

      System.out.println("just return..");

      return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
    }



    String shortUrl = urlService.processUrlData(userInput, request, urlDataRepository);



  
   

    System.out.println("longUrl been shorten is " + shortUrl);


    EntityModel<ShortUrlData> represenntationModel = EntityModel.of(userInput);

		represenntationModel.add(linkTo(methodOn(ShortenUrlRestfulController.class)
    .createShortUrl(null, null, null, null)).withSelfRel());


    


    return new ResponseEntity<>(represenntationModel, HttpStatus.OK);
  }



}