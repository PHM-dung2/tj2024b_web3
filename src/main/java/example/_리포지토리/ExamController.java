package example._리포지토리;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day01/jpa")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;
    // 1. C : 등록
    @PostMapping // http://localhost:8080/day01/jpa
    public boolean post(
            @RequestBody ExamEntity examEntity ){
        boolean result = examService.post( examEntity );
        return result;
    } // f end

    // 2. R : 전체 조회
    @GetMapping
    public List<ExamEntity> get(){
        return examService.get();
    } // f end

    // 3. U : 수정
    @PutMapping
    public boolean put( @RequestBody ExamEntity examEntity ){
        boolean result = examService.put2( examEntity );
        return result;
    } // f end

    // 4. D : 삭제
    @DeleteMapping
    public boolean delete( @RequestParam String id ){
        boolean result = examService.delete( id );
        return result;
    } // f end

}
