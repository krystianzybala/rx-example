package pl.krystianzybala.blog.commit;

import lombok.*;
import pl.krystianzybala.blog.commit.verification.Verification;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Commit {

    @Getter
    private Author author;

    @Getter
    private Committer committer;

    @Getter
    private String message;


    @Getter
    private Tree tree;

    @Getter
    private  String url;

    @Getter
    private int comment_count;

    @Getter
    private Verification verification;
}
