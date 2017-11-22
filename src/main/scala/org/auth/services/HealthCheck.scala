package org.auth.services

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import org.auth.DefaultJsonFormats

import scala.concurrent.ExecutionContext

class HealthCheck(
    implicit executionContext: ExecutionContext,
    materializer: ActorMaterializer,
    system: ActorSystem
) extends Directives
    with DefaultJsonFormats {

  val route = status

  def status =
    path("status") {
      get {
        complete("ok")
      }
    }

}
