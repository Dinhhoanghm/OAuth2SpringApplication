package vn.aivhub.oauth.config.model.stripe.response;


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
