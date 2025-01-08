package vn.aivhub.oauth.data.response;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.aivhub.data.tables.pojos.User;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
  private LocalDateTime expirationTime;
  private User user;
}
