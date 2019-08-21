import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.typesafe.config._


class ALoadTest extends Simulation {

	val threadsOrUsers: Int = 1
	val qps: Int = 4
	val loopsPerThread: Int = qps/threadsOrUsers toInt
	
	val httpProtocol = http
    //.baseUrl("http://10.40.154.41/stg-rakuten-dc/es/1.0/rms-app-api/") // Here is the root for all relative URLs
    //.proxy(Proxy("218.251.227.5", 80))
    .baseUrl("http://blazedemo.com/")
	.contentTypeHeader("application/json; charset=UTF-8")

	val user_headers = Map("Authorization" -> "ESA MlJvTU5NWGRoRU0zRFQwUTpIOTdoRDdHOWZibmlwYVRp") // Note the headers specific to a given request
	
	val test = scenario("load_test")
    // .repeat(loopsPerThread) {
	    // exec(session => (
      //   session.set("date", date)
      // ))
      .exec(http("request")
        // .get("shop/report")
		    // .queryParam("date", "${date}")
        //.headers(user_headers)
        .get("")
        .check(status.is(200))
      )
      .pause(10)
    // }
	
   val initialThreads: Int = 0
   val numberOfThreads: Int = 10
   val rampUpDuration: Int = 4
   val duration:Int = 20
   

setUp(
  test.inject(
    /*Open model load test config*/
    // nothingFor(4 seconds), // 1
    // atOnceUsers(10), // 2
    // rampUsers(10) during (5 seconds), // 3
    // constantUsersPerSec(20) during (15 seconds), // 4
    // constantUsersPerSec(20) during (15 seconds) randomized, // 5
    // rampUsersPerSec(10) to 20 during (1 minutes), // 6
    // rampUsersPerSec(10) to 20 during (1 minutes) randomized, // 7
    // heavisideUsers(1000) during (20 seconds) // 8

    /*Closed model load test config*/
    //  constantConcurrentUsers(0) during (30 seconds),
    rampConcurrentUsers(0) to (10) during (5 seconds), // 1
    constantConcurrentUsers(10) during (30 seconds),
    rampConcurrentUsers(10) to (20) during (5 seconds), // 2
    constantConcurrentUsers(20) during (20 seconds),
    rampConcurrentUsers(20) to (30) during (5 seconds), // 3
    constantConcurrentUsers(30) during (10 seconds),

    // rampUsers(numberOfThreads) during(rampUpDuration seconds),
    // nothingFor(1 seconds),
    // rampUsers(numberOfThreads) during(rampUpDuration seconds),
    // nothingFor(1 seconds),
    // rampUsers(numberOfThreads) during(rampUpDuration seconds),
    // nothingFor(1 seconds)
  ),
).protocols(httpProtocol)
	
}