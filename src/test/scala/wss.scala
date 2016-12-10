import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PCPConnections extends Simulation {
  val httpConf = http.wsBaseURL("wss://broker.example.com:8142").disableClientSharing

  var count = -1
  def counter(): Int = { count += 1; count }
  val feeder = Iterator.continually(Map(
    "gatling.http.ssl.keyStore.file" -> "%04dagent.example.com.ks".format(counter()),
    "gatling.http.ssl.keyStore.password" -> "foobarbaz",
    "gatling.http.ssl.trustStore.file" -> "truststore.ks",
    "gatling.http.ssl.trustStore.password" -> "foobarbaz"
  ))

  val scn = scenario("ConnectWebSocket")
    .feed(feeder)
    .exec(ws("Connect WS").open("/pcp"))

  setUp(
    scn.inject(atOnceUsers(50))
  ).protocols(httpConf)
}
