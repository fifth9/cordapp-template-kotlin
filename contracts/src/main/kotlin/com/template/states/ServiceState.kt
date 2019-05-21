package com.template.states

import com.template.contracts.ServiceContract
import net.corda.core.contracts.*
import net.corda.core.crypto.NullKeys
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.AnonymousParty
import java.time.Instant
import java.util.*

data class ServiceState (
        val issuance: PartyAndReference,
        override val owner: AbstractParty,
        val faceValue: Amount<Issued<Currency>>,
        val maturityDate: Instant
) : OwnableState {
    override val participants = listOf(owner)

    fun withoutOwner() = copy(owner = AnonymousParty(NullKeys.NullPublicKey))

    override fun withNewOwner(newOwner: AbstractParty) = CommandAndState(ServiceContract.Commands.Move(), copy(owner = newOwner))
}