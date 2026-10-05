package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ShipmentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GuineeTransitDao {

    // --- Shipments ---
    @Query("SELECT * FROM shipments ORDER BY createdAt DESC")
    fun getAllShipments(): Flow<List<ShipmentEntity>>

    @Query("SELECT * FROM shipments WHERE trackingNumber = :trackingNumber LIMIT 1")
    fun getShipmentByTrackingNumber(trackingNumber: String): Flow<ShipmentEntity?>

    @Query("SELECT * FROM shipments WHERE trackingNumber = :trackingNumber LIMIT 1")
    suspend fun getShipmentSync(trackingNumber: String): ShipmentEntity?

    @Query("SELECT * FROM shipments WHERE currentStatus = :status ORDER BY createdAt DESC")
    fun getShipmentsByStatus(status: ShipmentStatus): Flow<List<ShipmentEntity>>

    @Query("SELECT * FROM shipments WHERE senderPhone = :phone OR recipientPhone = :phone ORDER BY createdAt DESC")
    fun getShipmentsByPhone(phone: String): Flow<List<ShipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipment(shipment: ShipmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipments(shipments: List<ShipmentEntity>)

    @Update
    suspend fun updateShipment(shipment: ShipmentEntity)

    @Query("UPDATE shipments SET currentStatus = :newStatus, currentCity = :currentCity WHERE trackingNumber = :trackingNumber")
    suspend fun updateShipmentStatus(trackingNumber: String, newStatus: ShipmentStatus, currentCity: String)

    // --- Shipment Events / Timeline ---
    @Query("SELECT * FROM shipment_events WHERE trackingNumber = :trackingNumber ORDER BY timestamp ASC")
    fun getEventsForShipment(trackingNumber: String): Flow<List<ShipmentEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipmentEvent(event: ShipmentEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipmentEvents(events: List<ShipmentEventEntity>)

    // --- Trips ---
    @Query("SELECT * FROM trips ORDER BY departureTime ASC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE originCity = :origin AND destinationCity = :destination ORDER BY departureTime ASC")
    fun searchTrips(origin: String, destination: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE tripNumber = :tripNumber LIMIT 1")
    fun getTrip(tripNumber: String): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE tripNumber = :tripNumber LIMIT 1")
    suspend fun getTripSync(tripNumber: String): TripEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripEntity>)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    // --- Bookings ---
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingNumber = :bookingNumber LIMIT 1")
    fun getBooking(bookingNumber: String): Flow<BookingEntity?>

    @Query("SELECT * FROM bookings WHERE bookingNumber = :bookingNumber LIMIT 1")
    suspend fun getBookingSync(bookingNumber: String): BookingEntity?

    @Query("SELECT * FROM bookings WHERE passengerPhone = :phone ORDER BY createdAt DESC")
    fun getBookingsByPhone(phone: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    // --- Agencies ---
    @Query("SELECT * FROM agencies ORDER BY city ASC")
    fun getAllAgencies(): Flow<List<AgencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgencies(agencies: List<AgencyEntity>)

    // --- Vehicles ---
    @Query("SELECT * FROM vehicles ORDER BY plate ASC")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    // --- Drivers ---
    @Query("SELECT * FROM drivers ORDER BY name ASC")
    fun getAllDrivers(): Flow<List<DriverEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverEntity>)

    // --- Incidents ---
    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncidents(incidents: List<IncidentEntity>)

    @Update
    suspend fun updateIncident(incident: IncidentEntity)

    // --- Support Messages ---
    @Query("SELECT * FROM support_messages WHERE trackingNumber = :trackingNumber ORDER BY timestamp ASC")
    fun getMessagesForShipment(trackingNumber: String): Flow<List<SupportMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportMessage(message: SupportMessageEntity)

    // --- Counts for Dashboard ---
    @Query("SELECT COUNT(*) FROM shipments")
    fun countTotalShipments(): Flow<Int>

    @Query("SELECT COUNT(*) FROM shipments WHERE currentStatus = 'IN_TRANSIT'")
    fun countShipmentsInTransit(): Flow<Int>

    @Query("SELECT COUNT(*) FROM shipments WHERE currentStatus = 'DELIVERED'")
    fun countShipmentsDelivered(): Flow<Int>

    @Query("SELECT COUNT(*) FROM bookings")
    fun countTotalBookings(): Flow<Int>

    @Query("SELECT COUNT(*) FROM incidents WHERE status != 'RESOLVED'")
    fun countActiveIncidents(): Flow<Int>

    @Query("SELECT SUM(totalGnf) FROM shipments WHERE paymentStatus = 'PAID'")
    fun sumShipmentRevenue(): Flow<Long?>

    @Query("SELECT SUM(amountGnf) FROM bookings WHERE paymentStatus = 'PAID'")
    fun sumBookingRevenue(): Flow<Long?>
}
