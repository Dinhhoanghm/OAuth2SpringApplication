package vn.aivhub.oauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aivhub.oauth.data.request.stripe.CreatePaymentRequest;
import vn.aivhub.oauth.data.response.stripe.StripeResponse;
import vn.aivhub.oauth.service.StripeService;

@RestController
@RequestMapping("/api/v1/stripe")
public class  StripeController {
  private final StripeService stripeService;

  public StripeController(StripeService stripeService) {
    this.stripeService = stripeService;
  }

  @PostMapping("/create-payment")
  public ResponseEntity<StripeResponse> createPayment(@RequestBody CreatePaymentRequest createPaymentRequest) {
    StripeResponse stripeResponse = stripeService.createPayment(createPaymentRequest);
    return ResponseEntity
      .status(stripeResponse.getHttpStatus())
      .body(stripeResponse);
  }

  @GetMapping("/capture-payment")
  public ResponseEntity<StripeResponse> capturePayment(@RequestParam String sessionId) {
    StripeResponse stripeResponse = stripeService.capturePayment(sessionId);
    return ResponseEntity
      .status(stripeResponse.getHttpStatus())
      .body(stripeResponse);
  }
}
