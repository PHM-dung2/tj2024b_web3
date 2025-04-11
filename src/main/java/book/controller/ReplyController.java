package book.controller;

import book.model.dto.ReplyDto;
import book.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReplyController {
    private final ReplyService replyService;

    // 1. 리뷰 작성
    @PostMapping
    public ReplyDto replySave( @RequestBody ReplyDto replyDto ){
        return replyService.replySave( replyDto );
    } // f end

    @GetMapping
    // 2. 책 별 리뷰 전체 조회
    public List<ReplyDto> replyFindAll( @RequestParam int bno ){
        return replyService.replyFindAll( bno );
    } // f end

    @PutMapping("/delete")
    // 3. 리뷰 삭제
    public boolean replyDelete( @RequestBody ReplyDto replyDto ){
        return replyService.replyDelete( replyDto );
    } // f end

}
