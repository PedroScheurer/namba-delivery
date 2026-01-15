package dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model;

import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.enums.DeliveryStatus;
import dev.pedroscheurer.nambadelivery.delivery.tracking.domain.model.exceptions.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private ContactPoint recipient ;

    private List<Item> items = new ArrayList<>();

    public static Delivery draft(){
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, int quantity){
        Item item = Item.brandNew(name, quantity);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId){
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void changeItemQuantity(UUID itemId, int quantity){
        Item item = items.stream().filter(i -> i.getId().equals(itemId))
                .findFirst().orElseThrow();

        item.setQuantity(quantity);
        calculateTotalItems();
    }

    public void removeItems(){
        items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails details){
        verifyIfCanBeEdited();
        setSender(details.getSender());
        setRecipient(details.getRecipient());
        setDistanceFee(details.getDistanceFee());
        setCourierPayout(details.getCourierPayout());

        setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place(){
        verifyIfCanBePlaced();
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId){
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered(){
        this.changeStatusTo(DeliveryStatus.DELIVERY);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems(){
        int sum = items.stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(sum); ;
    }

    private void verifyIfCanBePlaced() {
        if(!isFilled()){
            throw new DomainException();
        }
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException("Delivery c an't be placed");
        }
    }

    private void verifyIfCanBeEdited(){
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException("Delivery can't be edited");
        }
    }

    private boolean isFilled() {
        return this.getSender() != null
                && this.getRecipient() != null
                && this.getTotalCost() != null;
    }

    private void changeStatusTo(DeliveryStatus newStatus){
        if(newStatus != null && this.getStatus().canNotChangeTo(newStatus)){
            throw new DomainException("Invalid status transition from: " + this.getStatus() +
                    " to " + newStatus);
        }
        this.setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails{
        private ContactPoint sender;
        private ContactPoint recipient;

        private BigDecimal distanceFee;
        private BigDecimal courierPayout;

        private Duration expectedDeliveryTime;
    }
}
