package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.DeliveryType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentStatus
import com.example.data.model.ShipmentStatus
import com.example.data.model.ShipmentType

@Entity(tableName = "shipments")
data class ShipmentEntity(
    @PrimaryKey val trackingNumber: String, // e.g. GT-2026-00018452
    val secretToken: String,
    val senderName: String,
    val senderPhone: String,
    val senderCity: String,
    val senderAddress: String,
    val recipientName: String,
    val recipientPhone: String,
    val recipientCity: String,
    val recipientAddress: String,
    val deliveryType: DeliveryType,
    val parcelType: ShipmentType,
    val description: String,
    val weightKg: Double,
    val declaredValueGnf: Long,
    val isFragile: Boolean,
    val isUrgent: Boolean,
    val hasInsurance: Boolean,
    val basePriceGnf: Long,
    val weightSurchargeGnf: Long,
    val optionsSurchargeGnf: Long,
    val totalGnf: Long,
    val paymentStatus: PaymentStatus,
    val paymentMethod: PaymentMethod,
    val currentStatus: ShipmentStatus,
    val currentCity: String,
    val deliveryOtp: String,
    val tripId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveredAt: Long? = null,
    val deliveryProofRecipientName: String? = null,
    val deliveryProofSignatureDate: Long? = null
)

@Entity(tableName = "shipment_events")
data class ShipmentEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackingNumber: String,
    val timestamp: Long,
    val status: ShipmentStatus,
    val city: String,
    val agencyName: String,
    val description: String,
    val operatorName: String
)

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val tripNumber: String, // e.g. TRIP-2026-00981
    val originCity: String,
    val destinationCity: String,
    val departureDate: String, // e.g. "2026-10-02"
    val departureTime: String, // e.g. "07:30"
    val estimatedArrival: String, // e.g. "16:00"
    val durationHours: String, // e.g. "8h 30m"
    val companyName: String,
    val vehiclePlate: String,
    val vehicleType: String, // e.g. "Minibus VIP 14 places"
    val totalSeats: Int,
    val bookedSeatsCsv: String, // e.g. "1,2,5,6"
    val priceGnf: Long,
    val amenities: String // e.g. "Climatisé, Wi-Fi, Prises USB, 20kg bagage"
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingNumber: String, // e.g. RES-2026-0491
    val tripNumber: String,
    val passengerName: String,
    val passengerPhone: String,
    val seatNumber: Int,
    val originCity: String,
    val destinationCity: String,
    val departureDate: String,
    val departureTime: String,
    val companyName: String,
    val vehiclePlate: String,
    val amountGnf: Long,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val status: String, // "VALID", "BOARDED", "CANCELLED"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "agencies")
data class AgencyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    val address: String,
    val phone: String,
    val openingHours: String,
    val latitude: Double,
    val longitude: Double,
    val services: String // CSV e.g. "Voyages,Dépôt Colis,Retrait Colis,Paiement"
)

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val plate: String, // e.g. RC-4521-A
    val model: String,
    val capacity: Int,
    val company: String,
    val driverName: String,
    val status: String, // "Disponible", "En trajet", "Maintenance"
    val nextMaintenance: String
)

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val licenseNumber: String,
    val assignedVehiclePlate: String,
    val status: String, // "Disponible", "En trajet", "Repos"
    val rating: Double,
    val completedTrips: Int
)

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey val incidentNumber: String,
    val relatedTrackingOrTrip: String,
    val type: String, // "Colis endommagé", "Retard de route", "Panne mécanique", "Colis manquant"
    val description: String,
    val priority: String, // "FAIBLE", "MOYENNE", "HAUTE", "CRITIQUE"
    val status: String, // "OPEN", "INVESTIGATING", "RESOLVED"
    val reportedBy: String,
    val timestamp: Long = System.currentTimeMillis(),
    val resolutionNotes: String? = null
)

@Entity(tableName = "support_messages")
data class SupportMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackingNumber: String,
    val senderRole: String, // "CLIENT", "AGENT", "SUPPORT"
    val senderName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
