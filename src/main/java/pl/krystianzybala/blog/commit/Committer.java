package pl.krystianzybala.blog.commit;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Committer {
    @Getter
    private String name;


    @Getter
    private String email;

    @Getter
    private String date;
}
