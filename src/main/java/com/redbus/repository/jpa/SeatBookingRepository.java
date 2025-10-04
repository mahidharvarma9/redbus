package com.redbus.repository.jpa;

import com.redbus.entity.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    
    @Query("SELECT sb FROM SeatBooking sb WHERE sb.booking.id = :bookingId")
    List<SeatBooking> findByBookingId(@Param("bookingId") Long bookingId);
    
    @Query("SELECT sb.seatNumber FROM SeatBooking sb WHERE sb.booking.schedule.id = :scheduleId AND sb.booking.bookingDate = :bookingDate")
    List<Integer> findBookedSeatNumbersByScheduleAndDate(@Param("scheduleId") Long scheduleId, 
                                                       @Param("bookingDate") java.time.LocalDate bookingDate);
}
