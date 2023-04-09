package shop.mtcoding.metamall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ValidDto {
    private List<ValidDetail> errors = new ArrayList<>();

    public ValidDto(Map<String, String> errorMap) {
        errorMap.forEach((k, v) -> {
            ValidDetail error = new ValidDetail(k, v);
                    errors.add(error);
                }
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ValidDetail {
        private String key;
        private String value;
    }
}
