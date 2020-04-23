package io.todo.api.security.jwt;

public class JwtBean {
    private String prefix;
    private String issuer;
    private Long expirationTimeInMillis;
    private Long verificationExpirationTimeInMillis;
    private String key;

    public JwtBean() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getExpirationTimeInMillis() {
        return expirationTimeInMillis;
    }

    public void setExpirationTimeInMillis(Long expirationTimeInMillis) {
        this.expirationTimeInMillis = expirationTimeInMillis;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getVerificationExpirationTimeInMillis() {
        return verificationExpirationTimeInMillis;
    }

    public void setVerificationExpirationTimeInMillis(Long verificationExpirationTimeInMillis) {
        this.verificationExpirationTimeInMillis = verificationExpirationTimeInMillis;
    }
}
