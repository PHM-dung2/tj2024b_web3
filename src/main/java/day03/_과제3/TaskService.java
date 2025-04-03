package day03._과제3;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final CourceRepository courceRepository;
    private final StudentRepository studentRepository;

    // 1. 과정 등록
    public boolean postCource( CourceDto courceDto ){
        System.out.println("TaskService.post");
        System.out.println("courceDto = " + courceDto);

        CourceEntity courceEntity = courceDto.toEntity();
        courceRepository.save( courceEntity );

        return true;
    } // f end

    // 2. 과정 전체 조회
    public List<CourceDto> getCource(){
        System.out.println("TaskService.getCource");
        List<CourceEntity> courceEntityList = courceRepository.findAll();

        return courceEntityList.stream()
                .map( cource -> cource.toDto() )
                .collect( Collectors.toList() );
    }
    // 3. 특정 과정에 수강생 등록
    public boolean postStudent( StudentDto studentDto ){
        System.out.println("TaskService.postStudent");
        System.out.println("studentDto = " + studentDto);

        StudentEntity studentEntity = studentDto.toEntity();
        studentRepository.save( studentEntity );

        return true;
    } // f end

    // 4. 특정 과정에 수강생 전체 조회
    public List<StudentDto> getStudent( int cno ){
        System.out.println("TaskService.getStudent");
        List<StudentEntity> studentEntityList = studentRepository.findAll();

        return studentEntityList.stream()
                .map( student -> student.toDto() )
                .filter( student -> student.getCno() == cno )
                .collect( Collectors.toList() );

    } // f end
}
