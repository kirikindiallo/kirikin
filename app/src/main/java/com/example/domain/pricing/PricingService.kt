package com.example.domain.pricing

import com.example.data.model.DeliveryType
import com.example.data.model.GuineanCities
import com.example.data.model.ShipmentType
import kotlin.math.abs

data class PricingConfig(
    val baseDocumentGnf: Long = 25000L,
    val baseStandardGnf: Long = 35000L,
    val baseVoluminousGnf: Long = 60000L,
    val baseElectronicGnf: Long = 45000L,
    val freeWeightThresholdKg: Double = 2.0,
    val extraPerKgGnf: Long = 8000L,
    val homeDeliveryFeeGnf: Long = 25000L,
    val urgentFeeGnf: Long = 20000L,
    val fragileFeeGnf: Long = 15000L,
    val insuranceRatePercent: Double = 0.015 // 1.5% of declared value
)

data class PricingBreakdown(
    val basePriceGnf: Long,
    val distanceSurchargeGnf: Long,
    val weightSurchargeGnf: Long,
    val homeDeliverySurchargeGnf: Long,
    val specialServicesSurchargeGnf: Long,
    val insuranceSurchargeGnf: Long,
    val totalGnf: Long
)

class PricingService(private var config: PricingConfig = PricingConfig()) {

    fun updateConfig(newConfig: PricingConfig) {
        config = newConfig
    }

    fun getConfig(): PricingConfig = config

    fun calculateParcelPrice(
        originCity: String,
        destinationCity: String,
        parcelType: ShipmentType,
        weightKg: Double,
        deliveryType: DeliveryType,
        isUrgent: Boolean,
        isFragile: Boolean,
        declaredValueGnf: Long,
        hasInsurance: Boolean
    ): PricingBreakdown {
        // 1. Base price by parcel category
        val basePrice = when (parcelType) {
            ShipmentType.DOCUMENT -> config.baseDocumentGnf
            ShipmentType.VOLUMINEUX -> config.baseVoluminousGnf
            ShipmentType.ELECTRONIQUE -> config.baseElectronicGnf
            else -> config.baseStandardGnf
        }

        // 2. Distance factor based on Guinean cities coordinates/km
        val cityOrigin = GuineanCities.findCity(originCity)
        val cityDest = GuineanCities.findCity(destinationCity)
        val distanceApprox = if (originCity.equals(destinationCity, ignoreCase = true)) {
            15
        } else {
            abs(cityOrigin.distanceKmFromConakry - cityDest.distanceKmFromConakry).coerceAtLeast(60)
        }
        val distanceSurcharge = when {
            distanceApprox > 600 -> 35000L // e.g. Conakry - Nzérékoré / Kankan
            distanceApprox > 300 -> 20000L // e.g. Conakry - Labé / Mamou
            distanceApprox > 100 -> 10000L // e.g. Conakry - Kindia
            else -> 5000L
        }

        // 3. Weight surcharge
        val extraWeight = (weightKg - config.freeWeightThresholdKg).coerceAtLeast(0.0)
        val weightSurcharge = (extraWeight * config.extraPerKgGnf).toLong()

        // 4. Home delivery fee
        val homeDeliverySurcharge = if (deliveryType == DeliveryType.HOME_DELIVERY) {
            config.homeDeliveryFeeGnf
        } else 0L

        // 5. Special handling options (Fragile, Urgent)
        var specialSurcharge = 0L
        if (isUrgent) specialSurcharge += config.urgentFeeGnf
        if (isFragile) specialSurcharge += config.fragileFeeGnf

        // 6. Insurance surcharge
        val insuranceSurcharge = if (hasInsurance && declaredValueGnf > 0) {
            (declaredValueGnf * config.insuranceRatePercent).toLong().coerceAtLeast(5000L)
        } else 0L

        val total = basePrice + distanceSurcharge + weightSurcharge + homeDeliverySurcharge + specialSurcharge + insuranceSurcharge

        return PricingBreakdown(
            basePriceGnf = basePrice,
            distanceSurchargeGnf = distanceSurcharge,
            weightSurchargeGnf = weightSurcharge,
            homeDeliverySurchargeGnf = homeDeliverySurcharge,
            specialServicesSurchargeGnf = specialSurcharge,
            insuranceSurchargeGnf = insuranceSurcharge,
            totalGnf = total
        )
    }
}
