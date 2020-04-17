package com.tollParking.library.tollParkingLibrary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@ApiModel(description = "A Parking slot Entity model")
@Entity
@Table(name = "Parking_Slot")
@NoArgsConstructor
public class ParkingSlot {

    @Id
    @ApiModelProperty(
            name = "id",
            required = true,
            notes = "A parking slot auto-generated id")
    private long id;

    @ApiModelProperty(
            name = "parkingSlotType",
            required = true,
            value = "STANDARD or ELECTRIC_CAR_20KW or ELECTRIC_CAR_50KW",
            notes = "The parking slot type")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @ApiModelProperty(
            name = "isParkingSlotFree",
            required = true,
            value = "false or true",
            notes = "Indicate if the parking slot is free or not")
    private boolean isParkingSlotFree;

    public ParkingSlot(SlotType slotType, boolean isParkingSlotFree) {
        this.slotType = slotType;
        this.isParkingSlotFree = isParkingSlotFree;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    @Column(name = "is_parking_slot_free", nullable = false)
    public boolean isParkingSlotFree() {
        return this.isParkingSlotFree;
    }

    public void setParkingSlotFree(boolean isParkingSlotFree) {
        this.isParkingSlotFree= isParkingSlotFree;
    }

}
