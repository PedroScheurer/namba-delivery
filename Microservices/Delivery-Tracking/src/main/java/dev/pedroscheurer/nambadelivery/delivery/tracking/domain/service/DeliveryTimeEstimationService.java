package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.service;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}
