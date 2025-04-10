package task.service;

import task.model.dto.TaskDto;
import task.model.entity.TaskEntity;
import task.model.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    // 1. 비품 등록
    /*
    public TaskDto taskSave(@RequestBody TaskDto taskDto ){
        TaskEntity taskEntity = taskDto.toEntity();
        TaskEntity saveEntity = taskRepository.save( taskEntity );

        if( saveEntity.getId() > 1 ){
            return saveEntity.toDto();
        }else{
            return null;
        } // if end

    } // f end
    */

    // 1. 비품 등록 native 쿼리
    public boolean taskSave( TaskDto taskDto ){
        TaskEntity taskEntity = taskDto.toEntity();
        int saveEntity = taskRepository.postByNative(
                taskEntity.getName(),
                taskEntity.getDescription(),
                taskEntity.getQuantity()
        );

        if( saveEntity == 1 ){
            return true;
        }else{
            return false;
        } // if end

    } // f end

    // 2. 전체 비품 조회
    public List<TaskDto> taskFindAll(){
        // List<TaskEntity> taskEntityList = taskRepository.findAll();
        // native 쿼리
        List<TaskEntity> taskEntityList = taskRepository.findAllByNative();

        return taskEntityList.stream()
                .map( TaskEntity::toDto )
                .collect( Collectors.toList() );

    } // f end

    // 3. 개별 비품 조회
    public TaskDto taskFindById( int id ){
        // return taskRepository.findById( id )
        // native 쿼리
        return taskRepository.findByIdByNative( id )
                .map( TaskEntity::toDto )
                .orElse( null );

    } // f end

    /*
    // 4. 비품 수정
    public TaskDto taskUpdate( TaskDto taskDto ){
        Optional<TaskEntity> optional = taskRepository.findById( taskDto.getId() );
        return taskRepository.findById( taskDto.getId() )
                .map( ( entity ) -> {
                    entity.setName( taskDto.getName() );
                    entity.setDescription( taskDto.getDescription() );
                    entity.setQuantity( taskDto.getQuantity() );
                    return entity.toDto();
                })
                .orElse( null );
    } // f end
    */

    // 4. 비품 수정
    public boolean taskUpdate( TaskDto taskDto ){
        int result = taskRepository.updateByNative(
                taskDto.getId(),
                taskDto.getName(),
                taskDto.getDescription(),
                taskDto.getQuantity()
        );

        if( result == 1 ){
            return true;
        } else{
            return false;
        } // if end
    } // f end

    // 5. 비품 삭제
    /*
    public boolean taskDelete( int id ){
        return taskRepository.findById( id )
                .map( ( entity ) -> {
                    taskRepository.deleteById( id );
                    return true;
                })
                .orElse( false );
    } // f end
    */

    public boolean taskDelete( int id ){
        int result = taskRepository.deleteByNative( id );
        if( result == 1 ){
            return true;
        } else{
            return false;
        } // if end
    } // f end

    // 6. 페이징 비품 목록 조회
    public List<TaskDto> taskFindByPage( int page, int size ){
        PageRequest pageRequest = PageRequest.of( page-1, size, Sort.by(Sort.Direction.DESC , "id" ) );

        return taskRepository.findAll( pageRequest ).stream()
                .map( TaskEntity::toDto )
                .collect( Collectors.toList() );
    } // f end


}
