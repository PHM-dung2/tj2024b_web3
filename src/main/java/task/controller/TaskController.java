package task.controller;

import task.model.dto.TaskDto;
import task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/product")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // 1. 비품 등록
    @PostMapping
    public boolean taskSave(@RequestBody TaskDto taskDto ){
        return taskService.taskSave( taskDto );
    } // f end

    // 2. 전체 비품 조회
    @GetMapping
    public List<TaskDto> taskFindAll(){
        return taskService.taskFindAll();
    } // f end

    // 3. 개별 비품 조회
    @GetMapping("/view")
    public TaskDto taskFindById( @RequestParam int id ){
        return taskService.taskFindById( id );
    } // f end

    // 4. 비품 수정
    @PutMapping
    public boolean taskUpdate( @RequestBody TaskDto taskDto ){
        return taskService.taskUpdate( taskDto );
    } // f end

    // 5. 비품 삭제
    @DeleteMapping
    public boolean taskDelete( @RequestParam int id ){
        return taskService.taskDelete( id );
    } // f end

    // 6. 페이징 비품 목록 조회
    @GetMapping("/page")
    public List<TaskDto> taskFindByPage(
            @RequestParam( defaultValue = "1" ) int page,
            @RequestParam( defaultValue = "3" ) int size ){
        return taskService.taskFindByPage( page, size );
    } // f end

}
