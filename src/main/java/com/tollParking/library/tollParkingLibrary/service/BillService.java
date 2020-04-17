package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.repository.ParkingBillRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class BillService implements IBillService {

    //Inject the global pricing policy which is define when the TollParkingLibraryApiConfig is loaded (Spring boot server start)
    private PricingPolicy ParkingSlotPricingPolicy;
    private ParkingBillRepository parkingBillRepository;

    /**
     * This constructor must be provided since the BillService only need the parkingBillRepository at dependency injection
     * The PricingPolicy is provided through configuration when application is loaded
     *
     * @param parkingBillRepository a {@link ParkingBillRepository}
     */
    public BillService(ParkingBillRepository parkingBillRepository) {
        this.parkingBillRepository = parkingBillRepository;
    }

    @Override
    public boolean changePricingPolicy(PricingPolicy pricingPolicy) {
        boolean isPricingPolicyChanged = false;
        if (Objects.nonNull(pricingPolicy) &&
                (pricingPolicy.getFixedAmountPrice() >= 0
                        && pricingPolicy.getPricePerNumberOfHours() > 0)) {
            this.ParkingSlotPricingPolicy = pricingPolicy;
            isPricingPolicyChanged = true;
        }
        return isPricingPolicyChanged;
    }

    @Override
    public ParkingBill billCustomer(ParkingBill parkingBill) {
        double customerPrice =
                ParkingSlotPricingPolicy.getFixedAmountPrice()
                        + ParkingSlotPricingPolicy.getPricePerNumberOfHours() * parkingHoursPrice(parkingBill);
        parkingBill.setPricePerHour(customerPrice);
        return parkingBillRepository.save(parkingBill);
    }

    private long parkingHoursPrice(ParkingBill parkingBill) {
        // Calculate the number of hours the customer have spent in the parking
        return parkingBill.getParkingEntryDate().until(parkingBill.getParkingExitDate(),
                ChronoUnit.MINUTES) / 60;
    }
}
