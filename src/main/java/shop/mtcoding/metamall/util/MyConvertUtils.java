package shop.mtcoding.metamall.util;

import shop.mtcoding.metamall.dto.ErrorDto;

import java.util.HashMap;
import java.util.Map;

public class MyConvertUtils {
    public static ErrorDto stringToErrorDto(String key, String value){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(key, value);
        return hashToErrorDto(errorMap);
    }

    public static ErrorDto hashToErrorDto(Map<String, String> errorMap){
        ErrorDto errorDto = new ErrorDto(errorMap);
        return errorDto;
    }
}
