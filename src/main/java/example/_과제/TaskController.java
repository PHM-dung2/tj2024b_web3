package example._과제;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // 1. 도서 등록
    @PostMapping
    public boolean post( @RequestBody TaskEntity taskEntity ){
        boolean result = taskService.post( taskEntity );
        return result;
    } // f end

    // 2. 도서 전체 조회
    @GetMapping
    public List<TaskEntity> get(){
        return taskService.get();
    } // f end

    // 3. 특정 도서 정보 수정
    @PutMapping
    public boolean put( @RequestBody TaskEntity taskEntity ){
        boolean result = taskService.put( taskEntity );
        return result;
    } // f end

    // 4. 특정 도서 삭제
    @DeleteMapping
    public boolean delete( @RequestParam Integer id ){
        boolean result = taskService.delete( id );
        return result;
    } // f end

}
