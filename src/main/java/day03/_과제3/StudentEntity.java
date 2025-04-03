package day03._과제3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder
@Table( name = "task3student" )
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int sno;

    @Column( nullable = false, length = 30 )
    private String sname;

    @Column
    private int cno;

    @ManyToOne
    private CourceEntity courceEntity;

    public StudentDto toDto(){
        return StudentDto.builder()
               .sno( this.sno )
               .sname( this.sname )
                .cno( this.cno )
               .build();
    }

}
