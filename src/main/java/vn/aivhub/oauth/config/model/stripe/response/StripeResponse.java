package vn.aivhub.oauth.config.model.stripe.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class StripeResponse<T> {
  private String status;
  private String message;
  private Integer httpStatus;
  private T data;
}
