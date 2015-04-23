package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import scala.io.Source

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  def jsonFile(name:String) = Source.fromFile("src/main/resources/"+name).mkString

  val myRoute = baseRoute ~ jordanRoute

  def baseRoute =  path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }

  lazy val jordanEmr = jsonFile("JordanMeyers.json")

  def jordanRoute = path("00:01:02:03:04:05") {
    get {
      respondWithMediaType(`application/json`){ complete(jordanEmr) }
    }
  }
}