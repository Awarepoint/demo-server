package com.awarepoint.demo.json.test

import com.awarepoint.demo.json.{DataCodecs, BeaconLocation}
import org.scalatest._

import DataCodecs._
import spray.json._
/**
 * Created by jordan on 5/7/15.
 */
class CodecsTest extends FlatSpec with Matchers {
  val source = """{
"x":1.0,
"y":2.0,
"beaconId":"00:11:22:33:44:55",
"roomId":8976,
"direction":"Omni",
"beaconIdType":"Mac",
"orientation":"Hallway"
}"""

  "A Codec" should "translate from JSON to Scala and back" in {
    val jsonAst = source.parseJson
    println(jsonAst.prettyPrint)
    println(jsonAst.convertTo[BeaconLocation])
  }

}
