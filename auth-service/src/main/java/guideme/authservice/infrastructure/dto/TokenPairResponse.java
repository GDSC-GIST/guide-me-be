package guideme.authservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenPairResponse {
    private String accessToken;
    private String refreshToken;
}
