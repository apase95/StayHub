package com.stayhub.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.stayhub.config.VnpayProperties;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VnpayPaymentServiceTest {
    private VnpayPaymentService service;

    @BeforeEach
    void setUp() {
        VnpayProperties properties = new VnpayProperties();
        properties.setTmnCode("TESTCODE");
        properties.setHashSecret("secret-key");
        properties.setPayUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
        properties.setReturnUrl("http://localhost:8080/payments/vnpay/return");
        properties.setIpnUrl("http://localhost:8080/api/v1/payments/vnpay/ipn");
        Clock clock = Clock.fixed(Instant.parse("2026-01-02T03:04:05Z"), ZoneId.of("UTC"));
        service = new VnpayPaymentService(properties, clock);
    }

    @Test
    void buildPaymentUrlCreatesSignedSortedParams() {
        String url = service.buildPaymentUrl(new VnpayPaymentRequest(
                "BOOKING-1",
                new BigDecimal("123456"),
                "StayHub booking #1",
                "127.0.0.1"
        ));

        Map<String, String> params = params(url);
        assertThat(url).startsWith("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?");
        assertThat(params.get("vnp_TmnCode")).isEqualTo("TESTCODE");
        assertThat(params.get("vnp_Amount")).isEqualTo("12345600");
        assertThat(params.get("vnp_CreateDate")).isEqualTo("20260102030405");
        assertThat(params.get("vnp_ExpireDate")).isEqualTo("20260102031905");
        assertThat(params.get("vnp_SecureHash")).hasSize(128);
        assertThat(service.verifySecureHash(params)).isTrue();
    }

    @Test
    void verifySecureHashRejectsTamperedParams() {
        String url = service.buildPaymentUrl(new VnpayPaymentRequest(
                "BOOKING-1",
                new BigDecimal("123456"),
                "StayHub booking #1",
                "127.0.0.1"
        ));
        Map<String, String> params = params(url);
        params.put("vnp_Amount", "99999900");

        assertThat(service.verifySecureHash(params)).isFalse();
    }

    private Map<String, String> params(String url) {
        String query = URI.create(url).getRawQuery();
        return Arrays.stream(query.split("&"))
                .map(part -> part.split("=", 2))
                .collect(Collectors.toMap(
                        pair -> decode(pair[0]),
                        pair -> pair.length > 1 ? decode(pair[1]) : ""
                ));
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
