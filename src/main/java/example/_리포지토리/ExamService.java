package example._리포지토리;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamService {

    // 1. 조작할 엔티티리포지토리의 인터페이스
    private final ExamEntityRepository examEntityRepository;

    public boolean post( ExamEntity examEntity ){
        System.out.println("ExamService.post");
        System.out.println("examEntity = " + examEntity);
        // 2. 현재 엔티티를 저장하기
        ExamEntity examEntity2 = examEntityRepository.save( examEntity );
        // examEntity : 영속 전 객체
        // examEntity2 : 영속된 객체, 매핑
        return true;
    } // f end

    // 2. R : 전체 조회
    public List<ExamEntity> get(){
        // 3. 모든 엔티티를 리스트로 반환, .findAll()
        List<ExamEntity> examEntityList = examEntityRepository.findAll();
        return examEntityList;
    }

    // 3. U : 수정
    public boolean put( ExamEntity examEntity ){
        // 4. 현재 엔티티의 ID가 존재하면 UPDATE, 없으면 INSERT
        examEntityRepository.save( examEntity );
        return true;
    } // f end

    // 3-1. 수정 : 존재하는 ID만 수정
    @Transactional // 아래 메소드에서 하나라도 sql 문제 발생하면 전체 취소
    public boolean put2( ExamEntity examEntity ){
        // 1. id에 해당하는 엔티티 찾기
        Optional< ExamEntity > optionalExamEntity =
                examEntityRepository.findById( examEntity.getId() );
        // 2. 만약 조회한 엔티티가 있으면
        if( optionalExamEntity.isPresent() ){
            // 3. Optional 객체에서 엔티티 꺼내기
            ExamEntity entity = optionalExamEntity.get();
            entity.setName( entity.getName() );
            entity.setKor( entity.getKor() );
            entity.setEng( entity.getEng() );
            return true;
        } // if end
        return false;
    } // f end

    // 4. D : 삭제
    public boolean delete( String id ){
        examEntityRepository.deleteById( id );
        System.out.println( examEntityRepository.count() );
        System.out.println( examEntityRepository.existsById( id ) );
        return true;
    } // f end

}
