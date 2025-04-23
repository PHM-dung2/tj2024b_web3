package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.MemberService;
import web.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {
    // *
    private final ProductService productService;
    private final MemberService memberService;

    // 1.
    /*
        제품등록 설계
        1. Post, "/product/register"
        2. '로그인 회원이 등록한다'
            토큰( Authorization ), 등록할 값들( pname, pcontent, pprice, 여러개 사진들, cno )
        3. boolean 반환
    */

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerProduct(
            @RequestHeader("Authorization") String token, // 토큰 받기
            @ModelAttribute ProductDto produductDto ){ // multipart/form(첨부파일) 받기
        System.out.println("token = " + token + ", produductDto = " + produductDto);

        // 1. 현재 토큰의 회원번호(작성자) 구하기
        int loginMno;
        try {
            loginMno = memberService.info(token).getMno();
        }catch ( Exception e ){
            return ResponseEntity.status( 401 ).body( false ); // 401 Unauthorized와 false 반환
        }

        // 2. 저장할 DTO와 회원번호를 서비스에게 전달
        boolean result = productService.registerProduct( produductDto, loginMno );
        if( result == false ){ return ResponseEntity.status( 400 ).body( false ); } // 400 Bad Request와 false 반환
        // 3. 요청 성공시 200 반환
        return ResponseEntity.status( 200 ).body( true ); // 200 요청성공과 true 반환
    } // f end

    // 2. (카테고리별) 제품 전체조회 : 설계 : (카테고리조회)?cno=3, (전체조회)?cno
    @GetMapping("/all")
    public ResponseEntity< List<ProductDto> > allProducts(
            @RequestParam( defaultValue = "0" ) long cno ){
        List<ProductDto> productDtoList = productService.allProducts( cno );
        return ResponseEntity.status( 200 ).body( productDtoList ); // 200성공과 값 반환
    } // f end

    // 3. 제품 개별조회 : 설계 : ?pno=1
    @GetMapping("/view")
    public ResponseEntity< ProductDto > viewProduct( @RequestParam int pno ){
        ProductDto productDto = productService.viewProduct( pno );
        if( productDto == null ){
            return ResponseEntity.status( 404 ).body( null ); // 404 not found와 null 반환
        }else{
            return ResponseEntity.status( 200 ).body( productDto );
        }
    } // f end

}
