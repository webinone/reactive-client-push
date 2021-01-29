package io.barogo.push.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
public class JwtTokenUtils implements Serializable {

  public static String getUsernameFromToken(String token) throws Exception {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public static Date getExpirationDateFromToken(String token) throws Exception {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private static Claims getAllClaimsFromToken(String token) throws Exception {

    ClassPathResource resource = new ClassPathResource("jwtOauth2.jks");

    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(resource.getInputStream(), "qkfhrhOauth123!".toCharArray());

    Key key = keystore.getKey("barogoOauth", "qkfhrhOauth123!".toCharArray());
    Certificate cert = keystore.getCertificate("barogoOauth");
    PublicKey SIGNING_KEY = cert.getPublicKey();

    return Jwts.parser()
        .setSigningKey(SIGNING_KEY)
        .parseClaimsJws(token)
        .getBody();
  }

  private static Boolean isTokenExpired(String token) throws Exception {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public static Boolean validateToken(String token) {

    boolean isValid = true;

    try {
      String username = getUsernameFromToken(token);
    } catch (IllegalArgumentException e) {
      isValid = false;
      log.error("an error occured during getting username from token", e);
    } catch (ExpiredJwtException e) {
      isValid = false;
      log.warn("the token is expired and not valid anymore", e);
    } catch(SignatureException e){
      isValid = false;
      log.error("Authentication Failed. Username or Password not valid.");
    } catch (Exception ex) {
      isValid = false;
    }
    return isValid;
  }

}
