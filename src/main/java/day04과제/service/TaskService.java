package day04과제.service;

import day04과제.model.dto.TaskDto;
import day04과제.model.entity.TaskEntity;
import day04과제.model.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    // 1. 비품 등록
    @PostMapping
    public TaskDto taskSave(@RequestBody TaskDto taskDto ){
        TaskEntity taskEntity = taskDto.toEntity();
        TaskEntity saveEntity = taskRepository.save( taskEntity );

        if( saveEntity.getId() > 1 ){
            return saveEntity.toDto();
        }else{
            return null;
        } // if end

    } // f end

    // 2. 전체 비품 조회
    public List<TaskDto> taskFindAll(){
        List<TaskEntity> taskEntityList = taskRepository.findAll();

        return taskEntityList.stream()
                .map( TaskEntity::toDto )
                .collect( Collectors.toList() );

    } // f end

    // 3. 개별 비품 조회
    public TaskDto taskFindById( @RequestParam int id ){
        return taskRepository.findById( id )
                .map( TaskEntity::toDto )
                .orElse( null );

    } // f end

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

    // 5. 비품 삭제
    public boolean taskDelete( int id ){
        return taskRepository.findById( id )
                .map( ( entity ) -> {
                    taskRepository.deleteById( id );
                    return true;
                })
                .orElse( false );
    } // f end

    // 6. 페이징 비품 목록 조회
    public List<TaskDto> taskFindByPage( int page, int size ){
        PageRequest pageRequest = PageRequest.of( page-1, size, Sort.by(Sort.Direction.DESC , "id" ) );

        return taskRepository.findAll( pageRequest ).stream()
                .map( TaskEntity::toDto )
                .collect( Collectors.toList() );
    } // f end

}
