package com.example.demo.global.util;

import com.example.demo.domain.refresh.entity.RefreshToken;
import com.example.demo.domain.refresh.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String BEARER_PREFIX = "Bearer ";

	@Value("${jwt.secret.key}")
	private String secretKey;

	private final long REFRESHTOKENTIME = 60 * 60 * 1000 * 24 * 7L;

	private final RefreshTokenRepository refreshTokenRepository;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	private Key key;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public String validateRefreshToken(Long userId) {
		RefreshToken token = refreshTokenRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("RefreshToken 이 유효하지 않습니다.")
		);
		String refreshToken = token.getRefreshToken().substring(7);
		Claims info = Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(refreshToken).getBody();
		return createAccessToken(info.get("userId", Long.class),
			info.get("username", String.class),info.get("nickname", String.class));
	}

	public void deleteRefreshToken(Long userId) {
		Optional<RefreshToken> checkToken = refreshTokenRepository.findByUserId(userId);
		checkToken.ifPresent(refreshTokenRepository::delete);
	}

	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String createToken(Long userId, String username,String nickname) {
		Date date = new Date();

		String accessToken = createAccessToken(userId, username,nickname);
		deleteRefreshToken(userId);

		String refreshToken = BEARER_PREFIX +
			Jwts.builder()
				.claim("userId", userId)
				.claim("username", username)
				.claim("nickname",nickname)
				.setIssuedAt(new Date(date.getTime()))
				.setExpiration(new Date(date.getTime() + REFRESHTOKENTIME))
				.signWith(key, signatureAlgorithm)
				.compact();
		RefreshToken token = RefreshToken.builder().refreshToken(refreshToken).userId(userId)
			.build();
		refreshTokenRepository.save(token);
		return accessToken;
	}

	public String createAccessToken(Long userId, String username,String nickname) {
		Date date = new Date();

		long TOKEN_TIME = 60 * 60 * 10000;
		return BEARER_PREFIX +
			Jwts.builder()
				.claim("userId", userId)
				.claim("username", username)
				.claim("nickname",nickname)
				.setExpiration(new Date(date.getTime() + TOKEN_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	public Claims getMemberInfoFromExpiredToken(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}