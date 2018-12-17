package com.frame.hariko.spring.boot.token.jwt.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * 密钥
     */
    private String secretKey = "dashuf";
    /**
     * 签发人
     */
    private String issuer;
    /**
     * 主题
     */
    private String subject;
    /**
     * 签发时间
     */
    private Date issuedAt = new Date();
    /**
     * 过期时间(ms)
     */
    private Long expiration;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
