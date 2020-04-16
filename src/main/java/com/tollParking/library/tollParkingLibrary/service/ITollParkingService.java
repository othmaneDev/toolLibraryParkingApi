package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.configuration.TollParkingLibraryApiConfig;
import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;

import java.util.Optional;

public interface ITollParkingService {

    /**
     * Initialize the toll parking library  with an initial configuration.
     * The given to {@link TollParkingLibraryApiConfig} will provide the library with the following data :
     *  - For each parking slot type : the total capacity (number of slots)
     *  - a default price policy as {@link PricingPolicy}
     *
     * @param tollParkingLibraryApiConfig a {@link TollParkingLibraryApiConfig}
     * @return used configuration
     */
     TollParkingLibraryApiConfig initializeTollParkingLibraryConfiguration(TollParkingLibraryApiConfig tollParkingLibraryApiConfig);

    /**
     * Change the price policy : This pricing policy has been defined first time when the the library get initialized
     *
     * @param pricingPolicy a{@link PricingPolicy}
     * @return true if the pricing policy has been updated successfully, false if the given pricing policy is not valid
     */
    boolean changePricingPolicy(PricingPolicy pricingPolicy);


    /**
     * Get the first available parking slot from the toll parking.
     *
     * @param plateNumber the custom car number plate as {@link String}
     * @return the custom car number plate as {@link Optional<ParkingSlot>} to avoid null check
     */
    Optional<ParkingSlot> getFirstAvailableParkingSlot(String plateNumber);

    /**
     * The parking slot related to the car number plate will be free again,
     * and then the parking bill need to be returned to the customer
     *
     * @param customerCarNumberPlate  the customer car number plate as {@link String}
     * @return parking bill is returned if exists as @{@link Optional<ParkingBill>} to avoid null check
     */
    Optional<ParkingBill> leaveParking(String customerCarNumberPlate);
}
