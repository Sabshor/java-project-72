package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public final class Url {
    private Long id;
    @ToString.Include
    private String name;
    //private LocalDate createdAt;
    //private Date createdAt;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
