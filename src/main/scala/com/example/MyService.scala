package com.example

import akka.actor.Actor
import com.awarepoint.demo.json.{Room, Point}
import spray.http.MediaTypes._
import spray.http._
import spray.routing._

import scala.io.Source
import java.io.File
import com.awarepoint.demo.json.{DataCodecs, BeaconLocation}
import DataCodecs._
import spray.json._

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

  val myRoute = images ~ emrLookup ~ getAllRooms

  val jordanEmr = jsonFile("JordanMeyers.json")

  def emrLookup = path("[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}".r){ mac =>
      respondWithMediaType(`application/json`){ complete(jordanEmr) }
  }

  def images = path("image" / "(\\S+)".r){s =>
    val IMG_DIR = "src/main/resources/image/"
    val (mediaType,img) = s match {
      case "eampleEMR" => (`image/gif`,IMG_DIR+"eampleEMR.gif")
      case _ =>           (`image/png`,IMG_DIR+"profile1.png")
    }

    respondWithMediaType(mediaType){complete( HttpData(new File(img)) )}
  }

  //Creates a number of rooms in a straight line, all sharing a single hallway above them
  val TOTAL_ROOMS = 10
  val rooms = (
    for{
      i <- (1 to TOTAL_ROOMS).toList
      a = Point((i-1)*10,0)
      b = Point(    i*10,0)
      c = Point(a.x,10)
      d = Point(b.x,10)
      vertices = Set(a,b,c,d)
    } yield Room(i,vertices)
  ) :+ Room(TOTAL_ROOMS+1,Set(Point(0,10),Point(0,20),Point(TOTAL_ROOMS*10,10),Point(TOTAL_ROOMS*10,20)))

  val roomsString = "["+rooms.map(_.toJson).mkString(",")+"]"

  def getAllRooms = path("getAllRooms"){
    respondWithMediaType(`application/json`){
      complete{roomsString}
    }
  }
}