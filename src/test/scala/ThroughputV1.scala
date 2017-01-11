import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ThroughputV1 extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142")

  var count1 = 0
  var count2 = 0
  var count3 = 0
  def counter1(): Int = { count1 += 1; count1 }
  def counter2(): Int = { count2 += 1; count2 }
  def counter3(): Int = { count3 += 1; count3 }
  val feeder = Iterator.continually(Map(
    "client_type" -> counter1(),
    "bytes" -> (pcp.associate_request("pcp://client01.example.com/"+counter2())),
    "message" -> (pcp.new_message("pcp://client01.example.com/"+counter3()))))

  val scn = scenario("ConnectWebSocket")
    .feed(feeder)
    // Specify client_type in URI to work with pcp-broker 1.0
    .exec(ws("Connect WS").open("/pcp/${client_type}"))
    .exec(
      ws("Association").sendBytes("${bytes}")
      // Checks don't work with binary messages. This may be fixed in Gatling 3.
      //.check(wsAwait.within(10).until(1))
    )
    .repeat(1000, "i") {
      exec(ws("Send Message")
        .sendBytes("${message}"))
    }
    .exec(ws("Close WS").close)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
