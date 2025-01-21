package vn.aivhub.oauth.config.model.stripe.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubscriptionPaymentRequest {
  private Long amount;
  private Long quantity;
  private String currency;
  private String name;
  private String successUrl;
  private String cancelUrl;
  private vn.aivhub.data.tables.pojos.ChargePlan planDetails;
  private Integer subscriptionId;
  private String customerEmail;
  private String customerName;
}
