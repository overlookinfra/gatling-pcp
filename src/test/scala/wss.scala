import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PCPConnections extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142")

  var count = 0
  def counter(): Int = { count += 1; count }
  val feeder = Iterator.continually(Map("bytes" -> (pcp.associate_request("pcp://client01.example.com/"+counter()))))

  val scn = scenario("ConnectWebSocket")
    .feed(feeder)
    .exec(ws("Connect WS").open("/pcp"))
    .exec(
      ws("Association").sendBytes("${bytes}")
      // Checks don't work with binary messages. This may be fixed in Gatling 3.
      // We also plan to switch to text-based messages. Punt for now.
      //.check(wsAwait.within(10).until(1))
    )
    // Leave all connections open until the end.
    //.exec(ws("Close WS").close)

  setUp(
    scn.inject(constantUsersPerSec(150) during(20 seconds))
  ).protocols(httpConf)
}
