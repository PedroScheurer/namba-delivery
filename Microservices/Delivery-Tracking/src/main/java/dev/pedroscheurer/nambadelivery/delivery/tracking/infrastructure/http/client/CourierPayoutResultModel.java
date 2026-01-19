package dev.pedroscheurer.nambadelivery.delivery.tracking.infrastructure.http.client;

import java.math.BigDecimal;

public record CourierPayoutResultModel(
        BigDecimal payoutFee
) {
}
