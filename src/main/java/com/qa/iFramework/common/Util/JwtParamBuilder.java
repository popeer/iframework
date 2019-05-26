package com.qa.iFramework.common.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtParamBuilder {

    public static final String ISSUER = "iss";

    public static final String SUBJECT = "sub";


    /**
     * JWT {@code Audience} claims parameter name: <code>"aud"</code>
     */

    public static final String AUDIENCE = "aud";


    /**
     * JWT {@code Expiration} claims parameter name: <code>"exp"</code>
     */

    public static final String EXPIRATION = "exp";


    /**
     * JWT {@code Not Before} claims parameter name: <code>"nbf"</code>
     */

    public static final String NOT_BEFORE = "nbf";


    /**
     * JWT {@code Issued At} claims parameter name: <code>"iat"</code>
     */

    public static final String ISSUED_AT = "iat";


    /**
     * JWT {@code JWT ID} claims parameter name: <code>"jti"</code>
     */

    public static final String ID = "jti";


    private Map<String, Object> claims;


    private final long now;


    private JwtParamBuilder() {

        claims = new HashMap<String, Object>();

        now = System.currentTimeMillis() / 1000l;

    }


    public static JwtParamBuilder build() {

        return new JwtParamBuilder();

    }


    public JwtParamBuilder addIssuedAt() {

        claims.put(ISSUED_AT, now);

        return this;

    }


    public JwtParamBuilder setExpirySeconds(final Integer expirySeconds) {

        claims.put(EXPIRATION, now + expirySeconds);

        return this;

    }


    public JwtParamBuilder setNotBeforeSeconds(final Integer beforeSeconds) {

        claims.put(NOT_BEFORE, now - beforeSeconds);

        return this;

    }


    public JwtParamBuilder setSubject(String sub) {

        addOneClaim(SUBJECT, sub);

        return this;

    }


    public JwtParamBuilder setIssuer(String iss) {

        addOneClaim(ISSUER, iss);

        return this;

    }


    public JwtParamBuilder setAudience(String aud) {

        addOneClaim(AUDIENCE, aud);

        return this;

    }


    public JwtParamBuilder addJwtId() {

        return setJwtId(UUID.randomUUID().toString());

    }


    public JwtParamBuilder setJwtId(String jwtid) {

        addOneClaim(ID, UUID.randomUUID().toString());

        return this;

    }


    public JwtParamBuilder claim(String name, Object value) {

        if (value == null) {

            this.claims.remove(name);

        } else {

            this.claims.put(name, value);

        }

        return this;

    }


    private void addOneClaim(String key, String value) {

        if (value != null && value.length() > 0) {

            claims.put(key, value);

        }

    }


    /**
     * @return the claims
     */

    public Map<String, Object> getClaims() {

        return claims;

    }


}