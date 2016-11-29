import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PCPConnections extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142")

  val scn = scenario("ConnectWebSocket")
    .exec(ws("Connect WS").open("/pcp"))
    .exec(ws("Association")
      .sendBytes(pcp.associate_request("pcp://client01.example.com/test01"))
      .check(wsAwait.within(30).until(1)))
    .exec(ws("Close WS").close)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
