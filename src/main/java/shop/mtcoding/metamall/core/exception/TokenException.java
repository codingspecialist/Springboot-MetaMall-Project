package shop.mtcoding.metamall.core.exception;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.http.MediaType;
import shop.mtcoding.metamall.dto.ResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenException extends RuntimeException{

    TOKEN_ERROR tokenError;


    @Getter
    public enum TOKEN_ERROR{
        UNACCEPT(401,"Token is null or too short"),
        BADTYPE(401,"Token type Bearer"),
        MALFORM(403,"Malformed Token"),
        BADSIGN(403,"BadSignatured Token"),
        EXPIRED(403,"Expired Token");

        private int status;
        private String msg;

        TOKEN_ERROR(int status,String msg){
            this.status = status;
            this.msg = msg;
        }
    }
    public TokenException(TOKEN_ERROR tokenError){
        super(tokenError.msg);
        this.tokenError = tokenError;
    }
    public void sendResponseError(HttpServletResponse response){
        response.setStatus(tokenError.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        ResponseDto<?> responseDto = new ResponseDto<>()
                .msg(tokenError.getMsg())
                .code(tokenError.getStatus());

        String result = gson.toJson(responseDto);

        try{
            response.getWriter().println(result);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
