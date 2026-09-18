package com.stayhub.payment;

import com.stayhub.common.exception.BusinessException;
import com.stayhub.config.VnpayProperties;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class VnpayPaymentService {
    private static final DateTimeFormatter VNPAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final VnpayProperties properties;
    private final Clock clock;

    @Autowired
    public VnpayPaymentService(VnpayProperties properties) {
        this(properties, Clock.systemDefaultZone());
    }

    VnpayPaymentService(VnpayProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    public String buildPaymentUrl(VnpayPaymentRequest request) {
        requireConfigured();

        LocalDateTime now = LocalDateTime.now(clock);
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", properties.getTmnCode());
        params.put("vnp_Amount", request.amount().setScale(0, RoundingMode.HALF_UP).multiply(java.math.BigDecimal.valueOf(100)).toPlainString());
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", request.txnRef());
        params.put("vnp_OrderInfo", request.orderInfo());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", properties.getReturnUrl());
        params.put("vnp_IpAddr", request.ipAddress());
        params.put("vnp_CreateDate", VNPAY_DATE_FORMAT.format(now));
        params.put("vnp_ExpireDate", VNPAY_DATE_FORMAT.format(now.plusMinutes(15)));

        String hashData = toQueryString(params);
        String secureHash = hmacSha512(properties.getHashSecret(), hashData);
        return properties.getPayUrl() + "?" + hashData + "&vnp_SecureHash=" + secureHash;
    }

    public boolean verifySecureHash(Map<String, String> vnpayParams) {
        requireHashSecret();
        String receivedHash = vnpayParams.get("vnp_SecureHash");
        if (!StringUtils.hasText(receivedHash)) {
            return false;
        }
        Map<String, String> signedParams = new TreeMap<>();
        vnpayParams.forEach((key, value) -> {
            if (StringUtils.hasText(value) && !"vnp_SecureHash".equals(key) && !"vnp_SecureHashType".equals(key)) {
                signedParams.put(key, value);
            }
        });
        String expectedHash = hmacSha512(properties.getHashSecret(), toQueryString(signedParams));
        return expectedHash.equalsIgnoreCase(receivedHash);
    }

    String hmacSha512(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create VNPay secure hash", exception);
        }
    }

    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(entry -> StringUtils.hasText(entry.getValue()))
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private void requireConfigured() {
        requireHashSecret();
        if (!StringUtils.hasText(properties.getTmnCode())
                || !StringUtils.hasText(properties.getPayUrl())
                || !StringUtils.hasText(properties.getReturnUrl())) {
            throw new BusinessException("ERR_VNPAY_CONFIG", "VNPay configuration is incomplete.");
        }
    }

    private void requireHashSecret() {
        if (!StringUtils.hasText(properties.getHashSecret())) {
            throw new BusinessException("ERR_VNPAY_CONFIG", "VNPay hash secret is missing.");
        }
    }
}
