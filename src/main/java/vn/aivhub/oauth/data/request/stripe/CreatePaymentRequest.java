package vn.aivhub.oauth.data.request.stripe;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.aivhub.data.tables.pojos.ChargePlan;

@Data
@Accessors(chain = true)
public class CreatePaymentRequest {
  private Long amount;
  private Long quantity;
  private String currency;
  private String name;
  private String successUrl;
  private String cancelUrl;
  private ChargePlan planDetails;
  private Integer subscriptionId;
  private String customerEmail;
  private String customerName;
}
