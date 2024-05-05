package com.shortenUrl.miniUrl.validation;

import com.shortenUrl.miniUrl.dataTransferObject.UserInfoForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> 
{
    
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserInfoForm user = (UserInfoForm) obj;

        boolean value = user.getPassword().equals(user.getConfirmPassword());
        System.out.println("PasswordMatchesValidator isValid = " + value);
        return user.getPassword().equals(user.getConfirmPassword());
    }
}