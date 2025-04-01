package guideme.authservice.domain.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private final String userId;
    private final String userRole;

    @Builder(access = AccessLevel.PACKAGE)
    public User(String userId, String userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
