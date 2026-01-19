package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.repository;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
