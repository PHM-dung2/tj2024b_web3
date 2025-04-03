package day03._자바참조관계;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
public class Board {
    private int 게시물번호;
    private String 게시물제목;
    private String 게시물내용;
    // Category 타입으로 멤버변수 선언 가능
    private Category category; // FK, 단방향 참조
    // 양방향 참조, 하나의 게시물이 여러개 댓글을 참조한다.
    @ToString.Exclude // 해당 필드는 toString 메소드에 재외
    private List<Reply> replyList;


}
