package pl.krystianzybala.blog.commit;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Author {
    @Getter
    private String name;

    @Getter
    private String email;

    @Getter
    private String date;
}
