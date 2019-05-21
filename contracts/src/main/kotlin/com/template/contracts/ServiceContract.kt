package com.template.contracts

import com.template.states.ServiceState
import net.corda.core.contracts.*
import net.corda.core.identity.AbstractParty
import net.corda.core.transactions.LedgerTransaction
import net.corda.core.transactions.TransactionBuilder

import net.corda.finance.contracts.utils.sumCashBy

class ServiceContract : Contract {

    companion object {
        const val SC_PROGRAM_ID: ContractClassName = "com.template.contracts.ServiceContract"
    }

    interface Commands : CommandData {
        class Move : TypeOnlyCommandData(), Commands
        class Redeem : TypeOnlyCommandData(), Commands
        class Issue : TypeOnlyCommandData(), Commands
    }

    override fun verify(tx: LedgerTransaction) {
        // Group by everything except owner: any modification to the CP at all is considered changing it fundamentally.
        val groups = tx.groupStates(ServiceState::withoutOwner)

        // There are two possible things that can be done with this CP. The first is trading it. The second is redeeming
        // it for cash on or after the maturity date.
        val command = tx.commands.requireSingleCommand<ServiceContract.Commands>()

        val timeWindow: TimeWindow? = tx.timeWindow

        for ((inputs, outputs, _) in groups) {
            when(command.value) {
                is Commands.Move -> {
                    val input = inputs.single()
                    requireThat {
                        "the transaction is signed by the owner of the service instance" using (input.owner.owningKey in command.signers)
                        "the state is propagated" using (outputs.size == 1)

                        // Don't need to check anything else, as if outputs.size == 1 then the output is equal to
                        // the input ignoring the owner field due to the grouping.
                    }
                }

                is Commands.Redeem -> {
                    // Redemption of the service requires movement of on-ledger cash.
                    val input = inputs.single()
                    val received = tx.outputs.map { it.data }.sumCashBy(input.owner)
                    val time = timeWindow?.fromTime ?: throw IllegalArgumentException("Redemptions must be timestamped")
                    requireThat {
                        "the service must have matured" using (time >= input.maturityDate)
                        "the received amount equals the face value" using (received == input.faceValue)
                        "the service must be destroyed" using outputs.isEmpty()
                        "the transaction is signed by the owner of the service instance" using (input.owner.owningKey in command.signers)
                    }
                }

                is Commands.Issue -> {
                    val output = outputs.single()
                    val time = timeWindow?.untilTime ?: throw IllegalArgumentException("Issuances must be timestamped")
                    requireThat {
                        // Don't allow people to issue commercial paper under other entities identities.
                        "output states are issued by a command signer" using (output.issuance.party.owningKey in command.signers)
                        "output values sum to more than the inputs" using (output.faceValue.quantity > 0)
                        "the maturity date is not in the past" using (time < output.maturityDate)
                        // Don't allow an existing service instance state to be replaced by this issuance.
                        "can't reissue an existing state" using inputs.isEmpty()
                    }
                }

                else -> throw IllegalArgumentException("Unrecognised command")
            }
        }
    }

}

