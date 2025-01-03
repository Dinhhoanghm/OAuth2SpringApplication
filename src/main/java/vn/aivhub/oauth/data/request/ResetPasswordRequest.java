package vn.aivhub.oauth.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResetPasswordRequest {
  private String token;
  private String newPassword;
}
