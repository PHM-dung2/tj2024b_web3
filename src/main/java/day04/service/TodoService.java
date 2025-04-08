package day04.service;

import day04.model.dto.TodoDto;
import day04.model.entity.TodoEntity;
import day04.model.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    // 1. 등록
    public TodoDto todoSave( TodoDto todoDto ){
        // 1. dto를 entity로 변환하기
        TodoEntity todoEntity = todoDto.toEntity();
        // 2. entity를 save(영속화/db레코드 매칭/등록)한다.
        TodoEntity saveEntity = todoRepository.save( todoEntity );
        // 3. save로부터 변환된 엔티티(영속화)된 결과가 존재하면
        if( saveEntity.getId() >= 1 ){
            return saveEntity.toDto(); // entity를 dto로 변환하여 반환
        }else{ // 결과가 존재하지 않으면
            return null; // null 반환
        } // f end

    } // f end

    // 2. 전체조회
    public List<TodoDto> todoFindAll(){
        // 1. 모든 entity 조회, findAll()
        List<TodoEntity> todoEntityList = todoRepository.findAll();
        /*
        // [방법1] 일반 반복분 ================================================================================= //
        // 2. 모든 entity 리스트를 dto 리스트로 변환
        List<TodoDto> todoDtoList = new ArrayList<>(); // 2-1 : dto 리스트 생성한다.
        for( int index = 0; index < todoEntityList.size(); index++ ){ // 2-2 : entity 리스트로 조회
            TodoDto todoDto = todoEntityList.get( index ).toDto(); // 2-3 : index번쨰 entity를 dto로 변환
            todoDtoList.add( todoDto ); // 2-4 : dto 리스트에 저장
        } // for end
        // 3. 결과 반환
        return todoDtoList;
        */
        // [방법2] stream ================================================================================= //
        return todoEntityList.stream()
                .map( TodoEntity::toDto )
                .collect( Collectors.toList() );

    } // f end

    // 3. 개별 조회
    public TodoDto todoFindById( int id ){
        /*
        // [방법1] 일반적인 ================================================================================= //
        // 1. pk( 식별변호 ) 이용한 entity 조회하기,  findById()
        // Optional 클래스 : null 제어하는 메소드들을 제공하는 클래스
        Optional< TodoEntity > optional = todoRepository.findById( id );
        // 2. 조회한 결과가 존재하면, isPresent
        if( optional.isPresent() ){
            // 3. Opional에서 entity 꺼내기
            TodoEntity todoEntity = optional.get();
            // 4. dto로 변환
            TodoDto todoDto = todoEntity.toDto();
            // 5. 반환
            return todoDto;
        }
        return null;
        */

        // [방법2] stream ================================================================================= //
        return todoRepository.findById( id )
                .map( TodoEntity::toDto ) // optional의 데이터가 null 이 아니면 map 실행
                .orElse( null ); // optional의 데이터가 null이면 null 반환
    } // f end

    // 4. 개별 수정 + @Transactional
    public TodoDto todoUpdate( TodoDto todoDto ){
        // [방법1] 일반적인 ================================================================================= //
        /*
        // 1. 수정할 엔티티를 조회한다.
        Optional< TodoEntity > optional = todoRepository.findById( todoDto.getId() );
        // 2. 존재하면 수정하고 존재하지 않으면 null 반환, .isPresent()
        if( optional.isPresent() ){
            // 3. 엔티티 꺼내기
            TodoEntity todoEntity = optional.get();
            // 4. dpsxlxl tnwjdgkrl
            todoEntity.setTitle( todoDto.getTitle() );
            todoEntity.setContent( todoDto.getContent() );
            todoEntity.setDone( todoDto.isDone() ); // boolean의 getter은 isXXX() 사용
            return todoEntity.toDto();
        }
        return null;
        */

        // [방법2] stream ================================================================================= //
        return todoRepository.findById( todoDto.getId() )
                // findById 결과의 optional 데이터가 존재하면
                .map( ( entity ) -> { // 람다식 함수, () -> {}
                            entity.setTitle( todoDto.getTitle() );
                            entity.setContent( todoDto.getContent() );
                            entity.setDone( todoDto.isDone() );
                            return entity.toDto();
                })
                .orElse( null ); // findById 결과의 optional 데이터가 존재하지 않으면 null 반환

    } // f end

    // 5. 개별 삭제
    public boolean todoDelete( int id ){
        /*
        // [방법1] 일반적인 ================================================================================= //
        // 1. id를 이용하여 엔티티 (존재여부) 조회하기
            // findById 반환타입 : Optional vs existById : boolean
        boolean result = todoRepository.existsById( id );
        // 2. 만약에 존재하면
        if( result == true ){
            // 3. 영속성 제거, deleteById( pk번호 )
            todoRepository.deleteById( id );
            return true; // 삭제 성공
        }
        return false; // 존재하지 않으면 삭제 취소
        */

        // [방법2] stream ================================================================================= //
        return todoRepository.findById( id )
                .map( (entity) -> {
                    todoRepository.deleteById( id );
                    return true;
                })
                .orElse( false );
    } // f end

    // 6. 전체조회( + 페이징처리 )
    public List<TodoDto> todoFindByPage( int page, int size ){
        // page : 현재 조회중인 페이지 번호
        // size : 페이지 1개당 조회할 자료 개수
        // 1. PageRequest 클래스 이용한 페이징처리 설정
            // PageRequest( 조회할페이지번호, 자료개수 , 정렬[선택] )
            // - 조회할페이지번호는 1페이지가 0부터 시작
            // - 페이지당 조회할 자료 개수
            // - Sort.by : 정렬
                // - Sort.by( Sort.Direction.ASC, "필드명" ) : 오름차순
                // - Sort.by( Sort.Direction.DESC, "필드명" ) : 내림차순( 최신순 )
        PageRequest pageRequest = PageRequest.of( page-1, size, Sort.by( Sort.Direction.DESC, "id" ) );
        // [방법2] 2. stream 이용한 조회 리스트를 dto로 변환한다.
        return todoRepository.findAll( pageRequest ).stream()
                .map( TodoEntity::toDto )
                .collect( Collectors.toList() );

        /*
        // 2. pageRequest 객체를 findXX에 매개변수로 대입한다. .finaAll( 페이징객체 ); , 반환타입 : Page타입
        Page< TodoEntity > todoEntityPage  = todoRepository.findAll( pageRequest );
        // 3. page타입의 entity를 dto로 변환
        List< TodoDto > todoDtoList = new ArrayList<>();
        for( int i = 0; i < todoEntityPage.getContent().size() ; i++ ){
            TodoDto todoDto = todoEntityPage.getContent().get( i ).toDto();
        } // for end
        */
    } // f end

    // 7. 제목 검색 조회( 입력한 값이 *일치*한 제목 조회 )
    public List<TodoDto> search1( String title ){
        // [1 쿼리메소드 ] JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
        return todoRepository.findByTitle( title )
                .stream().map( TodoEntity::toDto )
                .collect( Collectors.toList() );
        // [2 네이티브쿼리 ] JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
//        return todoRepository.findByTitleNative( title )
//                .stream().map( TodoEntity::toDto )
//                .collect( Collectors.toList() );

    } // f end

    // 8. 제목 검색 조회( 입력한 값이 *포함*된 제목 조회 )
    public List<TodoDto> search2( String keyword ){
        // [1 쿼리메소드 ] JPA 지포지토리에서 내가 만든 추상메소드 사용한다.
        return todoRepository.findByTitleContaining( keyword )
                .stream().map( TodoEntity::toDto )
                .collect( Collectors.toList() );
        // [2 네이티브쿼리 ] JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
//        return todoRepository.findByTitleNativeSearch( keyword )
//                .stream().map( TodoEntity::toDto )
//                .collect(Collectors.toList());
    } // f end

}
