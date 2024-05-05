package com.shortenUrl.miniUrl.service;

import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;
import com.shortenUrl.miniUrl.model.RegisterUser;

public interface IUserService 
{
    RegisterUser registerNewUserAccount(UserInfoForm user);

    // class no need to implement all the function in interface
    // class can implement multiple interface while only one class can be extended
    // abstract void lazyFunc();
    
}
