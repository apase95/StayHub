package com.stayhub.discount;

import com.stayhub.booking.BookingRepository;
import com.stayhub.booking.BookingPriceService;
import com.stayhub.booking.dto.BookingPriceQuote;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.ResourceNotFoundException;
import com.stayhub.discount.dto.DiscountValidateRequest;
import com.stayhub.discount.dto.DiscountValidationResponse;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountServiceImpl implements DiscountService {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MIN_TOTAL = BigDecimal.valueOf(0.01);

    private final DiscountCodeRepository discountCodeRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final BookingPriceService bookingPriceService;

    @Override
    public DiscountValidationResponse validate(Long guestId, DiscountValidateRequest request) {
        Property property = propertyRepository.findByIdAndStatus(request.getPropertyId(), PropertyStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        BookingPriceQuote quote = bookingPriceService.calculate(property, request.getCheckInDate(), request.getCheckOutDate());
        AppliedDiscount discount = apply(guestId, request.getCode(), quote.getTotalPrice());
        return DiscountValidationResponse.builder()
                .code(discount.code())
                .subtotalPrice(quote.getTotalPrice())
                .discountAmount(discount.amount())
                .totalPrice(quote.getTotalPrice().subtract(discount.amount()).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    @Override
    public AppliedDiscount apply(Long guestId, String code, BigDecimal subtotalPrice) {
        if (code == null || code.isBlank()) {
            return AppliedDiscount.none();
        }
        DiscountCode discount = discountCodeRepository.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> invalid("Discount code is invalid."));
        validateDiscount(guestId, discount, subtotalPrice);
        return new AppliedDiscount(discount.getId(), discount.getCode(), calculateAmount(discount, subtotalPrice));
    }

    private void validateDiscount(Long guestId, DiscountCode discount, BigDecimal subtotalPrice) {
        Instant now = Instant.now();
        if (!Boolean.TRUE.equals(discount.getActive()) || now.isBefore(discount.getStartsAt()) || now.isAfter(discount.getEndsAt())) {
            throw invalid("Discount code is not active.");
        }
        if (discount.getUsageLimit() != null && discount.getUsedCount() >= discount.getUsageLimit()) {
            throw invalid("Discount code usage limit has been reached.");
        }
        if (discount.getPerUserLimit() != null
                && bookingRepository.countByGuestIdAndDiscountCodeId(guestId, discount.getId()) >= discount.getPerUserLimit()) {
            throw invalid("You have already used this discount code.");
        }
        if (subtotalPrice.compareTo(discount.getMinimumAmount()) < 0) {
            throw invalid("Booking total does not meet the discount minimum amount.");
        }
        if (discount.getType() == DiscountType.PERCENT && discount.getValue().compareTo(ONE_HUNDRED) > 0) {
            throw invalid("Discount percentage is invalid.");
        }
    }

    private BigDecimal calculateAmount(DiscountCode discount, BigDecimal subtotalPrice) {
        BigDecimal amount = switch (discount.getType()) {
            case PERCENT -> subtotalPrice.multiply(discount.getValue()).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            case FIXED -> discount.getValue();
        };
        if (discount.getCap() != null && amount.compareTo(discount.getCap()) > 0) {
            amount = discount.getCap();
        }
        BigDecimal maxDiscount = subtotalPrice.subtract(MIN_TOTAL);
        return amount.min(maxDiscount).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    private BusinessException invalid(String message) {
        return new BusinessException("ERR_INVALID_DISCOUNT", message);
    }
}
