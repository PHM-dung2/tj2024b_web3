package day03._과제3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day03/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // 1. 과정 등록
    @PostMapping("/course")
    public boolean postCource(@RequestBody CourceDto courceDto){
        System.out.println("TaskController.post");
        System.out.println("courceDto = " + courceDto);

        return taskService.postCource( courceDto );
    } // f end

    // 2. 과정 전체 조회
    @GetMapping("/course")
    public List<CourceDto> getCource(){
        System.out.println("TaskController.get");

        return taskService.getCource();
    } // f end

    // 3. 특정 과정에 수강생 등록
    @PostMapping("/student")
    public boolean postStudent(@RequestBody StudentDto studentDto){
        System.out.println("TaskController.postStudent");
        System.out.println("studentDto = " + studentDto);

        return taskService.postStudent( studentDto );
    } // f end

    // 4. 특정 과정에 수강생 전체 조회
    @GetMapping("/student")
    public List<StudentDto> getStudent( @RequestParam int cno ){
        System.out.println("TaskController.getStudent");

        return taskService.getStudent( cno );
    } // f end

}
