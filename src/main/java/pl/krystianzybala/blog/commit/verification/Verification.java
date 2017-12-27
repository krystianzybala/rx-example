package pl.krystianzybala.blog.commit.verification;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Verification {

    @Getter
    private boolean verified;

    @Getter
    private String reason;

    @Getter
    private String signature;

    @Getter
    private String payload;
}
