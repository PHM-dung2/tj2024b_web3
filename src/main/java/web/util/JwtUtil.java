package web.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component // Spring 컨테이너에 빈 등록
public class JwtUtil {

    // 비밀키 알고리즘 : HS256알고리즘, HS512알고리즘
    // private String secretKey = "인코딩된 HS512 비트 키";
    // (1) 개발자 임의로 지정한 키 : private String secretKey = "qP9sLxV3tRzWn8vMbKjUyHdGcTfEeXcZwAoLpNjMqRsTuVyBxCmZkYhGjFlDnEpQzFgXt9pMwX8Sx7CtQ5VtBvKmA2QwE3D";
    // (2) 라이브러리 이요한 임의 키 :
        // import java.security.Key;
        // Keys.secretKeyFor( SignatureAlgorithm.알고리즘명 );
    private Key secretKey = Keys.secretKeyFor( SignatureAlgorithm.HS256 );

    @Autowired // 빈 주입
    private StringRedisTemplate stringRedisTemplate; // Redis를 조작하기 위한 객체

    // [1] JWT 토큰 발급, 사용자의 이메일을 받아서 토큰 만들기
    public String createToken( String memail ){
        String token = Jwts.builder() // + 해당 반환된 토큰을 변수에 저장
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
        // + 중복 로그인 방지하고자 웹서버가 아닌 Redis에 토큰 저장
            // (1) Redis에 토큰 저장하기,  .opsForValue.set( key, value );,  .opsForValue( 계정식별번호, 토큰 );
            stringRedisTemplate.opsForValue().set( "JWT:" + memail, token, 24, TimeUnit.HOURS ); // 토큰 유지시간과 일치
            // (2) Redis에 저장된 key들을 확인, .keys("*") : 현재 redis에 저장된 모든 key 반환
            System.out.println( stringRedisTemplate.keys("*") );
            // (3) Redis에 저장된 key 값 확인 .opsForValue().get( key );
            System.out.println( stringRedisTemplate.opsForValue().get( "JWT:" + memail ) );

        return token;
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

            // + 중복 로그인 방지하고자 Redis에서 최근 로그인된 토큰 확인
            String memail = claims.getSubject(); // + 현재 전달받은 토큰의 저장된 회원정보( 이메일 )
                // (1) 레디스에서 최신 토큰 가져오기
            String redisToken = stringRedisTemplate.opsForValue().get( "JWT:" + memail );
                // (2) 현재 전달받은 토큰과 레디스에 저장된 토큰 비교
            if( token.equals( redisToken ) ){ return memail; } // 현재 로그인상태 정상(중복 로그인이 아니다.)
                // (3) 만약에 두 토큰이 다르면 아래 코드에 nlll이 리턴된다.(토큰 불일치 또는 중복 로그인 감지)
            else{ System.out.println( " >> 중복 로그인 감지" ); }
        }catch ( ExpiredJwtException e ){
            // 토큰이 만료되었을 때 예회 클래스
            System.out.println(" >> JWT 토큰 기한 만료 " + e);
        }catch ( JwtException e ){
            // 그 외 모든 토큰 예외 클래스
            System.out.println(" >> JWT 예외 : " + e);
        }
        return null; // 유효하지 않은 토큰 또는 오류 발생시 null 반환
    } // f end

    // [3] 로그아웃시 redis에 저장된 토큰 삭제 서비스
    public void deleteToken( String memail ){
        stringRedisTemplate.delete( "JWT:" + memail );
    } // f end

}
