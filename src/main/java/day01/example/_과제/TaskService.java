package day01.example._과제;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskEntityRepository taskEntityRepository;

    // 1. 도서 등록
    public boolean post( TaskEntity taskEntity ){
        TaskEntity result = taskEntityRepository.save( taskEntity );
        if( result == null ){ return false; }
        return true;
    } // f end

    // 2. 도서 전체 조회
    public List<TaskEntity> get(){
        List<TaskEntity> result = taskEntityRepository.findAll();
        return result;
    } // f end

    // 3. 특정 도서 정보 수정
    @Transactional
    public boolean put( TaskEntity taskEntity ){
        Optional<TaskEntity> result = taskEntityRepository.findById( taskEntity.getId() );
        if( result.isPresent() ){
            TaskEntity entity = result.get();
            entity.setName(taskEntity.getName());
            entity.setWriter(taskEntity.getWriter());
            entity.setPublisher(taskEntity.getPublisher());
            entity.setYear(taskEntity.getYear());
            return true;
        }
        return false;
    } // f end

    // 4. 특정 도서 삭제
    public boolean delete( Integer id ){
        taskEntityRepository.deleteById( id );
        return true;
    } // f end

}
