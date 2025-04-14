package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // 트랜잭션 : 여러개의 SQL 명령어를 하나의 논리 단위 묶음
// 트랜잭션은 성공 또는 실패, 부분 성공은 없다.
// 메소드 안에서 여러가지 SQL 실행할 경우 하나라도 오류가 발생하면 롤백(취소) * JPA 엔티티 수정
public class MemberService {
    private final MemberRepository memberRepository;

    // 1. 회원가입
    public boolean onSignUp( MemberDto memberDto ){
        if( memberDto.getMpwd() == null ){ return false; }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashPwd = passwordEncoder.encode( memberDto.getMpwd() );
        memberDto.setMpwd( hashPwd );

        MemberEntity memberEntity = memberDto.toEntity();
        MemberEntity saveEntity = memberRepository.save( memberEntity );

        if( saveEntity.getMno() >= 1 ){ return true; }

        return false;
    } // f end

    // 2. 로그인
    public boolean onLogIn( MemberDto memberDto ){
        if (memberDto == null || memberDto.getMemail() == null) { return false; }
        List<MemberEntity> memberEntityList = memberRepository.findAll();


//        String matchPwd = result.getMpwd();
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches( memberDto.getMpwd(), matchPwd );
        return false;
    } // f end

}
