package dev.pedroscheurer.nambadelivery.courier.management.domain.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Courier {

    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fulfilledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;

    private OffsetDateTime lastFulfilledDeliveryAt;

    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries(){
        return Collections.unmodifiableList(this.pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone){
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setPendingDeliveriesQuantity(0);
        courier.setFulfilledDeliveriesQuantity(0);
        return courier;
    }

    public void assign(UUID deliveryId){
        this.pendingDeliveries.add(
                AssignedDelivery.pending(deliveryId)
        );
        this.setPendingDeliveriesQuantity(getPendingDeliveriesQuantity() + 1);
    }

    public void fulfill(UUID deliveryId){
        AssignedDelivery delivery = this.pendingDeliveries.stream().filter(
                d -> d.getId().equals(deliveryId)
        ).findFirst().orElseThrow();
        this.pendingDeliveries.remove(delivery);

        this.setPendingDeliveriesQuantity(getPendingDeliveriesQuantity() - 1);
        this.setFulfilledDeliveriesQuantity(getFulfilledDeliveriesQuantity() + 1);
        this.setLastFulfilledDeliveryAt(OffsetDateTime.now());
    }
}
