package shop.mtcoding.metamall.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.ValidDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 생성 이유: filter는 예외 핸들러로 처리 못함
public class MyFilterResponseUtil {
    public static void badRequest(HttpServletResponse resp, Exception400 e) throws
            IOException {
        resp.setStatus(400);
        resp.setContentType("application/json; charset=utf-8");
        ValidDTO validDTO = new ValidDTO(e.getKey(), e.getValue());
        ResponseDTO<?> responseDto = new ResponseDTO<>().fail(HttpStatus.BAD_REQUEST,
                "badRequest", validDTO);
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }
    public static void unAuthorized(HttpServletResponse resp, Exception e) throws
            IOException {
        resp.setStatus(401);
        resp.setContentType("application/json; charset=utf-8");

//        ResponseDTO<?> v1 = new ResponseDTO<>(); // delete 반환값 void
//        ResponseDTO<?> v2 = new ResponseDTO<>().data("값"); // get, put, post 성공시 메세지
//        ResponseDTO<?> v3 = new ResponseDTO<>().fail(); // 실패시

        ResponseDTO<?> responseDto = new ResponseDTO<>
                ().fail(HttpStatus.UNAUTHORIZED, "unAuthorized", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }
    public static void forbidden(HttpServletResponse resp, Exception e) throws
            IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDTO<?> responseDto = new ResponseDTO<>().fail(HttpStatus.FORBIDDEN,
                "forbidden", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }
}
