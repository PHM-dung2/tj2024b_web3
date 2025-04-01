package example._엔티티;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Task1todo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    @Column( nullable = true )
    private String title;

    @Column( nullable = true )
    private boolean state;

    @Column( nullable = true )
    private LocalDate createat;

    @Column( nullable = true )
    private LocalDateTime updateat;

}
