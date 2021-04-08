package com.yicj.study.mvc.convert.converter;


import com.yicj.study.mvc.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserConverter implements Converter<String, User> {

    @Override
    public User convert(String source) {
        User user = new User() ;
        String[] infos = source.split("-");
        long id = Long.parseLong(infos[0]);
        String userName = infos[1] ;
        String note = infos[2] ;
        user.setId(id);
        user.setUserName(userName);
        user.setNote(note);
        return user;
    }
}
