package com.tollParking.library.tollParkingLibrary.model;

/**
 * This {@link Enum} define the parking slot type having according power supply
 * We assume that the STANDARD slot type have 0 as power supply
 */
public enum SlotType {

    STANDARD(0) {
        @Override
        public boolean isForElectricCar() {
            return false;
        }
    },
    ELECTRIC_CAR_20KW(20) {
        @Override
        public boolean isForElectricCar() {
            return true;
        }
    },
    ELECTRIC_CAR_50KW(50) {
        @Override
        public boolean isForElectricCar() {
            return true;
        }
    };

    // the slot type power supply
    private int powerSupply;

    /**
     *  Default method of enum class that return true if the according slot type is for eletric car
     *  this method will be override for each parking slot type
     * @return true if the slot type is for electric car, false otherwise
     */
    public boolean isForElectricCar() {
        return false;
    }

    public int getPowerSupply() {
        return this.powerSupply;
    }

    /**
     * enum constructor that define a slot type having specific power supply value
     * @param powerSupply the parking slot power supply (value only relevant for electric car parking slot)
     */
    SlotType(int powerSupply) {
        this.powerSupply = powerSupply;
    }
}
