package vn.aivhub.oauth.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
  private LocalDateTime expirationTime;
}
