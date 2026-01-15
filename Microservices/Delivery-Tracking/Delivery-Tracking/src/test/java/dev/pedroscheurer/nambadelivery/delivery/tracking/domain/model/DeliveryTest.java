package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.enums.DeliveryStatus;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {
    @Test
    public void shouldChangeToPlaced() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shouldNotPlace() {
        Delivery delivery = Delivery.draft();

        assertThrows(DomainException.class, delivery::place);

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipcode("00000-000")
                .street("Rua Barão do Rio Branco")
                .number("239")
                .complement("sala 302")
                .name("Pedro Silva")
                .phone("(54) 99123-1241")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipcode("12345-000")
                .street("Rua Padres Capuchinhos")
                .number("939")
                .complement("")
                .name("Pietra Santos")
                .phone("(54) 99653-7435")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }
}