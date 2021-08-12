package com.yicj.study.mvc.validation;


import com.yicj.study.mvc.model.User;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        boolean flag = User.class.isAssignableFrom(clazz);
        return flag ;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null){
            errors.rejectValue("", null, "用户不能为空");
            return;
        }
        User user = (User) target ;
        if (StringUtils.isEmpty(user.getUserName())){
            errors.rejectValue("userName", null, "用户名不能为空");
        }
    }
}
