import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ThroughputV2 extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142")

  var count = 0
  def counter(): Int = { count += 1; count }
  val feeder = Iterator.continually(Map("count" -> (counter())))

  val scn = scenario("ConnectWebSocket")
    .feed(feeder)
    .exec(ws("Connect WS").open("/pcp2/${count}"))
    .repeat(1000, "i") {
      exec(ws("Send Message")
        .sendText( """{"id": "f859455e-3cbb-4748-a3c4-a7d2a4e69bda", "message_type": "hello${i}", "target": "pcp://client01.example.com/${count}"}""" )
        .check(wsAwait.within(1).until(1).regex("hello${i}"))
        )
    }
    .exec(ws("Close WS").close)

  setUp(
    scn.inject(atOnceUsers(10))
  ).protocols(httpConf)
}
