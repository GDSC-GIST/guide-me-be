package guideme.authservice.service.auth;

import guideme.authservice.util.id.IdHolder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IdHolder idHolder;

    public String getLoginUrl() {
        String state = idHolder.generate();
        String nonce = idHolder.generate();
        String clientId = "guide_me";
        String redirectUri = "https://api.guide-me.com/api/auth/callback";

        return String.format(
                "https://idp.gistory.me/authorize?response_type=code" + "&client_id=%s" + "&scope=openid%20profile"
                        + "&state=%s" + "&redirect_uri=%s" + "&prompt=none", clientId, redirectUri, state);
    }

    public String getAccessToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("guide_me", "secret");

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_url", "https://api.guide-me.com/api/auth/callback");

        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.idp.gistory.me/oauth2/token",
                requestEntity, Map.class);

        Map body = response.getBody();
        return (String) body.get("access_token");
    }

    // 회원 정보 조회 후 사용자 정보 유무에 따라서 return 다르게 한다.
}
