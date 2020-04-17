package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.repository.ParkingBillRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceBillTest {

    private static final LocalDateTime PARKING_ENTRY_DATE = LocalDateTime.of(2020, 2, 2, 11, 0);
    private static final LocalDateTime PARKING_EXIT_DATE = LocalDateTime.of(2020, 2, 2, 13, 45);
    private static final String CUSTOMER_PLATE_NUMBER = "EP 756 RC";

    @Mock
    private ParkingBillRepository parkingBillRepository;

    private IBillService billService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(parkingBillRepository.save(any()))
                .thenAnswer(
                        (Answer<ParkingBill>) invocation -> {
                            Object[] args = invocation.getArguments();
                            return (ParkingBill) args[0];
                        });

        billService = new BillService(parkingBillRepository);
    }

    @Test
    public void testBillCustomer_ValidPricingPolicyUseCase() {
        // Data initialization
        PricingPolicy pricingPolicy = new PricingPolicy(1, 2);
        ParkingSlot parkingSlot = new ParkingSlot();
        ParkingBill parkingBill =
                new ParkingBill(CUSTOMER_PLATE_NUMBER, parkingSlot, PARKING_ENTRY_DATE);
        parkingBill.setParkingExitDate(PARKING_EXIT_DATE);

        // execute
        boolean isPricingPolicyChanged= billService.changePricingPolicy(pricingPolicy);
        parkingBill = billService.billCustomer(parkingBill);

        // asserts
        Assert.assertTrue("Pricing policy has been changed successfully", isPricingPolicyChanged);
        Assert.assertNotNull("Parking bill is not null", parkingBill);
        Assert.assertEquals(5.0, parkingBill.getPricePerHour(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testBillCustomer_InvalidPricingPolicyUseCase() {
        // Data initialization
        PricingPolicy pricingPolicy = new PricingPolicy(0.0, 0.0);
        ParkingSlot parkingSlot = new ParkingSlot();
        ParkingBill parkingBill =
                new ParkingBill(CUSTOMER_PLATE_NUMBER, parkingSlot, PARKING_ENTRY_DATE);
        parkingBill.setParkingExitDate(PARKING_EXIT_DATE);

        // execute changePricingPolicy from bill service
        boolean isPricingPolicyChanged= billService.changePricingPolicy(pricingPolicy);
        parkingBill = billService.billCustomer(parkingBill);

        // assert
        Assert.assertFalse("Pricing policy has been updated", isPricingPolicyChanged);

        // execute bill customer method, must throw the exception
        billService.billCustomer(parkingBill);
    }

}
