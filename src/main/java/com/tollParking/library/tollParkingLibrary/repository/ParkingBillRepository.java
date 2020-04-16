package com.tollParking.library.tollParkingLibrary.repository;

import com.tollParking.library.tollParkingLibrary.model.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {

}
