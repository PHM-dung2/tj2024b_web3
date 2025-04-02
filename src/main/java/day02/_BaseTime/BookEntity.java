package day02._BaseTime;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity // 해당 클래스는 DB테이블과 매핑
@Table( name = "day02book" ) // DB테이블명 정의
@Data // 롬복
public class BookEntity extends BaseTime{

    @Id // PRIMARY KEY
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int 도서번호;

    @Column( nullable = false, length = 100 ) // not null, varchar(100)
    private String 도서명;

    @Column( nullable = false, length = 30 ) // not null, varchar(30)
    private String 저자;

    @Column( nullable = false, length = 50 ) // not null, varchar(50)
    private String 출판사;

    @Column
    private int 출판연도;
}
