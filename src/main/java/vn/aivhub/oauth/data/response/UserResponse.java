package vn.aivhub.oauth.data.response;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.aivhub.data.tables.pojos.User;

@Data
@Accessors(chain = true)
public class UserResponse extends User {
  private String company;

}
