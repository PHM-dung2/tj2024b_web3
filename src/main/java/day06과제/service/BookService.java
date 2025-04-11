package day06과제.service;

import day06과제.model.dto.BookDto;
import day06과제.model.entity.BookEntity;
import day06과제.model.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    // 1. 책 추천 등록
    public BookDto bookSave( BookDto bookDto ){
        try{
            if( bookDto.getBpwd() == null ){ return null; }
            else{
                BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
                String hashPwd = pwdEncoder.encode( bookDto.getBpwd() );
                bookDto.setBpwd( hashPwd );
            } // if end

            BookEntity bookEntity = bookDto.toEntity();
            BookEntity saveEntity = bookRepository.save( bookEntity );
            if( saveEntity.getBno() >= 1 ){
                return saveEntity.toDto();
            }
        }catch ( Exception e ){ System.out.println( e ); }
        return null;
    } // f end

    // 2. 책 추천 목록 조회
    public List<BookDto> bookFindAll(){
        List<BookEntity> bookEntityList = bookRepository.findAll();

        return bookEntityList.stream()
                .map( BookEntity::toDto )
                .collect( Collectors.toList() );
    } // f end

    // 3. 책 추천 상세 조회
    public BookDto bookFindById( int bno ){
        return bookRepository.findById( bno )
                .map( BookEntity::toDto )
                .orElse( null );
    } // f end

    // 비밀번호 매칭 함수
    public boolean matchPwd( int bno, String bpwd ){
        String pwd = bookRepository.findPwd( bno );
        if( pwd == null ){ return false; }
        BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
        boolean result = pwdEncoder.matches( bpwd, pwd );
        if( !result ){ return false; }
        return true;
    } // f end

    // 4. 책 추천 수정
    public BookDto bookUpdate( BookDto bookDto ){
        // 비밀번호 매칭 함수
        boolean result = matchPwd( bookDto.getBno(), bookDto.getBpwd() );
        if( !result ){ return null; }

        return bookRepository.findById( bookDto.getBno() )
                .map( (entity) -> {
                    entity.setBtitle( bookDto.getBtitle() );
                    entity.setBwriter( bookDto.getBwriter() );
                    entity.setBcontent( bookDto.getBcontent() );
                    return entity.toDto();
                })
                .orElse( null );
    } // f end

    // 5. 책 추천 삭제
    public boolean bookDelete( BookDto bookDto ){
        // 비밀번호 매칭 함수
        boolean result = matchPwd( bookDto.getBno(), bookDto.getBpwd() );
        if( !result ){ return false; }

        return bookRepository.findById( bookDto.getBno() )
                .map( (entity) -> {
                    bookRepository.deleteById( bookDto.getBno() );
                    return true;
                })
                .orElse( false );
    } // f end

}
