package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.SlotType;

public interface IParkingSlotService {

    /**
     * Search the parking slot type based on the customer car number plate
     *
     * @param carNumberPlate the customer car number plate as {@link String}
     * @return return the customer car corresponding {@link SlotType}
     */
    SlotType retrieveParkingSlotType(String carNumberPlate);
}
