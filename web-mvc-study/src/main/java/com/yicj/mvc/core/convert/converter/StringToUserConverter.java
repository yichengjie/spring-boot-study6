package com.yicj.mvc.core.convert.converter;

import com.yicj.mvc.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserConverter implements Converter<String, User> {

    @Override
    public User convert(String source) {
        User user = new User();
        String[] infos = source.split("-");
        String username = infos[0] ;
        String addr = infos[1] ;
        user.setUsername(username);
        user.setAddr(addr);
        return user;
    }
}
