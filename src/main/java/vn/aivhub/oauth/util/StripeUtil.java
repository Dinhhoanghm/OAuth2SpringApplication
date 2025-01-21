package vn.aivhub.oauth.util;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import vn.aivhub.oauth.config.model.stripe.request.CreatePaymentRequest;
import vn.aivhub.oauth.config.model.stripe.request.StripeCustomerRequest;
import vn.aivhub.oauth.config.model.stripe.request.SubscriptionPaymentRequest;

public class StripeUtil {
  public static SessionCreateParams.LineItem.PriceData.ProductData getStripeProductData(CreatePaymentRequest createPaymentRequest) {
    SessionCreateParams.LineItem.PriceData.ProductData productData =
      SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(createPaymentRequest.getName())
        .build();
    return productData;
  }

  public static SessionCreateParams.LineItem.PriceData.ProductData getStripeProductData(SubscriptionPaymentRequest subscriptionPaymentRequest) {
    SessionCreateParams.LineItem.PriceData.ProductData productData =
      SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(subscriptionPaymentRequest.getName())
        .build();
    return productData;
  }


  public static CustomerCreateParams getStripeCustomerCreateParams(StripeCustomerRequest stripeCustomerRequest) {
    return
      CustomerCreateParams.builder()
        .setName(stripeCustomerRequest.getName())
        .setEmail(stripeCustomerRequest.getEmail())
        .build();
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

  public static SessionCreateParams.LineItem.PriceData getStripePriceData(SubscriptionPaymentRequest subscriptionPaymentRequest,
                                                                          SessionCreateParams.LineItem.PriceData.ProductData productData) {
    SessionCreateParams.LineItem.PriceData priceData =
      SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency(subscriptionPaymentRequest.getCurrency())
        .setUnitAmount(subscriptionPaymentRequest.getAmount())
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
