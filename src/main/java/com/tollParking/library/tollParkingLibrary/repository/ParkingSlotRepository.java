package com.tollParking.library.tollParkingLibrary.repository;

import com.tollParking.library.tollParkingLibrary.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

}
