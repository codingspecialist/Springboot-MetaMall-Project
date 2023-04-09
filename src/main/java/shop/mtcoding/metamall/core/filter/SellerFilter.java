package shop.mtcoding.metamall.core.filter;


import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.user.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SellerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("디버그 : SellerFilter 동작함");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        User user = (User) req.getSession().getAttribute("loginUser");
        if (!user.getRole().equals("SELLER")) {
            error(resp, new Exception400("등록된 SELLER 가 아닙니다."));
            return;
        }
        chain.doFilter(req,resp);
    }

    private void error(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDto<?> responseDto = new ResponseDto<>().fail(HttpStatus.UNAUTHORIZED, "권한이 없습니다", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        resp.getWriter().println(responseBody);
    }

}
