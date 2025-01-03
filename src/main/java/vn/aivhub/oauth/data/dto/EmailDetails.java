package vn.aivhub.oauth.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmailDetails {
  private String recipient;
  private String body;
  private String subject;
  private String attachment;
}
