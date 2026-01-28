package dev.pedroscheurer.nambadelivery.courier.management.api.controller;

import dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos.CourierInput;
import dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos.CourierPayoutCalculationInput;
import dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos.CourierPayoutResultModel;
import dev.pedroscheurer.nambadelivery.courier.management.domain.model.Courier;
import dev.pedroscheurer.nambadelivery.courier.management.domain.repository.CourierRepository;
import dev.pedroscheurer.nambadelivery.courier.management.domain.service.CourierPayoutService;
import dev.pedroscheurer.nambadelivery.courier.management.domain.service.CourierRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
@Slf4j
public class CourierController {

    private final CourierRegistrationService courierRegistrationService;
    private final CourierPayoutService courierPayoutService;
    private final CourierRepository courierRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Courier create(@Valid @RequestBody CourierInput input) {
        return courierRegistrationService.create(input);
    }

    @PutMapping("/{courierId}")
    public Courier create(@PathVariable UUID courierId,
                          @Valid @RequestBody CourierInput input) {
        return courierRegistrationService.update(courierId, input);
    }

    @GetMapping
    public PagedModel<Courier> findAll(@PageableDefault Pageable pageable) {
        log.info("FindAll Requested");
        return new PagedModel<>(courierRepository.findAll(pageable));
    }

    @GetMapping("/{courierId}")
    public Courier findById(@PathVariable UUID courierId){
        return courierRepository.findById(courierId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/payout-calculation")
    public CourierPayoutResultModel calculate(@RequestBody CourierPayoutCalculationInput input) throws InterruptedException {
        BigDecimal payoutFee = courierPayoutService.calculate(input.distanceInKm());

        return new CourierPayoutResultModel(payoutFee);
    }
}
