package pl.krystianzybala.blog;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Author {
    @Getter
    private String login;

    @Getter
    private long id;

    @Getter
    private String avatar_url;

    @Getter
    private String gravatar_id;

    @Getter
    private String url;

    @Getter
    private String html_url;

    @Getter
    private String followers_url;

    @Getter
    private String following_url;

    @Getter
    private String gists_url;

    @Getter
    private String starred_url;

    @Getter
    private String subscriptions_url;

    @Getter
    private String organizations_url;

    @Getter
    private String repos_url;

    @Getter
    private String events_url;

    @Getter
    private String received_events_url;

    @Getter
    private String type;

    @Getter
    private boolean site_admin;
}
