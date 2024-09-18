package me.hwiggy.shoelace.staff.utility

import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.code.DefaultCodeVerifier
import dev.samstevens.totp.code.HashingAlgorithm
import dev.samstevens.totp.qr.QrData
import dev.samstevens.totp.secret.DefaultSecretGenerator
import dev.samstevens.totp.time.SystemTimeProvider
import me.hwiggy.shoelace.staff.database.StorageProvider
import org.bukkit.configuration.ConfigurationSection
import java.util.*

class TOTPAuthentication(
    private val storageProvider: StorageProvider,
    authConf: ConfigurationSection
) {
    private val codeLength = authConf["code.length"] as Int
    private val codeHashAlg = (authConf["code.hash"] as String).let (
        HashingAlgorithm::valueOf
    )
    private val verifierPeriod = authConf["verifier.period"] as Int
    private val discrepancy = authConf["verifier.discrepancy"] as Int

    private val timeProvider = SystemTimeProvider()
    private val secretGenerator = DefaultSecretGenerator(16)
    private val codeGenerator = DefaultCodeGenerator(codeHashAlg, codeLength)
    private val codeVerifier = DefaultCodeVerifier(codeGenerator, timeProvider).also {
        it.setTimePeriod(verifierPeriod)
        it.setAllowedTimePeriodDiscrepancy(discrepancy)
    }

    private val qrIssuer = authConf["qrdata.issuer"] as String

    fun getCredential(id: UUID): Pair<String, Boolean> {
        val credential = storageProvider.credential(id)
        return if (credential == null) {
            storageProvider.storeCredential(id, secretGenerator.generate()) to true
        } else credential to false
    }

    fun testCode(id: UUID, code: String): Boolean {
        val (credential, _) = getCredential(id)
        return codeVerifier.isValidCode(credential, code)
    }

    fun getQRData(credential: String): QrData {
        return QrData.Builder()
            .secret(credential)
            .issuer(qrIssuer)
            .algorithm(codeHashAlg)
            .digits(codeLength)
            .period(verifierPeriod)
            .build()
    }
}