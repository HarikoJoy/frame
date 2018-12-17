package com.frame.hariko.spring.boot.token.jwt.autoconfigure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

/**
 * jwt 处理
 */
public class JwtTokens {

    private final Logger logger = LoggerFactory.getLogger(JwtTokens.class);

    private JwtProperties jwtProperties;

    public JwtTokens(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 创建jwt token
     * 默认取读取配置，如果没有配置则取空
     */
    public String create() {
        return create(Jwts.claims());
    }
    /**
     * 创建包含过期时间的token
     *
     * @param ttl 过期时间(ms)
     */
    public String create(long ttl) {
        return create(null, new Date(System.currentTimeMillis() + ttl));
    }
    /**
     * 创建包含过期时间的token
     *
     * @param end 过期时间
     */
    public String create(Date end) {
        return create(null, end);
    }
    /**
     * 创建包含开始时间和过期时间的token
     *
     * @param start 开始时间
     * @param end   结束时间
     */
    public String create(Date start, Date end) {
        return create(Jwts.claims().setExpiration(end).setNotBefore(start));
    }
    /**
     * 创建包含过期时间的，自定义参数的token
     * @param ttl    超时时间
     * @param claims 自定义body
     * @return
     */
    public String create(Claims claims, long ttl) {
        claims.setExpiration(new Date(System.currentTimeMillis() + ttl));
        return create(claims);
    }

    /**
     * 创建自定义参数的token
     * 如果不设置参数，取默认配置
     * Claims claims = Jwts.claims();
     * claims.setExpiration(new Date());
     * claims.setIssuer("dashu");
     * claims.put("userId", 1L);
     *
     * @param claims
     * @return
     */
    public String create(Claims claims) {

        if (claims.getExpiration() == null && jwtProperties.getExpiration() != null) {
            claims.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()));
        }
        if (claims.getIssuer() == null) {
            claims.setIssuer(jwtProperties.getIssuer());
        }
        if (claims.getIssuedAt() == null) {
            claims.setIssuedAt(jwtProperties.getIssuedAt());
        }
        if (claims.getSubject() == null) {
            claims.setSubject(jwtProperties.getSubject());
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        return Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(getSecretKey()), signatureAlgorithm)
                .compact();
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     * @throws ExpiredJwtException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     * @throws IllegalArgumentException
     */
    public Jws<Claims> parser(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
    }

    private byte[] getSecretKey() {
        if (jwtProperties.getSecretKey().equals("dashuf")) {
            logger.warn("您需要重新配置jwt.secret-key!!!!");
        }
        byte[] base64key = DatatypeConverter.parseBase64Binary(jwtProperties.getSecretKey());
        return base64key.length < 32 ? Arrays.copyOf(base64key, 32) : base64key;
    }
}
