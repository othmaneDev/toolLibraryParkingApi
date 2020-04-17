package com.tollParking.library.tollParkingLibrary.controller;

import com.tollParking.library.tollParkingLibrary.configuration.TollParkingLibraryApiConfig;
import com.tollParking.library.tollParkingLibrary.exception.InvalidPricingPolicy;
import com.tollParking.library.tollParkingLibrary.exception.NoAvailableParkingSlot;
import com.tollParking.library.tollParkingLibrary.exception.NoParkingBill;
import com.tollParking.library.tollParkingLibrary.exception.tollParkingLibraryInitializationFailed;
import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import com.tollParking.library.tollParkingLibrary.model.PricingPolicy;
import com.tollParking.library.tollParkingLibrary.service.ITollParkingService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(
        value = "TollParkingController",
        tags = {"TollParking Controller"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "TollParking Controller",
                        description = "The toll parking main controller (Unique entry point)")
        })
@RestController("/tollParkingApi")
@AllArgsConstructor
public class TollParkingController {
    private ITollParkingService tollParkingService;

    @ApiOperation(value = "Action that initialize the toll parking library with configuration",
            response = TollParkingLibraryApiConfig.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Toll parking library has been successfully initialized"),
                    @ApiResponse(code = 403, message = "Toll parking library couldn't be initialized since it has been already initialize")
            })
    @PostMapping("/initializeLibraryWithConfig")
    public ResponseEntity<TollParkingLibraryApiConfig> initializeTollParking(
            @RequestBody TollParkingLibraryApiConfig tollParkingLibraryConfig) throws tollParkingLibraryInitializationFailed {
        TollParkingLibraryApiConfig tollParkingLibraryApiConfig =
                Optional.ofNullable(tollParkingService.initializeTollParkingLibraryConfiguration(tollParkingLibraryConfig))
                        .orElseThrow(
                                () -> new tollParkingLibraryInitializationFailed("Toll parking library couldn't be initialized since it has been already initialize"));
        return ResponseEntity.ok(tollParkingLibraryApiConfig);
    }

    @ApiOperation(
            value = "Action for changing(update) pricing policy",
            response = PricingPolicy.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Pricing policy changed"),
                    @ApiResponse(code = 403, message = "Forbidden: Invalid pricing policy")
            })
    @PutMapping("/changingPricingPolicy")
    public ResponseEntity<PricingPolicy> changingPricingPolicy(@RequestBody PricingPolicy pricingPolicy)
            throws InvalidPricingPolicy {
        if (tollParkingService.changePricingPolicy(pricingPolicy)) {
            return ResponseEntity.ok(pricingPolicy);
        } else {
            throw new InvalidPricingPolicy("The given pricing policy is not known," +
                    "so the update has been failed");
        }
    }

    @ApiOperation(value = "Get first available parking slot for the given customer car that enter the parking",
            response = ParkingSlot.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: An available free parking slot has been retrieved"),
                    @ApiResponse(code = 404, message = "Not found: Sorry, but there is no parking slot available for now")
            })
    @GetMapping("/enterParking/{customerCarPlateNumber}")
    public ResponseEntity<ParkingSlot> enterParking(@PathVariable("customerCarPlateNumber") String customerCarPlateNumber)
            throws NoAvailableParkingSlot {
        ParkingSlot parkingSlot =
                tollParkingService
                        .getFirstAvailableParkingSlot(customerCarPlateNumber)
                        .orElseThrow(
                                () ->
                                        new NoAvailableParkingSlot(
                                                "Sorry, but there is no parking slot available for the given car" +
                                                        " having this plate number " + ": " + customerCarPlateNumber));

        return ResponseEntity.ok(parkingSlot);
    }

    @ApiOperation(value = "Action when the customer leave the parking, so a parking bill need to be returned to the customer",
            response = ParkingBill.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Parking bill retrieved"),
                    @ApiResponse(code = 404, message = "Not found: Parking bill not found")
            })
    @GetMapping("/leaveParking/{customerCarPlateNumber}")
    public ResponseEntity<ParkingBill> leaveParking(@PathVariable("customerCarPlateNumber") String customerCarPlateNumber)
            throws NoParkingBill {
        ParkingBill parkingBill =
                tollParkingService
                        .leaveParking(customerCarPlateNumber)
                        .orElseThrow(
                                () ->
                                        new NoParkingBill(
                                                "The parking exit bill could not be edited for this number plate :"
                                                        + customerCarPlateNumber));

        return ResponseEntity.ok(parkingBill);
    }

}
