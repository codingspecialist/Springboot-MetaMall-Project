package shop.mtcoding.metamall.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.ValidDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filter는 예외 핸들러로 처리 못한다.
public class MyFilterResponseUtil {

    public static void badRequest(HttpServletResponse resp, Exception400 e) throws IOException {
        resp.setStatus(400);
        resp.setContentType("application/json; charset=utf-8");
        ValidDto validDto = new ValidDto(e.getKey(), e.getValue());
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, "badRequest", validDto);
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }

    public static void unAuthorized(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.UNAUTHORIZED, "unAuthorized", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.FORBIDDEN, "forbiden", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }
}
