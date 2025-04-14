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
    public boolean onSave( @RequestBody MemberDto memberDto ){
        return memberService.onSave( memberDto );
    } // f end

    // 2. 로그인
    @PostMapping("/login")
    public boolean onLogIn( @RequestBody MemberDto memberDto ){
        return memberService.onLogIn( memberDto );
    } // f end

}
