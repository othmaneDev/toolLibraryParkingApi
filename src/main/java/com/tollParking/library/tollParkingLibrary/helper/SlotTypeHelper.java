package com.tollParking.library.tollParkingLibrary.helper;

import com.tollParking.library.tollParkingLibrary.model.SlotType;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
public final class SlotTypeHelper {

    /**
     * The aim of this method is to calculate a random slot type based of the customer number of plate
     * @param plateNumber the customer number of plate as {@link String}
     * @return a random {@link SlotType}
     */
    public static SlotType generateRandomSlotType(String plateNumber) {
        Random random = new Random();
        SlotType[] slotTypes = SlotType.values();
        // Pick and return a random value of the slotType enum
        return SlotType.values()[random.nextInt(slotTypes.length)];
    }
}
