package dev.pedroscheurer.nambadelivery.courier.management.domain.service;

import dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos.CourierInput;
import dev.pedroscheurer.nambadelivery.courier.management.domain.model.Courier;
import dev.pedroscheurer.nambadelivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {

    private final CourierRepository courierRepository;

    public Courier create(@Valid CourierInput input) {
        Courier courier = Courier.brandNew(input.name(), input.phone());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, @Valid CourierInput input) {
        Courier courier = courierRepository.findById(courierId).orElseThrow();

        courier.setPhone(input.phone());
        courier.setName(input.name());

        return courierRepository.saveAndFlush(courier);
    }
}
