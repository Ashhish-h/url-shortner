package com.url_shortner.url_shortner.encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Base62Encoder {

    @Value("${encoding-key}")
    private String CHARS;

    public String encode(Long id){

        StringBuilder base62Number = new StringBuilder();

        if(id == 0){
            return String.valueOf(CHARS.charAt(0));
        }

        if(id > 0) {

            while (id > 0) {
                long remainder = (id % 62);
                base62Number.append(CHARS.charAt((int) remainder));
                id = id / 62;
            }
        }

        return base62Number.reverse().toString();
    }

    public Long decode(String base62Number){
        long decodedNumber = 0L;

        for(int i = 0; i < base62Number.length() ; i++){
           int index = CHARS.indexOf(base62Number.charAt(i));

           if(index != -1){
               decodedNumber = decodedNumber * 62 + index;
           } else {
               throw new IllegalArgumentException(base62Number + " is not a valid base62 number.");
           }
        }

        return decodedNumber;
    }

}
