package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor // final(수정불가) 필드의 생성자 자동 생성
@CrossOrigin("*")
public class MemberController {
    // -> 관례적으로 클래스 내부에서 사용하는 모든 필드들을 수정불가능 상태로 사용한다.(안정성 보장)
    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping
    public boolean signUp( @RequestBody MemberDto memberDto ){
        return memberService.signUp( memberDto );
    } // f end

    // 2. 로그인
    @PostMapping("/login")
//    public boolean logIn( @RequestBody MemberDto memberDto ){
    public String logIn( @RequestBody MemberDto memberDto ){
        return memberService.logIn( memberDto );
    } // f end

    // 3. 로그인된 회원 검증 / 내정보 조회
    // @RequestHeader : HTTP 헤더 정보를 매핑하는 어노테이션, JWT 정보는 HTTP 헤더에 담을 수 있다.
        // Authorization : 인증 속성, { Authorization : token 값 }
    // @RequestPram : HTTP 헤더의 쿼리스트링 매핑하는 어노테이션
    // @RequestBody : HTTP 본문의 객체를 매핑하느 어노테이션
    // @PathVariable : HTTP 헤더의 경로 값 매핑하는 어노테이션
    @GetMapping("/info")
    // headers : { 'Authorization' : 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2UxMjNAcXdlcXdlIiwiaWF0IjoxNzQ0NzcxNDYxLCJleHAiOjE3NDQ4NTc4NjF9.hiri5tW65SXL1HUUHz8BNMH--XAhS7Z4lN34AaI6P6A' }
    public MemberDto info( @RequestHeader("Authorization") String token ){
        System.out.println("token = " + token);
        return memberService.info( token );
    } // f end

    // 4. 로그아웃, 로그아웃할 토큰 가져오기
    @GetMapping("/logout")
    public void logout( @RequestHeader("Authorization") String token ){
        memberService.logout( token );
    } // f end

    // 5. 최근 24시간 로그인한 접속자 수
    @GetMapping("/login/count")
    public int loginCount(){
        return memberService.loginCount();
    } // f end


}
