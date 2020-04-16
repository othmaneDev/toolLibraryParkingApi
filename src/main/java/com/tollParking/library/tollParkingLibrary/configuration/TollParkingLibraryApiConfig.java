package com.tollParking.library.tollParkingLibrary.configuration;

import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "The toll parking library API initial configuration")
@Data
@AllArgsConstructor
public class TollParkingLibraryApiConfig {

        @ApiModelProperty(
                name = "numberOfStandardParkingSlot",
                required = true,
                value = "50",
                notes = "The number of standard parking slot")
        private int numberOfStandardParkingSlot;

        @ApiModelProperty(
                name = "numberOfElectricCar20KWParkingSlot",
                required = true,
                value = "30",
                notes = "The number of electric car parking slot with 20kW supply power")
        private int numberOfElectricCar20KWParkingSlot;

        @ApiModelProperty(
                name = "numberOfElectricCar50KWParkingSlot",
                required = true,
                value = "20",
                notes = "number of electric car parking slot with 50kW supply power")
        private int numberOfElectricCar50KWParkingSlot;

        @ApiModelProperty(
                name = "ParkingSlotPricingPolicy",
                required = true,
                notes = "The Parking slot pricing policy")
        private PricingPolicy ParkingSlotPricingPolicy;
}
