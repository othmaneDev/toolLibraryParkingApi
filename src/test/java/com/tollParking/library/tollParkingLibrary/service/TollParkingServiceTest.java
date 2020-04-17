package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.configuration.TollParkingLibraryApiConfig;
import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.model.SlotType;
import com.tollParking.library.tollParkingLibrary.repository.ParkingBillRepository;
import com.tollParking.library.tollParkingLibrary.repository.ParkingSlotRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TollParkingServiceTest {

    private  static final String CUSTOMER_CAR_NUMBER_PLATE = "EP 456 RC";

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private ParkingBillRepository parkingBillRepository;

    @Mock
    private ITollLibraryHelperService tollLibraryHelperService;

    private ITollParkingService tollParkingService;


    private IBillService billService;

    private PricingPolicy pricingPolicy;

    private  Optional<ParkingSlot> firstAvailableParkingSlot;

    private TollParkingLibraryApiConfig initialTollLibraryApiConfig;

    private TollParkingLibraryApiConfig newTollParkingLibraryApiConfig;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(tollLibraryHelperService.generateRandomSlotType(Mockito.anyString()))
                .thenReturn(SlotType.ELECTRIC_CAR_20KW);
        ParkingSlot parkingSlot = new ParkingSlot(SlotType.ELECTRIC_CAR_20KW, true);
        parkingSlot.setId(0L);
        Mockito.when(parkingSlotRepository.findAll())
                .thenReturn(Arrays.asList(new ParkingSlot(SlotType.ELECTRIC_CAR_20KW, true)));
        Mockito.when(parkingSlotRepository.findById(0L)).thenReturn(Optional.of(parkingSlot));
        Mockito.when(parkingBillRepository.findAll())
                .thenReturn(Arrays.asList(new ParkingBill(CUSTOMER_CAR_NUMBER_PLATE, parkingSlot, LocalDateTime.now())));
        billService = new BillService(parkingBillRepository);
        tollParkingService = new TollParkingService (billService, parkingSlotRepository, parkingBillRepository, tollLibraryHelperService);
    }

    @Test
    public void testInitializeTollParkingLibraryConfiguration() {
        this.InitializeTollLibrary();
        firstAvailableParkingSlot = tollParkingService.getFirstAvailableParkingSlot(CUSTOMER_CAR_NUMBER_PLATE);
        // assert to check expected results for returned parking slot and configuration
        Assert.assertNotNull("The new configuration is not null", newTollParkingLibraryApiConfig);
        Assert.assertEquals(
                "the new and the initial toll library configuration must be the same", initialTollLibraryApiConfig, newTollParkingLibraryApiConfig);
        Assert.assertNotNull(
                "According to the configuration, a parking slot should be returned (Since not is occupied)",
                firstAvailableParkingSlot.get());
        Assert.assertFalse(
                "The parking slot is not free anymore", firstAvailableParkingSlot.get().isParkingSlotFree());
    }

    @Test
    public void testLeaveParking (){
        InitializeTollLibrary();
        firstAvailableParkingSlot = tollParkingService.getFirstAvailableParkingSlot(CUSTOMER_CAR_NUMBER_PLATE);
        Optional<ParkingBill> editedParkingBill = tollParkingService.leaveParking(CUSTOMER_CAR_NUMBER_PLATE);
        Assert.assertNotNull("A parking bill should be returned", editedParkingBill.get());
        Assert.assertEquals(
                "The parking bill is edited for the correct customer car",
                editedParkingBill.get().getCustomerCarPlateNumber(), CUSTOMER_CAR_NUMBER_PLATE);
        Assert.assertEquals(
                "The parking bill is edited for the correct parking slot",
                firstAvailableParkingSlot.get().getId(),
                editedParkingBill.get().getParkingSlot().getId());
    }


    @Test
    public void testChangePricingPolicy_ValidPricingPolicyUseCase() {

        // prepare data test input with the valid pricingPolicy
        pricingPolicy = new PricingPolicy(2, 1.5);

        // execute the changePricingPolicy with the valid pricingPolicy
        boolean isPricingPolicyChanged = tollParkingService.changePricingPolicy(pricingPolicy);

        // assert
        Assert.assertTrue("Pricing policy has been correctly changed", isPricingPolicyChanged);
    }

    @Test
    public void testChangePricingPolicy_InvalidPricingPolicyUseCase() {
        // prepare a dummy invalid pricing policy as data input
        PricingPolicy invalidPricingPolicy = new PricingPolicy(1, 0.0);

        // execute the changePricingPolicy with an invalid pricingPolicy
        boolean isPricingPolicyChanged = tollParkingService.changePricingPolicy(invalidPricingPolicy);

        // assert
        Assert.assertFalse("Pricing policy could't be changed", isPricingPolicyChanged);
    }

    private void InitializeTollLibrary() {
        // prepare data
        pricingPolicy = new PricingPolicy(2, 1.5);
        initialTollLibraryApiConfig = new TollParkingLibraryApiConfig(
                3, 2, 1, pricingPolicy);
        // execute the method initializeTollParkingLibraryConfiguration with the prepared data
        newTollParkingLibraryApiConfig = tollParkingService.initializeTollParkingLibraryConfiguration(initialTollLibraryApiConfig);
    }

}
