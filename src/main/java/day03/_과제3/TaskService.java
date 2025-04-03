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

        // 1. DTO --> entity 변환
        CourceEntity saveEntity = courceDto.toEntity();
        // 2. 해당 entity를 .save 하기
        courceRepository.save( saveEntity ); // 반환값 : 영속된 객체
        // 3. 결과 확인
        if( saveEntity.getCno() > 0 ){ return false; } // 만약에 영속 결과(과정번호)가 없다면 false

        return true;
    } // f end

    // 2. 과정 전체 조회
    public List<CourceDto> getCource(){
        System.out.println("TaskService.getCource");
        List<CourceEntity> courceEntityList = courceRepository.findAll();

        return courceEntityList.stream()
                .map( entity -> entity.toDto() )
                .collect( Collectors.toList() );
    }
    // 3. 특정 과정에 수강생 등록
    public boolean postStudent( StudentDto studentDto ){
        System.out.println("TaskService.postStudent");
        System.out.println("studentDto = " + studentDto);

        CourceEntity courceEntity = courceRepository.findById( studentDto.getCno() ).orElse( null );
        if( courceEntity == null ){ return false; }
        StudentEntity studentEntity = studentDto.toEntity();
        studentEntity.setCourceEntity( courceEntity );
        studentRepository.save( studentEntity );

        return true;
    } // f end

    // 4. 특정 과정에 수강생 전체 조회
    public List<StudentDto> getStudent( int cno ){
        System.out.println("TaskService.getStudent");
        List<StudentEntity> studentEntityList = studentRepository.findAll();

        return studentEntityList.stream()
                .map( entity -> entity.toDto() )
                .filter( entity -> entity.getCno() == cno )
                .collect( Collectors.toList() );

    } // f end
}
