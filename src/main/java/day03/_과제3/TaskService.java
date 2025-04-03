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
        // 1. 학생 dto --> 학생 엔티티 변환
        StudentEntity studentEntity = studentDto.toEntity();
        // 2. 학생 엔티티 save
        StudentEntity saveEntity = studentRepository.save( studentEntity );
        if( saveEntity.getSno() < 1 ){ return false; }
        // 3. 특정한 과정 엔티티 조회
        CourceEntity courceEntity = courceRepository.findById( studentDto.getCno() ).orElse( null );
        if( courceEntity == null ){ return false; }
        // 4. 등록된 학생 엔티티의 특정한 과정 엔티티 대입<FK대입>
        saveEntity.setCourceEntity( courceEntity ); // 단방향 멤버변수에 과정엔티티 대입하기( fk 대입 )

        return true;
    } // f end

    // 4. 특정 과정에 수강생 전체 조회
    public List<StudentDto> getStudent( int cno ){
        System.out.println("TaskService.getStudent");
        // 1. cno 이용하여 과정 엔티티 찾기
        CourceEntity courceEntity = courceRepository.findById( cno ).orElse( null );
        if( courceEntity == null ){ return null; }
        // 2. 조회한 과정 엔티티 안에 찹조중인 학생 목록
        List<StudentEntity> studentEntityList = courceEntity.getStudentEntityList();
        // 3. dto 변환
        List<StudentDto> studentDtoList = studentEntityList.stream()
                .map( entity -> entity.toDto() )
                .collect( Collectors.toList() );
        // 4.
        return studentDtoList;

//        List<StudentEntity> studentEntityList = studentRepository.findAll();
//
//        return studentEntityList.stream()
//                .map( entity -> entity.toDto() )
//                .filter( entity -> entity.getCno() == cno )
//                .collect( Collectors.toList() );

    } // f end
}
