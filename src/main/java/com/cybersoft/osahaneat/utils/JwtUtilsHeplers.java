package com.cybersoft.osahaneat.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtilsHeplers {
    @Value("${jwt.privateKey}")
    private String privateKey;
    long expiredTime = 8 * 60 * 60 * 1000; //8 tiếng
    public String gererateToken(String data){
        System.out.println(privateKey);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));

        Date date = new Date();
        long currentDateMilis = date.getTime() + expiredTime;
        Date expriredDate = new Date(currentDateMilis);

        String jwt = Jwts.builder()
                .setSubject(data) //dữ liệu muốn lưu kèm khi mã hóa JWT để sau này lấy ra sử dụng
                .signWith(key) //key mã hóa
                .setExpiration(expriredDate)
                .compact();
        System.out.println(jwt);
        return jwt;
    }

    public boolean verifyToken(String token){
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getDataFromToken(String token){
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){
            return "";
        }
    }
}
