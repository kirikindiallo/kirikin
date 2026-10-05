package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.DeliveryType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentStatus
import com.example.data.model.ShipmentStatus
import com.example.data.model.ShipmentType

class Converters {
    @TypeConverter
    fun fromShipmentStatus(value: ShipmentStatus): String = value.name

    @TypeConverter
    fun toShipmentStatus(value: String): ShipmentStatus = runCatching {
        ShipmentStatus.valueOf(value)
    }.getOrDefault(ShipmentStatus.CREATED)

    @TypeConverter
    fun fromShipmentType(value: ShipmentType): String = value.name

    @TypeConverter
    fun toShipmentType(value: String): ShipmentType = runCatching {
        ShipmentType.valueOf(value)
    }.getOrDefault(ShipmentType.PETIT_COLIS)

    @TypeConverter
    fun fromDeliveryType(value: DeliveryType): String = value.name

    @TypeConverter
    fun toDeliveryType(value: String): DeliveryType = runCatching {
        DeliveryType.valueOf(value)
    }.getOrDefault(DeliveryType.AGENCY_PICKUP)

    @TypeConverter
    fun fromPaymentMethod(value: PaymentMethod): String = value.name

    @TypeConverter
    fun toPaymentMethod(value: String): PaymentMethod = runCatching {
        PaymentMethod.valueOf(value)
    }.getOrDefault(PaymentMethod.CASH_AGENCY)

    @TypeConverter
    fun fromPaymentStatus(value: PaymentStatus): String = value.name

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = runCatching {
        PaymentStatus.valueOf(value)
    }.getOrDefault(PaymentStatus.PENDING)
}

@Database(
    entities = [
        ShipmentEntity::class,
        ShipmentEventEntity::class,
        TripEntity::class,
        BookingEntity::class,
        AgencyEntity::class,
        VehicleEntity::class,
        DriverEntity::class,
        IncidentEntity::class,
        SupportMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): GuineeTransitDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guinee_transit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
