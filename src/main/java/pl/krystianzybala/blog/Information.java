package pl.krystianzybala.blog;


import lombok.*;
import pl.krystianzybala.blog.commit.Commit;
import pl.krystianzybala.blog.committer.Committer;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Information {

    @Getter
    private String sha;

    @Getter
    private Commit commit;

    @Getter
    private String url;

    @Getter
    private String html_url;

    @Getter
    private String comments_url;

    @Getter
    private Author author;

    @Getter
    private Committer committer;

    @Getter
    private Parent[] parents;

    @Override
    public String toString() {
        return "Information{" +
                "commit=" + commit.getAuthor().getName() +
                '}';
    }
}
