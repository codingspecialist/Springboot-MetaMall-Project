package shop.mtcoding.metamall.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilterResponseUtils {
    public static void unAuthorized(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.UNAUTHORIZED, "인증 안됨", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.FORBIDDEN, "권한 없음", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }
}
