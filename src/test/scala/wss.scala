import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PCPConnections extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142")

  var count = 0
  def counter(): Int = { count += 1; count }
  val feeder = Iterator.continually(Map("count" -> (counter())))

  val scn = scenario("ConnectWebSocket")
    .feed(feeder)
    .exec(ws("Connect WS").open("/pcp2/${count}"))
    // Leave all connections open until the end.
    //.exec(ws("Close WS").close)

  setUp(
    scn.inject(constantUsersPerSec(100) during(10 seconds))
  ).protocols(httpConf)
}
