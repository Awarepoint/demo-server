package com.awarepoint.demo.json
import spray.json._
import DefaultJsonProtocol._ // if you don't supply your own Protocol (see below)
import Device._
/**
 * Created by jordan on 5/7/15.
 */
object DataCodecs extends DefaultJsonProtocol {
  implicit val pointFormat = jsonFormat2(Point)
  implicit val roomFormat = jsonFormat2(Room)
  implicit object DeviceEmitDirectionJsonFormat extends RootJsonFormat[DeviceEmitDirection] {
    def write(ded: DeviceEmitDirection) = JsString(ded.value)
    def read(value: JsValue) = value match {
      case JsString(Directional.value) => Directional
      case JsString(Omni.value) => Omni
      case _ => throw new DeserializationException("DeviceEmitDirection expected")
    }
  }

  implicit object IdentifierTypeJsonFormat extends RootJsonFormat[IdentifierType] {
    def write(ded: IdentifierType) = JsString(ded.value)
    def read(value: JsValue) =  value match {
      case JsString(Mac.value)=> Mac
      case JsString(ProductIdentifier.value) => ProductIdentifier
      case _ => throw new DeserializationException("IdentifierType expected")
    }
  }

  implicit object DeviceRoomOrientationJsonFormat extends RootJsonFormat[DeviceRoomOrientation] {
    def write(ded: DeviceRoomOrientation) = JsString(ded.value)
    def read(value: JsValue) = value match {
      case JsString(Device.Room.value) => Device.Room
      case JsString(Hallway.value) => Hallway
      case _ => throw new DeserializationException("DeviceRoomOrientation expected")
    }
  }

  implicit val beaconFormat = jsonFormat7(BeaconLocation)

}
