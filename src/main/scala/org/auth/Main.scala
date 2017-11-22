package org.auth

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import org.auth.services.{HealthCheck, Login}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App with RouteConcatenation with DefaultJsonFormats {
  implicit val system: ActorSystem = ActorSystem("service-auth")

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // FIX: To obtain it via Consul
  //val conf: Config = ConfigFactory.load()

  val consul = new HealthCheck()
  val login = new Login()

  val routes =
    cors()(
      pathPrefix("api") {
        pathPrefix("v1") {
          pathPrefix("auth") {
            login.route
          } ~
          consul.route
        }
      }
    )

  //val activePort = System.getenv("NOMAD_PORT_http").toInt
  val activePort = 9000

  val localhost: String = InetAddress.getLocalHost.getHostAddress

  val bindingFuture = Http().bindAndHandle(routes, localhost, activePort)

  println(s"Server online at http://$localhost:${activePort}/\nPress RETURN to stop...")

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => {
      system.terminate()
    }) // and shutdown when done

}
