package com.template.webserver

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
class Controller(rpc: NodeRPCConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    private val proxy = rpc.proxy
    private val nodeInfo = proxy.nodeInfo()
    private val myIdentity = nodeInfo.legalIdentities.first()


    // TODO create the endpoints for DAB
    @GetMapping(value = "/me")
    private fun me(): MeResponse{
        return MeResponse(
                O = myIdentity.name.toString(),
                Key = myIdentity.owningKey.toString(),
                registered = Instant.now()
        )
    }

    @GetMapping(value = "/test")
    private fun test(): TestResponse{
        return TestResponse(
                username = "Andy",
                screenName = "HazySmithers",
                email = "andrew.baxter02@gmail.com",
                registered = Instant.now()
        )
    }

    @GetMapping(value = "/templateendpoint", produces = arrayOf("text/plain"))
    private fun templateendpoint(): String {
        return "Define an endpoint here."
    }
}

/**
 * JSON object for test response
 */
data class TestResponse @JsonCreator constructor(
        val username: String,
        val screenName: String,
        val email: String,
        val registered: Instant
)

/**
 * JSON object for test response
 */
data class MeResponse @JsonCreator constructor(
        val O: String,
        val Key: String,
        val registered: Instant
)