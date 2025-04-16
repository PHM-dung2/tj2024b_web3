package web.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Spring 컨테이너에 빈 등록
public class JwtUtil {

    // 비밀키 알고리즘 : HS256알고리즘, HS512알고리즘
    // private String secretKey = "인코딩된 HS512 비트 키";
    // (1) 개발자 임의로 지정한 키 : private String secretKey = "qP9sLxV3tRzWn8vMbKjUyHdGcTfEeXcZwAoLpNjMqRsTuVyBxCmZkYhGjFlDnEpQzFgXt9pMwX8Sx7CtQ5VtBvKmA2QwE3D";
    // (2) 라이브러리 이요한 임의 키 :
        // import java.security.Key;
        // Keys.secretKeyFor( SignatureAlgorithm.알고리즘명 );
    private Key secretKey = Keys.secretKeyFor( SignatureAlgorithm.HS256 );

    // [1] JWT 토큰 발급, 사용자의 이메일을 받아서 토큰 만들기
    public String createToken( String memail ){
        return Jwts.builder()
                // 토큰에 넣을 내용물, 로그인 성공한 회원의 이메일을 넣는다.
                .setSubject( memail )
                // 토큰이 발급된 날짜, new Data() : 자바에서 제공하는 현재날짜 클래스
                .setIssuedAt( new Date() )
                // 토큰 만료시간, 밀리초( 1/1000 ), new Date( System.currentTimeMillis() ) : 현재시간의 밀리초
                // new Date( System.currentTimeMillis() + ( 1000 * 초 * 분 * 시 ) )
                .setExpiration( new Date( System.currentTimeMillis() + ( 1000 * 60 * 60 * 24 ) ) ) // 1일의 토큰 유지기간
                // 지정한 비밀키로 암호화한다.
                .signWith( secretKey )
                // 위 정보로 JWT 토큰 생성하고 반환한다.
                .compact();
    } // f end

    // [2] JWT 토큰 검증
    public String validateToken( String token ){
        try{
            Claims claims = Jwts.parser() // 1. parser : JWT토큰 검증하기 위한 함수
                    .setSigningKey( secretKey ) // 2. 검증하기 위한 비밀키 넣기
                    .build() // 3. 검증 실행할 객체 생성
                    .parseClaimsJws( token ) // 4. 검증에 사용할 토큰 지정
                    .getBody(); // 5. 검증된 (claims) 객체 생성 후 반환
            // claims 안에는 다양한 토큰 정보 들어있따.
            System.out.println("claims = " + claims.getSubject()); // 토큰에 저장된 (로그인된)회원 이메일
            return claims.getSubject();
        }catch ( ExpiredJwtException e ){
            // 토큰이 만료되었을 때 예회 클래스
            System.out.println(" >> JWT 토큰 기한 만료 " + e);
        }catch ( JwtException e ){
            // 그 외 모든 토큰 예외 클래스
            System.out.println();
        }
        return null; // 유효하지 않은 토큰 또는 오류 발생시 null 반환
    } // f end

}
