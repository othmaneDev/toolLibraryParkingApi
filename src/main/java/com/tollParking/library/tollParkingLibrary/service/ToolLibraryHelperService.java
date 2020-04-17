package com.tollParking.library.tollParkingLibrary.service;

import com.tollParking.library.tollParkingLibrary.model.SlotType;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ToolLibraryHelperService implements ITollLibraryHelperService{

   @Override
    public SlotType generateRandomSlotType(String plateNumber) {
        Random random = new Random();
        SlotType[] slotTypes = SlotType.values();
        // Pick and return a random value of the slotType enum
        return SlotType.values()[random.nextInt(slotTypes.length)];
    }

}
