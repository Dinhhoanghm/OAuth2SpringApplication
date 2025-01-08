package vn.aivhub.oauth.data.response.stripe;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class CapturePaymentResponse {
  private String sessionId;
  private String sessionStatus;
  private String paymentStatus;
}
