package hexlet.code.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Builder
public class UrlCheck {
    @Setter
    private Long id;
    private Long urlId;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    @Setter
    private Timestamp createdAt;
}
