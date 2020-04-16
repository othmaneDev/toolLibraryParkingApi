package com.tollParking.library.tollParkingLibrary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "Class representing a parking slot pricing policy")
@Data
@AllArgsConstructor
public class PricingPolicy {

    @ApiModelProperty(
            name = "fixedAmountPerHourPricePolicy",
            required = true,
            value = "8.00",
            notes = "According to this policy :" +
                    "Customer are billed a fixed amount + " +
                    "each hour spent in the parking (fixed amount + nb hours * hour price)")
    private double fixedAmountPerHourPricePolicy;

    @ApiModelProperty(
            name = "pricePerNumberOfHoursPolicy",
            required = true,
            value = "2",
            notes = "According to this policy :" +
                    "Customer are billed  for each hour spent in the parking (nb hours * hour price)")
    private double pricePerNumberOfHoursPolicy;

}
