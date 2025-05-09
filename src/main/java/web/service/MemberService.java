package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberRepository;
import web.util.JwtUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional // 트랜잭션 : 여러개의 SQL 명령어를 하나의 논리 단위 묶음
// 트랜잭션은 성공 또는 실패, 부분 성공은 없다.
// 메소드 안에서 여러가지 SQL 실행할 경우 하나라도 오류가 발생하면 롤백(취소) * JPA 엔티티 수정
public class MemberService {
    private final MemberRepository memberRepository;

    // 1. 회원가입
    public boolean signUp( MemberDto memberDto ){
        if( memberDto.getMpwd() == null ){ return false; }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashPwd = passwordEncoder.encode( memberDto.getMpwd() );
        memberDto.setMpwd( hashPwd );

        MemberEntity memberEntity = memberDto.toEntity();
        MemberEntity saveEntity = memberRepository.save( memberEntity );

        if( saveEntity.getMno() >= 1 ){ return true; }

        return false;
    } // f end

    // * JWT 객체 투입
    private final JwtUtil jwtUtil;

    // 2. 로그인
    public String logIn( MemberDto memberDto ){
        // 1. 이메일(아이디)를 DB에서 조회하여 엔티티 찾기
        MemberEntity memberEntity = memberRepository.findByMemail( memberDto.getMemail() );
        // 2. 조회된 엔티티가 없으면
        if( memberEntity == null ){ return null; } // 로그인 실패
        // 3. 조회된 엔티티의 비밀번호 검증  .matches( 입력받은패스워드, 암호화된패스워드 )
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean inMatch = passwordEncoder.matches( memberDto.getMpwd(), memberEntity.getMpwd() );
        // 4. 비밀번호 검증 실패이면
        if( inMatch == false ){ return null; } // 로그인 실패
        // 5. 비밀번호 검증 성공이면, 세션 할당 vs 토큰 할당
        String token = jwtUtil.createToken( memberEntity.getMemail() );
        System.out.println("token = " + token);

        // + 레디스에 24시간만 저장되는 로그인 로그(기록)하기
        stringRedisTemplate.opsForValue().set(
                "RECENT_LOGIN:" + memberDto.getMemail(), "true", 1, TimeUnit.DAYS );

        return token;
    } // f end

//    public boolean logIn( MemberDto memberDto ){
//        if (memberDto == null || memberDto.getMemail() == null) { return false; }
//        List<MemberEntity> memberEntityList = memberRepository.findAll();
//        String matchPwd = memberRepository.MatchPwd( memberDto.getMemail() );
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches( memberDto.getMpwd(), matchPwd );
//    } // f end

    // 3. 전달받은 token으로 token 검증하여 유효한 token은 회원정보 반환(dto) 반환,
    //                                   유효하지 않은 tokeb null 반환
    public MemberDto info( @RequestHeader("Authorization") String token ){
        // 1. 전달받은 token으로 검증하기 vs 세션 호출
        String memail = jwtUtil.validateToken( token );
        // 2. 검증이 실패이면 '비로그인'이거나 유효기간 만료, 실패
        if( memail == null ){ return null; }
        // 3. 검증이 성공이면 토큰에 저장된 이메일을 가지고 엔티티 조회
        MemberEntity memberEntity = memberRepository.findByMemail( memail );
        // 4. 조회된 엔티티가 없으면 실패
        if( memberEntity == null ){ return null; }
        // 5. 조회 성공시 조회된 엔티티를 dto로 변환한다.
        return memberEntity.toDto();
    } // f end

    // 4. 로그아웃
    public void logout( String token ){
        // 1. 해당 token의 이메일 조회
        String memail = jwtUtil.validateToken( token );
        // 2. 조회된 이메일의 redis 토큰 삭제
        jwtUtil.deleteToken( memail );
    } // f end

    @Autowired
    private final StringRedisTemplate stringRedisTemplate;

    // 5. 최근 24시간 로그인한 접속자 수
    public int loginCount(){ // * 로그인시 레디스에 'RECENT_LOGIN:' 이름으로 key 저장
        // 레디스에 저장된 키들 중에서 "RECENT_LOGIN:"으로 시작하는 모든 KEY 반환
        Set<String> keys = stringRedisTemplate.keys("RECENT_LOGIN:*");
        // 반환된 개수 확인, 비어있으면 0 아니면 size() 함수 이용한 key 개수 반환
        return keys == null ? 0 : keys.size();
    } // f end

}
