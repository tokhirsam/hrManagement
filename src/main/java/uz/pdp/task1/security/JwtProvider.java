package uz.pdp.task1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.payload.RoleTokenParse;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private static final long expireTime = 1000 * 60 * 60 * 24;
    private static final String secretKey = "topSecret";

    public String generateToken(String username, Set<Role> role) {
        Date expireTime = new Date(System.currentTimeMillis() + JwtProvider.expireTime);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .claim("roles", role)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();


    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (Exception e) {
            return null;
        }

    }
    public String getRoleFromToken(String token) {
        try {
            Object roleJson = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles");
            ArrayList<RoleTokenParse> roles = (ArrayList<RoleTokenParse>) roleJson;
            String rolesToString = roles.toString();
            String[] split = rolesToString.split(",");
            return split[1].substring(10);


        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
