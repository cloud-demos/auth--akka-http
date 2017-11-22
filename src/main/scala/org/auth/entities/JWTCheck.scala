package org.auth.entities

import java.util.Optional

import akka.http.javadsl.model.HttpHeader
import akka.http.scaladsl.model.headers.HttpChallenges
import akka.http.scaladsl.server.AuthenticationFailedRejection.CredentialsRejected
import akka.http.scaladsl.server.directives.RouteDirectives
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Directive0}

trait JWTCheckDirectives extends RouteDirectives {

  import akka.http.scaladsl.server.directives.BasicDirectives._

  def validateToken: Directive0 = extractRequestContext.flatMap { ctx =>
    val v: Optional[HttpHeader] = ctx.request.getHeader("code")

    if (v.isPresent && v.get().value() == "aa") {

      mapResponse { response =>
        response
      }

    } else {

      val basicChallenge = HttpChallenges.basic("CodeRealm")

      reject(AuthenticationFailedRejection(CredentialsRejected, basicChallenge))

    }

  }

}

object JWTDirectives extends JWTCheckDirectives
