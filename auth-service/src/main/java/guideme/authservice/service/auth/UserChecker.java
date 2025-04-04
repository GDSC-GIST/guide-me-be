package guideme.authservice.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import guideme.authservice.infrastructure.dto.user.LoginAccessUser;
import guideme.authservice.infrastructure.dto.user.UserInfoChecker;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserChecker {

    private static final String USER_SERVICE_URL = "http://user-service/user/email";
    private static final String USER_EMAIL_ID = "user_email_id";
    private static final String STUDENT_ID = "student_id";
    private static final String USER_INFO_URL = "https://api.idp.gistory.me/oauth/userinfo";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserChecker(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public LoginAccessUser getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return getUserInfoAndConvertAccessEntity(entity);
    }

    private LoginAccessUser getUserInfoAndConvertAccessEntity(HttpEntity<Void> entity) {
        try {
            Map<String, Object> userInfo = getUserSpecificInfo(entity);
            if (userInfo == null) {
                throw new IllegalArgumentException("");
            }
            return createLoginAccessUser(userInfo);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    private Map<String, Object> getUserSpecificInfo(HttpEntity<Void> entity) {
        ResponseEntity<Map> response = restTemplate.exchange(USER_INFO_URL,
                HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    private LoginAccessUser createLoginAccessUser(Map<String, Object> userInfo) {
        String email = (String) userInfo.get(USER_EMAIL_ID);
        String studentId = (String) userInfo.get(STUDENT_ID);
        return new LoginAccessUser(email, studentId);
    }

    public Optional<UserInfoChecker> getUserInfoCheck(String userEmail) {
        try {
            Map<String, Object> body = getRequestBody(userEmail);
            if (body == null || Boolean.FALSE.equals(body.get("isSuccess"))) {
                return Optional.empty();
            }
            return generateUserInfoChecker(body);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Map<String, Object> getRequestBody(String userEmail) {
        String url = USER_SERVICE_URL + "?email=" + userEmail;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
        return response.getBody();
    }

    private Optional<UserInfoChecker> generateUserInfoChecker(Map<String, Object> body) {
        Object data = body.get("data");
        UserInfoChecker user = objectMapper.convertValue(data, UserInfoChecker.class);
        return Optional.of(user);
    }
}
