package pl.krystianzybala.blog;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Parent {

    @Getter
    private String sha;

    @Getter
    private String url;

    @Getter
    private String html_url;
}
