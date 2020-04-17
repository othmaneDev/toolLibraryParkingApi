package com.tollParking.library.tollParkingLibrary.controller;

import com.tollParking.library.tollParkingLibrary.configuration.TollParkingLibraryApiConfig;
import com.tollParking.library.tollParkingLibrary.controller.util.JsonUtils;
import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.model.SlotType;
import com.tollParking.library.tollParkingLibrary.repository.ParkingBillRepository;
import com.tollParking.library.tollParkingLibrary.repository.ParkingSlotRepository;
import com.tollParking.library.tollParkingLibrary.service.IBillService;
import com.tollParking.library.tollParkingLibrary.service.ITollParkingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
/**
 * @SpringBootTest Annotation that can be specified on a test class that runs Spring Boot based tests.
 * Provides the following features over and above the regular Spring TestContext Framework:
 *   - Uses SpringBootContextLoader as the default ContextLoader when no specific
 *     @ContextConfiguration(loader=...) is defined
 *   - Automatically searches for a @SpringBootConfiguration when nested @Configuration is not used,
 *     and no explicit classes are specified
 *   - Allows custom Environment properties to be defined using the properties attribute.
 *   - Allows application arguments to be defined using the args attribute.
 *   - Provides support for different webEnvironment modes,
 *     including the ability to start a fully running web server listening on a defined or random port.
 *   - Registers a TestRestTemplate and/or WebTestClient bean for use in web tests
 *     that are using a fully running web server.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/**
 * @AutoConfigureMockMvc add the ability to use  Spring Mock MVC Test Framework
 * in order to test our RESTfull API
 */
@AutoConfigureMockMvc
public class TollParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITollParkingService tollParkingService;

    @MockBean
    private IBillService billService;

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @MockBean
    private ParkingBillRepository parkingBillRepository;

    // Mocked data
    private PricingPolicy initialPricingPolicy;
    private PricingPolicy updatedPricingPolicy;
    private TollParkingLibraryApiConfig tollParkingConfig;
    private String customerCarPlateNumber;
    private LocalDateTime enteringParkingStartDate;
    private LocalDateTime exitParkingEndDate;

    @Before
    public void initializeMockedData() {
        // prepare data before all tests
        this.initialPricingPolicy = new PricingPolicy(2.0, 3.5);
        this.updatedPricingPolicy = new PricingPolicy(1.5, 5.0);
        this.tollParkingConfig = new TollParkingLibraryApiConfig(
                5,
                20,
                30,
                this.initialPricingPolicy);
        this.customerCarPlateNumber = "EP-787-RT";
        this.enteringParkingStartDate = LocalDateTime.of(2019, 12, 4, 14, 0);
        this.exitParkingEndDate = LocalDateTime.of(2012, 7, 1, 13, 45);
        //Create a full parking slot (which is not free)
        ParkingSlot fullParkingSlot = new ParkingSlot(SlotType.ELECTRIC_CAR_50KW, false);
        fullParkingSlot.setId(1L);
        Optional<ParkingSlot> optionalFullParkingSlot = Optional.of(fullParkingSlot);
        ParkingBill parkingBill =
                new ParkingBill(customerCarPlateNumber, fullParkingSlot, enteringParkingStartDate);
        parkingBill.setId(1L);
        parkingBill.setParkingExitDate(exitParkingEndDate);
        parkingBill.setPricePerHour(2.5);
        Optional<ParkingBill> optionalParkingBill = Optional.of(parkingBill);
        doReturn(tollParkingConfig).when(tollParkingService).initializeTollParkingLibraryConfiguration(any());
        doReturn(true).when(tollParkingService).changePricingPolicy(any());
        doReturn(optionalFullParkingSlot).when(tollParkingService).getFirstAvailableParkingSlot(customerCarPlateNumber);
        doReturn(optionalParkingBill).when(tollParkingService).leaveParking(customerCarPlateNumber);
    }

    @Test
    public void testInitializeLibraryWithConfigEndpoint() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/initializeLibraryWithConfig")
                        .content(JsonUtils.convertObjetToJsonAsByte(tollParkingConfig))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStandardParkingSlot", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar20KWParkingSlot", is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar50KWParkingSlot", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parkingSlotPricingPolicy.fixedAmountPrice", is(2.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parkingSlotPricingPolicy.pricePerNumberOfHours", is(3.5)));
    }

    @Test
    public void testChangingPricingPolicyEndpoint() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/changingPricingPolicy")
                        .content(JsonUtils.convertObjetToJsonAsByte(updatedPricingPolicy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fixedAmountPrice", is(1.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricePerNumberOfHours", is(5.0)));
    }

    @Test
    public void testEnterParkingEndpoint() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/enterParking/EP-787-RT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parkingSlotFree", is(false)));
    }

    @Test
    public void testLeaveParkingEndpoint() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/leaveParking/EP-787-RT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerCarPlateNumber", is("EP-787-RT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricePerHour", is(2.5)));
    }

}
