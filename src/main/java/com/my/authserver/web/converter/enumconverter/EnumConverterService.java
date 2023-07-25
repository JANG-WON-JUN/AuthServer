package com.my.authserver.web.converter.enumconverter;

import com.my.authserver.domain.enums.Sex;

import static com.my.authserver.domain.enums.Sex.FEMALE;
import static com.my.authserver.domain.enums.Sex.MALE;

/*
    1. Get 요청으로 전달받은 쿼리스트링의 파라미터를 DTO의 enum 객체를 매핑할 때
        -> spring converter를 사용하여 매핑
    2. Post 요청으로 Http Body에 담긴 JSON 파라미터를 DTO enum 객체를 매핑할 때
        -> Enum에 @JsonCreator 어노테이션을 사용하여 매핑
    3. 1번과 2번의 로직은 공통으로 사용하기 위해
        EnumConverterService에 static으로 선언 후 각 위치에서 사용한다.

    참고. 브라우저에서 전송되는 source값이 null이면 spring convert 혹은 @JsonCreator가 작동하지 않는다.
*/
public class EnumConverterService {

    public static Sex stringToSex(String source){
        return switch (source.toUpperCase()){
            case "M", "MALE" -> MALE;
            case "F", "FEMALE" -> FEMALE;
            default -> null;
        };
    }
}
