package com.redbus.repository.jpa;

import com.redbus.entity.Booking;
import com.redbus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByBookingReference(String bookingReference);
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByUserIdAndStatus(Long userId, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.schedule.id = :scheduleId AND b.bookingDate = :bookingDate")
    List<Booking> findBookingsByScheduleAndDate(@Param("scheduleId") Long scheduleId, 
                                              @Param("bookingDate") LocalDate bookingDate);
    
    @Query("SELECT COUNT(sb) FROM SeatBooking sb WHERE sb.booking.schedule.id = :scheduleId AND sb.booking.bookingDate = :bookingDate")
    Long countBookedSeatsByScheduleAndDate(@Param("scheduleId") Long scheduleId, 
                                         @Param("bookingDate") LocalDate bookingDate);
    
    @Query("SELECT sb.seatNumber FROM SeatBooking sb WHERE sb.booking.schedule.id = :scheduleId AND sb.booking.bookingDate = :bookingDate")
    List<Integer> findBookedSeatNumbers(@Param("scheduleId") Long scheduleId, 
                                       @Param("bookingDate") LocalDate bookingDate);
}
