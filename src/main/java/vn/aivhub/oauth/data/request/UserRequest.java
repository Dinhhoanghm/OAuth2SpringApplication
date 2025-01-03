package vn.aivhub.oauth.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequest {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String email;

}
