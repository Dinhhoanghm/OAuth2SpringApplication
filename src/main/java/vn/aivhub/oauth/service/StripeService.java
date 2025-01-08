package vn.aivhub.oauth.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.aivhub.oauth.data.request.stripe.CreatePaymentRequest;
import vn.aivhub.oauth.data.response.stripe.CapturePaymentResponse;
import vn.aivhub.oauth.data.response.stripe.CreatePaymentResponse;
import vn.aivhub.oauth.data.response.stripe.StripeResponse;

import javax.annotation.PostConstruct;

import static vn.aivhub.oauth.data.constant.StripeConstant.*;
import static vn.aivhub.oauth.util.StripeUtil.*;

@Service
@Log4j2
public class StripeService {
  @Value("${spring.stripe.secret-key}")
  private String secretKey;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }

  public StripeResponse createPayment(CreatePaymentRequest createPaymentRequest) {
    SessionCreateParams.LineItem.PriceData.ProductData productData = getStripeProductData(createPaymentRequest);

    SessionCreateParams.LineItem.PriceData priceData = getStripePriceData(createPaymentRequest, productData);


    SessionCreateParams.LineItem lineItem = getStripeLineItem(createPaymentRequest, priceData);

    SessionCreateParams params = getSessionCreateParams(createPaymentRequest, lineItem);

    Session session;
    try {
      session = Session.create(params);
    } catch (StripeException e) {
      e.printStackTrace();
      return StripeResponse
        .builder()
        .status(FAILURE)
        .message("Payment session creation failed")
        .httpStatus(400)
        .data(null)
        .build();
    }

    CreatePaymentResponse responseData = CreatePaymentResponse
      .builder()
      .sessionId(session.getId())
      .sessionUrl(session.getUrl())
      .build();

    return StripeResponse
      .builder()
      .status(SUCCESS)
      .message("Payment session created successfully")
      .httpStatus(200)
      .data(responseData)
      .build();
  }

  public StripeResponse capturePayment(String sessionId) {
    Stripe.apiKey = secretKey;

    try {
      Session session = Session.retrieve(sessionId);
      String status = session.getStatus();

      if (status.equalsIgnoreCase(STRIPE_SESSION_STATUS_SUCCESS)) {
        log.info("Payment successfully captured.");
      }
      CapturePaymentResponse responseData = CapturePaymentResponse
        .builder()
        .sessionId(sessionId)
        .sessionStatus(status)
        .paymentStatus(session.getPaymentStatus())
        .build();

      return StripeResponse
        .builder()
        .status(SUCCESS)
        .message("Payment successfully captured.")
        .httpStatus(200)
        .data(responseData)
        .build();
    } catch (StripeException e) {
      e.printStackTrace();
      return StripeResponse
        .builder()
        .status(FAILURE)
        .message("Payment capture failed due to a server error.")
        .httpStatus(500)
        .data(null)
        .build();
    }
  }

}
