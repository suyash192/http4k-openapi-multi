package com.suyashbelekar.http4kopenapimulti

import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Argo
import org.http4k.lens.Query
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

val queryNamesLens = Query.string().multi.required("name")

val greetRoute = "/greet" meta {
    summary = "greet api"
    queries += queryNamesLens
} bindContract Method.GET to { request ->
    val names = queryNamesLens(request)
    Response(OK).body("Hello ${names}")
}

fun main() {
    val contract = contract {
        renderer = OpenApi3(ApiInfo("my great api", "v1.0"), Argo)
        descriptionPath = "/docs/swagger.json"
        routes += greetRoute
    }

    routes(
        "/" bind contract
    ).asServer(Jetty(8080)).start()
}