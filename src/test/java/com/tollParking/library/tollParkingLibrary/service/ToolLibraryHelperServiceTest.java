package com.tollParking.library.tollParkingLibrary.service;
import java.util.Arrays;

import com.tollParking.library.tollParkingLibrary.model.SlotType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToolLibraryHelperServiceTest {
    private static final String CUSTOMER_CAR_PLATE_NUMBER = "EP FRT RT";
    private ITollLibraryHelperService tollLibraryHelperService = new ToolLibraryHelperService();

    @Test
    public void testRetrieveParkingSlotType() {

        // execute
        SlotType randomSLotType = tollLibraryHelperService.generateRandomSlotType(CUSTOMER_CAR_PLATE_NUMBER);

        // assert
        Assert.assertTrue(
                "Ensure that the ramdom generated slot type exist within the SlotType Enum values",
                Arrays.asList(SlotType.values()).contains(randomSLotType));
    }
}
