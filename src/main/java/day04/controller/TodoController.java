package day04.controller;

import day04.model.dto.TodoDto;
import day04.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    // 1. 등록
    @PostMapping
    public TodoDto todoSave( @RequestBody TodoDto todoDto ){
        return todoService.todoSave(todoDto);
    } // f end

    // 2. 전체조회
    @GetMapping
    public List<TodoDto> todoFindAll(){
        return todoService.todoFindAll();
    } // f end

    // 3. 개별 조회
    @GetMapping("/view")
    public TodoDto todoFindById( @RequestParam int id ){
        return todoService.todoFindById( id );
    } // f end

    // 4. 개별 수정
    @PutMapping
    public TodoDto todoUpdate( @RequestBody TodoDto todoDto ){
        return todoService.todoUpdate( todoDto );
    } // f end

    // 5. 개별 삭제
    @DeleteMapping
    public boolean todoDelete( @RequestParam int id ){
        return todoService.todoDelete( id );
    } // f end

    // 6. 전체조회( + 페이징처리 )
    @GetMapping("/page")
    public List<TodoDto> todoFindByPage(
            // @RequestParam( defaultValue = "초기값" ) : 만약에 매개변수값이 존재하지 않으면 초기값 대입
            @RequestParam( defaultValue = "1" ) int page, // page : 현재 조회할 페이지번호, 초기값 = 1
            @RequestParam( defaultValue = "3" ) int size ){ // size : 현재 조회할 페이지당 개수, 초기값 = 3
        return todoService.todoFindByPage( page, size );
    } // f end

}
