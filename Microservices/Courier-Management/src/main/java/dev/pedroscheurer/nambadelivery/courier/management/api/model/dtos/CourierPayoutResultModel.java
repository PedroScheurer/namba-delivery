package dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos;

import java.math.BigDecimal;

public record CourierPayoutResultModel(
        BigDecimal payoutFee
) {
}
