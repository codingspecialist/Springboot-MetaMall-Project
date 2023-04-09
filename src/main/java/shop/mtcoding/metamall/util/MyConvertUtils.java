package shop.mtcoding.metamall.util;

import shop.mtcoding.metamall.dto.ValidDto;

import java.util.HashMap;
import java.util.Map;

public class MyConvertUtils {
    public static ValidDto stringToErrorDto(String key, String value){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(key, value);
        return hashToErrorDto(errorMap);
    }

    public static ValidDto hashToErrorDto(Map<String, String> errorMap){
        ValidDto errorDto = new ValidDto(errorMap);
        return errorDto;
    }
}
