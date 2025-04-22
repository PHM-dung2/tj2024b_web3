package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Boolean> signUp(@RequestBody MemberDto memberDto ){
        boolean result = memberService.signUp( memberDto );
        if( result ) { // * 개발자 마음대로 원하는 응답 코드를 반환할 수 있다.
            return ResponseEntity.status( 201 ).body( true ); // 201(create ok 뜻) true(반환값)
        }else{
            return ResponseEntity.status( 400 ).body( false ); // 400(bad request 잘못된 요청 뜻 )
        }
    } // f end

    // 2. 로그인
    @PostMapping("/login")
//    public boolean logIn( @RequestBody MemberDto memberDto ){
    public ResponseEntity<String> logIn( @RequestBody MemberDto memberDto ){
        String token = memberService.logIn( memberDto );
        if( token != null ){ // 만약에 토큰이 존재하면( 로그인 성공 )
            return ResponseEntity.status( 200 ).body( token );
        }else{
            return ResponseEntity.status( 400 ).body( "로그인 실패" ); // 반응 실패 : null
        }
    } // f end

    // 3. 로그인된 회원 검증 / 내정보 조회
    // @RequestHeader : HTTP 헤더 정보를 매핑하는 어노테이션, JWT 정보는 HTTP 헤더에 담을 수 있다.
        // Authorization : 인증 속성, { Authorization : token 값 }
    // @RequestPram : HTTP 헤더의 쿼리스트링 매핑하는 어노테이션
    // @RequestBody : HTTP 본문의 객체를 매핑하느 어노테이션
    // @PathVariable : HTTP 헤더의 경로 값 매핑하는 어노테이션
    @GetMapping("/info")
    // headers : { 'Authorization' : 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2UxMjNAcXdlcXdlIiwiaWF0IjoxNzQ0NzcxNDYxLCJleHAiOjE3NDQ4NTc4NjF9.hiri5tW65SXL1HUUHz8BNMH--XAhS7Z4lN34AaI6P6A' }
    public ResponseEntity<MemberDto> info( @RequestHeader("Authorization") String token ){
        MemberDto memberDto = memberService.info( token );
        if( memberDto != null){
            return ResponseEntity.status( 200 ).body( memberDto ); // 200 ok와 조회된 회원정보 반환
        }else{
            return ResponseEntity.status( 403 ).build(); // 403과 NoContent(자료없음)
        }
    } // f end

    // 4. 로그아웃, 로그아웃할 토큰 가져오기
    @GetMapping("/logout")
    public ResponseEntity<Void> logout( @RequestHeader("Authorization") String token ){
        memberService.logout( token );
        return ResponseEntity.status( 204 ).build(); // 204 : 성공했지만 반환할 값이 없다 뜻
    } // f end

    // 5. 최근 24시간 로그인한 접속자 수
    @GetMapping("/login/count")
    public ResponseEntity<Integer> loginCount(){
        int result = memberService.loginCount();
        return ResponseEntity.status( 200 ).body( result ); // 200 : 요청 성공 뜻과 응답 값 반환
    } // f end


}
