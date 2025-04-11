package book.service;

import book.model.dto.ReplyDto;
import book.model.entity.BookEntity;
import book.model.entity.ReplyEntity;
import book.model.repository.BookRepository;
import book.model.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final BookRepository bookRepository;
    private final ReplyRepository replyRepository;

    // 1. 리뷰 작성
    public ReplyDto replySave( ReplyDto replyDto ){
        try{
            if( replyDto.getRpwd() == null ){ return null; }
            else{
                BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
                String hashpwd = pwdEncoder.encode( replyDto.getRpwd() );
                replyDto.setRpwd( hashpwd );
            }

            ReplyEntity replyEntity = replyDto.toEntity();
            ReplyEntity saveEntity = replyRepository.save( replyEntity );
            if( saveEntity.getRno() < 1 ){ return null; }

            BookEntity bookEntity = bookRepository.findById( replyDto.getBno() ).orElse( null );
            if( bookEntity == null ){ return null; }
            saveEntity.setBookEntity( bookEntity );

            return saveEntity.toDto();
        }catch ( Exception e ){ System.out.println( e ); }

        return null;
    } // f end

    // 2. 책 별 리뷰 전체 조회
    public List<ReplyDto> replyFindAll( int bno ){
        BookEntity bookEntity = bookRepository.findById( bno ).orElse( null );
        if( bookEntity == null ){ return null; }

        List<ReplyEntity> replyEntityList = bookEntity.getReplyEntityList();
        return replyEntityList.stream()
                .map( (entity) -> entity.toDto() )
                .collect( Collectors.toList() );
    } // f end

    // 비밀번호 매칭 함수
    public boolean matchPwd( int rno, String rpwd ){
        String pwd = replyRepository.findPwd( rno );
        if( pwd == null ){ return false; }
        BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
        boolean result = pwdEncoder.matches( rpwd, pwd );
        if( !result ){ return false; }
        return true;
    } // f end

    // 3. 리뷰 삭제
    public boolean replyDelete( ReplyDto replyDto ){
        boolean result = matchPwd( replyDto.getRno(), replyDto.getRpwd() );
        if( !result ){ return false; }

        return replyRepository.findById( replyDto.getRno() )
                .map( (entity) -> {
                    replyRepository.deleteById( replyDto.getRno() );
                    return true;
                })
                .orElse( false );
    } // f end

}
