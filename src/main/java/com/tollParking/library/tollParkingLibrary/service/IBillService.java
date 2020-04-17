package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;

public interface IBillService {

    /**
     * Change the initial pricing policy
     *
     * @param pricingPolicy a{@link PricingPolicy}
     * @return true if the pricing policy has been changes, false if the given pricing policy is not valid
     */
    boolean changePricingPolicy(PricingPolicy pricingPolicy);

    /**
     * Create a new {@link ParkingBill} based on the calculated price for the parking
     * and then return it to the customer
     *
     * @param parkingBill a {@link ParkingBill}
     * @return a new {@link ParkingBill} based on the calculated parking price
     */
    ParkingBill billCustomer(ParkingBill parkingBill);
}
