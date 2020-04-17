package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.SlotType;


public interface ITollLibraryHelperService {

    /**
     * The aim of this method is to calculate a random slot type based of the customer number of plate
     * @param plateNumber the customer number of plate as {@link String}
     * @return a random {@link SlotType}
     */
    SlotType generateRandomSlotType(String plateNumber);
}
