package shop.mtcoding.metamall.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

/**
* JWT에서 권한정보 추출
* */
@Component
public class JwtUtil {

	private static final String SECRET = "메타코딩";
	private static final String AUTH_HEADER = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer ";

	public String extractUsername(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody();

		return claims.getSubject();
	}

	public String extractRole(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody();

		return claims.get("role", String.class);
	}
}

