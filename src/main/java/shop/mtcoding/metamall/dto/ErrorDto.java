package shop.mtcoding.metamall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorDto {
    private List<ErrorDetail> errors = new ArrayList<>();

    public ErrorDto(Map<String, String> errorMap) {
        errorMap.forEach((k, v) -> {
                    ErrorDetail error = new ErrorDetail(k, v);
                    errors.add(error);
                }
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorDetail {
        private String key;
        private String value;
    }
}
