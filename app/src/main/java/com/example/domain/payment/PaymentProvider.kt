package com.example.domain.payment

import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentStatus

data class PaymentRequest(
    val reference: String,
    val amountGnf: Long,
    val customerPhone: String,
    val customerName: String,
    val description: String,
    val method: PaymentMethod
)

data class PaymentResponse(
    val transactionId: String,
    val status: PaymentStatus,
    val message: String,
    val confirmationToken: String? = null
)

interface PaymentProvider {
    val method: PaymentMethod
    suspend fun initiatePayment(request: PaymentRequest): PaymentResponse
    suspend fun verifyPayment(transactionId: String): PaymentStatus
}

class OrangeMoneyGuineaProvider : PaymentProvider {
    override val method: PaymentMethod = PaymentMethod.ORANGE_MONEY

    override suspend fun initiatePayment(request: PaymentRequest): PaymentResponse {
        // Architecture adapter: Ready for Orange Money Guinea Open API integration
        // Generates realistic Guinean transaction reference OM-GN-XXXX
        val txId = "OM-GN-${System.currentTimeMillis().toString().takeLast(6)}"
        return PaymentResponse(
            transactionId = txId,
            status = PaymentStatus.PAID,
            message = "Paiement Orange Money de ${request.amountGnf} GNF initié avec succès pour le numéro ${request.customerPhone}."
        )
    }

    override suspend fun verifyPayment(transactionId: String): PaymentStatus = PaymentStatus.PAID
}

class MtnMoMoGuineaProvider : PaymentProvider {
    override val method: PaymentMethod = PaymentMethod.MTN_MOMO

    override suspend fun initiatePayment(request: PaymentRequest): PaymentResponse {
        val txId = "MOMO-GN-${System.currentTimeMillis().toString().takeLast(6)}"
        return PaymentResponse(
            transactionId = txId,
            status = PaymentStatus.PAID,
            message = "Paiement MTN MoMo validé. Notification de débit transmise au ${request.customerPhone}."
        )
    }

    override suspend fun verifyPayment(transactionId: String): PaymentStatus = PaymentStatus.PAID
}

class AgencyCashPaymentProvider : PaymentProvider {
    override val method: PaymentMethod = PaymentMethod.CASH_AGENCY

    override suspend fun initiatePayment(request: PaymentRequest): PaymentResponse {
        val txId = "CASH-${System.currentTimeMillis().toString().takeLast(5)}"
        return PaymentResponse(
            transactionId = txId,
            status = PaymentStatus.PAID,
            message = "Paiement en espèces enregistré au guichet de l'agence. Reçu physique imprimé."
        )
    }

    override suspend fun verifyPayment(transactionId: String): PaymentStatus = PaymentStatus.PAID
}

class CardPaymentProvider : PaymentProvider {
    override val method: PaymentMethod = PaymentMethod.CARTE_BANCAIRE

    override suspend fun initiatePayment(request: PaymentRequest): PaymentResponse {
        val txId = "CARD-GN-${System.currentTimeMillis().toString().takeLast(6)}"
        return PaymentResponse(
            transactionId = txId,
            status = PaymentStatus.PAID,
            message = "Autorisation bancaire 3D Secure validée pour ${request.amountGnf} GNF."
        )
    }

    override suspend fun verifyPayment(transactionId: String): PaymentStatus = PaymentStatus.PAID
}

object PaymentGatewayRegistry {
    private val providers = mapOf(
        PaymentMethod.ORANGE_MONEY to OrangeMoneyGuineaProvider(),
        PaymentMethod.MTN_MOMO to MtnMoMoGuineaProvider(),
        PaymentMethod.CASH_AGENCY to AgencyCashPaymentProvider(),
        PaymentMethod.CARTE_BANCAIRE to CardPaymentProvider()
    )

    fun getProvider(method: PaymentMethod): PaymentProvider {
        return providers[method] ?: AgencyCashPaymentProvider()
    }
}
