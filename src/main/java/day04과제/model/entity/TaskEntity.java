package day04과제.model.entity;

import day04과제.model.dto.TaskDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "product" )
public class TaskEntity extends BaseTime{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private String name;
    private String description;
    private int quantity;

    public TaskDto toDto(){
        return TaskDto.builder()
               .id( this.id )
               .name( this.name )
               .description( this.description )
               .quantity( this.quantity )
                .createdate( this.getCreatedate() )
                .updatedate( this.getUpdatedate() )
               .build();
    } // f end

}
