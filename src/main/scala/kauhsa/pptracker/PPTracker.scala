package kauhsa.pptracker

import io._
import dispatch._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._
import scala.util.{ Failure, Success }

object PPTracker extends App {
  val UpdateIntervalMilliseconds = 1000 * 30
  implicit val formats = DefaultFormats
  
  val key = Source.fromFile("api_key.txt").mkString.trim
  val osu = new OsuAPI(key)
  
  def loop() = {
    var oldResult: Option[UserResult] = None
    
    while (true) {
      val result = Await.result(osu.getUser("Kauhsa"), 5.seconds)
      
      oldResult match {
        case Some(r) => {
          if (result != r) {
            println("change")
            val rankDiff = result.rank - r.rank
            val ppDiff = result.pp - r.pp
            val playsDiff = result.plays - r.plays
            println(s"Playcount: $playsDiff, PP: $ppDiff, Rank: $rankDiff")
          } else {
            println("no change")
          }
        }
        
        case None => {
          println("huh")
        }
      }
            
      oldResult = Some(result)
      Thread.sleep(UpdateIntervalMilliseconds)
    }
  }
  
  loop()  
}