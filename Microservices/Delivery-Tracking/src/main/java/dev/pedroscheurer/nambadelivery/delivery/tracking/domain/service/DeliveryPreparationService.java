package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.service;

import dev.pedroscheurer.nambadelivery.delivery.tracking.api.model.dtos.ContactPointInput;
import dev.pedroscheurer.nambadelivery.delivery.tracking.api.model.dtos.DeliveryInput;
import dev.pedroscheurer.nambadelivery.delivery.tracking.api.model.dtos.ItemInput;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.exceptions.DomainException;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.ContactPoint;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.Delivery;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTimeEstimationService deliveryTimeEstimationService;

    private final CourierPayoutCalculationService courierPayoutCalculationService;

    @Transactional
    public Delivery draft(DeliveryInput deliveryInput) {
        Delivery delivery = Delivery.draft();
        handlePreparation(deliveryInput, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput deliveryInput) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new DomainException("Delivery not found"));
        delivery.removeItems();
        handlePreparation(deliveryInput, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput deliveryInput, Delivery delivery) {
        ContactPointInput senderInput = deliveryInput.sender();
        ContactPointInput recipientInput = deliveryInput.recipient();

        ContactPoint sender = ContactPoint.builder()
                .street(senderInput.street())
                .zipcode(senderInput.zipCode())
                .number(senderInput.number())
                .complement(senderInput.complement())
                .name(senderInput.name())
                .phone(senderInput.phone())
                .build();
        ContactPoint recipient = ContactPoint.builder()
                .street(recipientInput.street())
                .zipcode(recipientInput.zipCode())
                .number(recipientInput.number())
                .complement(recipientInput.complement())
                .name(recipientInput.name())
                .phone(recipientInput.phone())
                .build();

        DeliveryEstimate estimate = deliveryTimeEstimationService.estimate(sender, recipient);

        BigDecimal calculatedPayout =
                courierPayoutCalculationService.calculatePayout(estimate.getDistanceInKm());

        BigDecimal distanceFee = calculateFee(estimate.getDistanceInKm());

        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipient)
                .sender(sender)
                .expectedDeliveryTime(estimate.getEstimatedTime())
                .courierPayout(calculatedPayout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for(ItemInput itemInput : deliveryInput.items()){
            delivery.addItem(itemInput.name(), itemInput.quantity());
        }
    }

    private BigDecimal calculateFee(Double distanceInKm) {
        return new BigDecimal("3")
                .multiply(new BigDecimal(distanceInKm))
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
