package vn.aivhub.oauth.data.request.stripe;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePaymentRequest {
  private Long amount;
  private Long quantity;
  private String currency;
  private String name;
  private String successUrl;
  private String cancelUrl;
}
