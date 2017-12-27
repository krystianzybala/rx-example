package pl.krystianzybala.blog.commit;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Tree {
    @Getter
    private String sha;

    @Getter
    private String url;
}
