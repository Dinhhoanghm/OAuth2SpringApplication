package vn.aivhub.oauth.config.model.stripe.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StripeCustomerRequest {
  private String name;
  private String email;
}
