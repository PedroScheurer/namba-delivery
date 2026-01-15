package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {

    @Test
    void draft_canChangeToWaitingForCourier(){
        assertTrue(
                DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.WAITING_FOR_COURIER)
        );
    }
    @Test
    void draft_canChangeToInTransit(){
        assertTrue(
                DeliveryStatus.DRAFT.canNotChangeTo(DeliveryStatus.IN_TRANSIT)
        );
    }
}