package day02._BaseTime;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day02/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    // 1. 등록
    @PostMapping
    public boolean post( @RequestBody BookEntity bookEntity ){
        System.out.println("BookController.post");
        System.out.println("bookEntity = " + bookEntity);

        return bookService.post( bookEntity );
    } // f end

    // 2. 전체조회
    @GetMapping
    public List<BookEntity> get(){
        System.out.println("BookController.get");

        return bookService.get();
    } // f end

    // 3. 수정
    @PutMapping
    public boolean put( @RequestBody BookEntity bookEntity ){
        System.out.println("BookController.put");
        System.out.println("bookEntity = " + bookEntity);

        return bookService.put( bookEntity );
    } // f end

    // 4. 삭제
    @DeleteMapping
    public boolean delete( @RequestParam int 도서번호 ){
        System.out.println("BookController.delete");
        System.out.println("도서번호 = " + 도서번호);

        return bookService.delete( 도서번호 );
    } // f end

}
