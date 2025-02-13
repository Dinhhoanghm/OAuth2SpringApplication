package vn.aivhub.oauth.data.response.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CreatePaymentResponse {
  private String sessionId;
  private String sessionUrl;
}
