package task.model.entity;

import task.model.dto.TaskDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "product" )
public class TaskEntity{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private String name;
    private String description;
    private int quantity;
    private String createdate;
    private String updatedate;

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
