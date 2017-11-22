package org.auth.services

import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import org.auth.DefaultJsonFormats
import org.auth.entities.JWTCheckDirectives

import scala.concurrent.ExecutionContext

class Login(
    implicit executionContext: ExecutionContext,
    materializer: ActorMaterializer
) extends Directives
    with JWTCheckDirectives
    with DefaultJsonFormats {

  val route = login

  def login: Route =
    path("login") {

      post {

        complete("ok")

      }

    } ~ path("refresh-token") {

      post {

        validateToken {

          complete("ok")

        }

      }

    }

}
