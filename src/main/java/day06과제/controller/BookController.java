package day06과제.controller;

import day06과제.model.dto.BookDto;
import day06과제.model.entity.BookEntity;
import day06과제.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BookController {
    private final BookService bookService;

//     {
//      "btitle": "책제목",
//      "bwriter" : "작가",
//      "bcontent" : "책설명",
//      "bpwd" : "1234"
//    }
    // 1. 책 추천 등록
    @PostMapping
    public BookDto bookSave( @RequestBody BookDto bookDto ){
        return bookService.bookSave( bookDto );
    } // f end

    // 2. 책 추천 목록 조회
    @GetMapping
    public List<BookDto> bookFindAll(){
        return bookService.bookFindAll();
    } // f end

    // 3. 책 추천 상세 조회
    @GetMapping("/view")
    public BookDto bookFindById( @RequestParam int bno ){
        return bookService.bookFindById( bno );
    } // f end

//    {
//        "bno" : 3,
//            "btitle": "책제목 수정",
//            "bwriter" : "작가 수정",
//            "bcontent" : "책설명 수정",
//            "bpwd" : "1234"
//    }
    // 4. 책 추천 수정
    @PutMapping
    public BookDto bookUpdate( @RequestBody BookDto bookDto ){
        return bookService.bookUpdate( bookDto );
    } // f end

    // 5. 책 추천 삭제
    @PutMapping("/delete")
    public boolean bookDelete( @RequestBody BookDto bookDto ){
        return bookService.bookDelete( bookDto );
    } // f end

}
