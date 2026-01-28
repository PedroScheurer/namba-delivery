package dev.pedroscheurer.nambadelivery.delivery.tracking.infrastructure.http.client;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceHttpImpl
        implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        try {
            CourierPayoutResultModel courierPayoutResultModel =
                    courierAPIClient.payoutCalculation(new CourierPayoutCalculationInput(distanceInKm));
            return courierPayoutResultModel.payoutFee();
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException();
        } catch (HttpServerErrorException | IllegalArgumentException | CallNotPermittedException e) {
            throw new BadGatewayException();
        }

    }
}
