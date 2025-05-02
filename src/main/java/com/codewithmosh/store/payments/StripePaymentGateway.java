package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.orders.OrderItem;
import com.codewithmosh.store.orders.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${websiteUrl}")
    private String websiteUrl;
    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var paymentIntentData = SessionCreateParams.PaymentIntentData.builder()
                    .putMetadata("orderId", order.getId().toString())
                    .build();
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancelled")
                    .setPaymentIntentData(paymentIntentData);

            order.getItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()) {

                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));


                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));

                default -> Optional.empty();

            };


        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature");
        }

    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe object.")
        );
        var paymentIntent = (PaymentIntent) stripeObject;

        var metadata = paymentIntent.getMetadata();
        System.out.println("Metadata: " + metadata);

        if (!metadata.containsKey("orderId")) {
            throw new PaymentException("Missing orderId in Stripe metadata.");
        }

        return Long.valueOf(metadata.get("orderId"));

    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                        createPriceData(item)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setProductData(
                        createProductData(item)
                )
                .setUnitAmountDecimal((item.getUnitPrice()).multiply(BigDecimal.valueOf(100)))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
