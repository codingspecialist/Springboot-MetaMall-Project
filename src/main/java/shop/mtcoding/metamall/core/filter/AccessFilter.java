package shop.mtcoding.metamall.core.filter;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.mtcoding.metamall.core.exception.TokenException;
import shop.mtcoding.metamall.core.jwt.JwtProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Order(1)
@Component
public class AccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(path.startsWith("/user") || path.startsWith("/login")){   //user ㄱ관련된 돗은 통과
            filterChain.doFilter(request,response);
            return;
        }

        try{
            Map<String,Claim> token = validateAccessToken(request);

            System.out.println(token);
            String role = token.get("role").asString();
            String username = token.get("id").asString();

            log.info("username ===> {}",username);
            log.info("role ===> {}",role);

            request.setAttribute("role",role);
            request.setAttribute("username",username);
            filterChain.doFilter(request,response);

        }catch (TokenException tokenExpiredException){
            tokenExpiredException.sendResponseError(response);
        }


    }

    public Map<String, Claim> validateAccessToken(HttpServletRequest request) throws TokenException{
        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length()<8){
            throw new TokenException(TokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);

        try{
            DecodedJWT decodedJWT = JwtProvider.verify(tokenStr);
            Map<String, Claim> claimMap = decodedJWT.getClaims();
            return claimMap;
        }catch (TokenExpiredException tokenExpiredException){//시간만료
            log.warn("TOKEN EXPIRED - Refresh Token request Is required 403 ");
            throw  new TokenException(TokenException.TOKEN_ERROR.EXPIRED);
        }catch (SignatureVerificationException signatureVerificationException){ //토큰의 서명 검증이 실패한 경우
            log.warn("TOKEN BADSIGN - Token has changed its signature changed ");
            throw  new TokenException(TokenException.TOKEN_ERROR.BADSIGN);
        }catch (InvalidClaimException invalidClaimException){ //토큰의 클레임 검증 실패 클레임이 없거나 예상값과 다를 때
            log.warn("TOKEN INVALID - Invalid token.");
            throw  new TokenException(TokenException.TOKEN_ERROR.MALFORM);
        }catch (JWTDecodeException jwtDecodeException){ //토큰손상되면 또는 올바른 형식이 아니.면
            log.warn("TOKEN MALFORM - Invalid token ");
            throw  new TokenException(TokenException.TOKEN_ERROR.MALFORM);
        }
    }



}
