package com.awarepoint.demo.json

import com.awarepoint.demo.json.Device.{DeviceRoomOrientation, IdentifierType, DeviceEmitDirection}

import scala.collection.immutable.Set

/**
 * Created by jordan on 5/7/15.
 */
object Device {
  trait DeviceEmitDirection{ val value:String}
  case object Directional extends DeviceEmitDirection{val value = "Directional"}
  case object Omni extends DeviceEmitDirection{val value = "Omni"}

  trait IdentifierType{ val value:String}
  case object Mac extends IdentifierType{ val value="Mac"}
  case object ProductIdentifier extends IdentifierType{ val value="ProductIdentifier"}

  trait DeviceRoomOrientation{ val value:String}
  case object Room extends DeviceRoomOrientation{ val value="Room"}
  case object Hallway extends DeviceRoomOrientation{ val value="Hallway"}
}

case class BeaconLocation(x:Double,y:Double,beaconId:String,roomId:Long,direction:DeviceEmitDirection,
                          beaconIdType:IdentifierType,orientation:DeviceRoomOrientation){
  require(beaconId != null)
  require(direction != null)
  require(beaconIdType != null)
  require(orientation != null)
}

case class Point(x:Double,y:Double)

case class Room(id:Long,vertices:Set[Point]){
  require(vertices != null)
  require(!vertices.isEmpty)
}
