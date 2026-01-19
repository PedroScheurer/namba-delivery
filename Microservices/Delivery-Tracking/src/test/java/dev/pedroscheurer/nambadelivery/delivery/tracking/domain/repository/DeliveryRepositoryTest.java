package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.repository;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.ContactPoint;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist(){
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.addItem("Computador",2);
        delivery.addItem("Celular",2);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2,persistedDelivery.getItems().size());
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