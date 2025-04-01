package example._과제;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Table( name = "book" )
@Data
public class TaskEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    @Column( nullable = false )
    private String name;
    @Column( nullable = false )
    private String writer;
    @Column( nullable = false )
    private String publisher;
    @Column( nullable = false )
    private String year;
}
