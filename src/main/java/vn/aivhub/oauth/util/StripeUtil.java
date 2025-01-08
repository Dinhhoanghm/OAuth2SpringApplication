package vn.aivhub.oauth.util;

import com.stripe.param.checkout.SessionCreateParams;
import vn.aivhub.oauth.data.request.stripe.CreatePaymentRequest;

public class StripeUtil {
  public static SessionCreateParams.LineItem.PriceData.ProductData getStripeProductData(CreatePaymentRequest createPaymentRequest) {
    SessionCreateParams.LineItem.PriceData.ProductData productData =
      SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(createPaymentRequest.getName())
        .build();
    return productData;
  }

  public static SessionCreateParams.LineItem.PriceData getStripePriceData(CreatePaymentRequest createPaymentRequest,
                                                                          SessionCreateParams.LineItem.PriceData.ProductData productData) {
    SessionCreateParams.LineItem.PriceData priceData =
      SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency(createPaymentRequest.getCurrency())
        .setUnitAmount(createPaymentRequest.getAmount())
        .setProductData(productData)
        .build();
    return priceData;
  }


  public static SessionCreateParams getSessionCreateParams(CreatePaymentRequest createPaymentRequest,
                                                           SessionCreateParams.LineItem lineItem) {
    SessionCreateParams params =
      SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(createPaymentRequest.getSuccessUrl())
        .setCancelUrl(createPaymentRequest.getCancelUrl())
        .addLineItem(lineItem)
        .build();
    return params;
  }

  public static SessionCreateParams.LineItem getStripeLineItem(CreatePaymentRequest createPaymentRequest,
                                                               SessionCreateParams.LineItem.PriceData priceData) {
    SessionCreateParams.LineItem lineItem =
      SessionCreateParams
        .LineItem.builder()
        .setQuantity(createPaymentRequest.getQuantity())
        .setPriceData(priceData)
        .build();
    return lineItem;
  }
}
