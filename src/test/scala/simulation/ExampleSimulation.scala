package simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import util.Constants

class ExampleSimulation extends Simulation {

  private val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(Constants.BASE_URL)
    .acceptHeader(Constants.CONTENT_TYPE)

  private val headers = Map(
    "example-header" -> Constants.EXAMPLE_HEADER
  )

  /*
  Creating a Scenario.
  It will execute a request to provided API Endpoint,
  check the response status and response body,
  and save the id from response.
   */
  private val createRequest: ScenarioBuilder = scenario(Constants.SCENARIO_NAME)
    .exec(http("")
      .post(Constants.ENDPOINT)
      .headers(headers)
      .body(ElFileBody(Constants.REQUEST_BODY_PATH))
      .asJson
    )

  /*
  In setUp(), we will inject the desired load to our API
   */
  setUp(
    createRequest.inject(
      constantUsersPerSec(Constants.USER_REQUESTS_PER_SECOND) during  (Constants.USER_DURATION)
    )
  ).protocols(httpProtocol)
}
