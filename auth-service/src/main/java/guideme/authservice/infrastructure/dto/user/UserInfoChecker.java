package guideme.authservice.infrastructure.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserInfoChecker {

    private String userId;
    private String nickname;
    private String email;
    private int semester;
}
