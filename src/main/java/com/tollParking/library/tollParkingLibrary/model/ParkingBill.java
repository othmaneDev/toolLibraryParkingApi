package com.tollParking.library.tollParkingLibrary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;

@ApiModel(description = "Class representing a Parking bill")
@Entity
@Table(name = "Parking_Bill")
@NoArgsConstructor
public class ParkingBill {

    @ApiModelProperty(
            name = "id",
            required = true,
            value = "123",
            notes = "A parking bill auto-generated id")
    private long id;

    @ApiModelProperty(
            name = "customerCarPlateNumber",
            required = true,
            value = "EP 654 RE",
            notes = "customer vehicle number plate")
    private String customerCarPlateNumber;

    @ApiModelProperty(
            name = "parkingSlot",
            required = true,
            notes = "parking slot related to the customer bill")
    private ParkingSlot parkingSlot;

    @ApiModelProperty(
            name = "hourPrice",
            required = true,
            value = "8",
            notes = "Parking price per hour")
    private double pricePerHour;

    @ApiModelProperty(
            name = "parkingEntryDate",
            required = true,
            value = "2020-04-11T20:30:20.110",
            notes = "The date when the customer enter the parking")
    private LocalDateTime parkingEntryDate;

    @ApiModelProperty(
            name = "parkingExitDate",
            value = "2020-04-11T22:30:20.110",
            notes = "The date when the customer exist the parking")
    //This field should be nullable in order to check whether or not the customer has left the parking
    private LocalDateTime parkingExitDate;

    public ParkingBill(String numberplate, ParkingSlot parkingSlot, LocalDateTime parkingEntryDate) {
        this.customerCarPlateNumber = numberplate;
        this.parkingSlot = parkingSlot;
        this.parkingEntryDate = parkingEntryDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "PARKINGSLOT_ID")
    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    @Column(name = "car_plate_number", nullable = false)
    public String getCustomerCarPlateNumber() {
        return customerCarPlateNumber;
    }

    public void setCustomerCarPlateNumber(String carPlateNumber) {
        this.customerCarPlateNumber = carPlateNumber;
    }

    @Column(name = "price_per_hour", nullable = false)
    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    @Column(name = "parking_entry_date_time", nullable = false)
    public LocalDateTime getParkingEntryDate() {
        return parkingEntryDate;
    }
    public void setParkingEntryDate(LocalDateTime parkingEntryDate) {
        this.parkingEntryDate= parkingEntryDate;
    }

    @Column(name = "parking_exit_date_time")
    public LocalDateTime getParkingExitDate() {
        return this.parkingExitDate;
    }

    public void setParkingExitDate(LocalDateTime parkingExitDate) {
        this.parkingExitDate= parkingExitDate;
    }

}
