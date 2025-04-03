package day03._과제3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @Entity @Builder
@Table( name = "task3cource" )
@NoArgsConstructor
@AllArgsConstructor
public class CourceEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int cno;

    @Column( nullable = false, length = 30 )
    private String cname;

    @OneToMany( mappedBy = "courceEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<StudentEntity> studentEntityList = new ArrayList<>();

    public CourceDto toDto(){
        return CourceDto.builder()
                .cno( this.cno )
                .cname( this.cname )
                .build();
    }
}
