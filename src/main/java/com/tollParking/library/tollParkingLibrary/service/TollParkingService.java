package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.configuration.TollParkingLibraryApiConfig;
import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.model.SlotType;
import com.tollParking.library.tollParkingLibrary.repository.ParkingBillRepository;
import com.tollParking.library.tollParkingLibrary.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class TollParkingService implements ITollParkingService {

    private ParkingSlotRepository parkingSlotRepository;
    private ParkingBillRepository parkingBillRepository;
    private IBillService billService;
    private ITollLibraryHelperService tollLibraryHelperService;

    private boolean isTollParkingLibraryInitialized;

    public TollParkingService(
            IBillService billService,
            ParkingSlotRepository parkingSlotRepository,
            ParkingBillRepository parkingBillRepository,
            ITollLibraryHelperService tollLibraryHelperService) {
        this.billService = billService;
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingBillRepository = parkingBillRepository;
        this.tollLibraryHelperService = tollLibraryHelperService;
    }

    @Override
    public TollParkingLibraryApiConfig initializeTollParkingLibraryConfiguration(TollParkingLibraryApiConfig tollParkingLibraryApiConfig) {
        if(!isTollParkingLibraryInitialized) {
            int numberOfStandardParkingSlot = tollParkingLibraryApiConfig.getNumberOfStandardParkingSlot();
            int numberOfElectricCar20KWParkingSlot = tollParkingLibraryApiConfig.getNumberOfElectricCar20KWParkingSlot();
            int numberOfElectricCar50KWParkingSlot = tollParkingLibraryApiConfig.getNumberOfElectricCar50KWParkingSlot();
            /* Create new Parking slots foreach slotType
             * (depending of the number of parking slot for each type from the givin configuration

             */
            IntStream.rangeClosed(1, numberOfStandardParkingSlot)
                    .forEach(
                            i -> parkingSlotRepository.save(new ParkingSlot(SlotType.STANDARD, true)));
            IntStream.rangeClosed(1, numberOfElectricCar20KWParkingSlot)
                    .forEach(
                            i ->
                                    parkingSlotRepository.save(
                                            new ParkingSlot(SlotType.ELECTRIC_CAR_20KW, true)));
            IntStream.rangeClosed(1, numberOfElectricCar50KWParkingSlot)
                    .forEach(
                            i ->
                                    parkingSlotRepository.save(
                                            new ParkingSlot(SlotType.ELECTRIC_CAR_50KW, true)));
            billService.changePricingPolicy(tollParkingLibraryApiConfig.getParkingSlotPricingPolicy());
            this.isTollParkingLibraryInitialized=true;
            return tollParkingLibraryApiConfig;

        }
        return null;
    }

    @Override
    public boolean changePricingPolicy(PricingPolicy pricingPolicy) {
        return billService.changePricingPolicy(pricingPolicy);
    }

    @Override
    /**
     * This method need to be thread safe in order to avoid race conditions,
     * since this is an instance method only one thread per instance of the this class can have access to it
     * In real life, multiple customer could call our API to get the first available parking slot
     */
    public synchronized Optional<ParkingSlot> getFirstAvailableParkingSlot(String plateNumber) {
        //Get the customer car related parking slot type (This operation is random)
        SlotType customerCarParkingSlotType = tollLibraryHelperService.generateRandomSlotType(plateNumber);
        Optional<ParkingSlot> firstAvailableParkingSlot = parkingSlotRepository.findAll().stream()
                .filter(parkingSlot -> customerCarParkingSlotType == parkingSlot.getSlotType()
                        && parkingSlot.isParkingSlotFree())
                .findFirst();
        // If a free parking slot is available, we update as not free anymore and bill the customer
        if(firstAvailableParkingSlot.isPresent()) {
            ParkingSlot parkingSlot = firstAvailableParkingSlot.get();
            parkingSlot.setParkingSlotFree(false);
            // Save the retrieved parkingSlot in database with the update status (not available)
            parkingSlotRepository.save(parkingSlot);
            // Create a new parking bill for the customer who reach the parking
            parkingBillRepository.save(new ParkingBill(plateNumber,parkingSlot, LocalDateTime.now()));
        }
        return firstAvailableParkingSlot;
    }

    /**
     * This method need to be thread safe in order to avoid race conditions,
     * since this is an instance method only one thread per instance of the this class can have access to it
     * In real life, multiple customer could call our API to get the first available parking slot
     */
    @Override
    public synchronized Optional<ParkingBill> leaveParking(String customerCarNumberPlate) {
        // A customer who do not leave the parking yet shouldn't have a parkingExitDate
        Optional<ParkingBill> customerParkingBill = parkingBillRepository.findAll().stream()
                .filter(parkingBill -> customerCarNumberPlate.equals(parkingBill.getCustomerCarPlateNumber())
                        && Objects.isNull(parkingBill.getParkingExitDate()))
                .findFirst();
        if(customerParkingBill.isPresent()) {
            ParkingBill parkingBill = customerParkingBill.get();
            parkingBill.setParkingExitDate(LocalDateTime.now());
            // Retrieve the customer parking slot and update it as free ( since the customer is leaving the parking)
            ParkingSlot customerRelatedParkingSlot= parkingSlotRepository.findById(parkingBill.getParkingSlot().getId())
                                                                                  .get();
            customerRelatedParkingSlot.setParkingSlotFree(true);
            parkingSlotRepository.save(customerRelatedParkingSlot);
            // Calculate the parking bill price and save the parking bill in database
            billService.billCustomer(parkingBill);
        }
        return customerParkingBill;
    }

}
