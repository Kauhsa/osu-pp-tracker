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
import osu._
import net.ceedubs.ficus.FicusConfig._
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext
import java.io.File

class OsuUpdater(apiKey: String, userName: String, updateIntervalSeconds: Int)(implicit ec: ExecutionContext, formats: Formats) {
  private val api = new OsuAPI(apiKey)
  
  private def getNewResult = {
    Await.result(api.getUser(userName), 5.seconds)
  }
  
  private def singleDiff(oldUserResult: UserResult, newUserResult: UserResult) = {
    if (newUserResult != oldUserResult) {
      val rankDiff = newUserResult.rank - oldUserResult.rank
      val ppDiff = newUserResult.pp - oldUserResult.pp
      val playsDiff = newUserResult.plays - oldUserResult.plays
      println(s"Playcount: $playsDiff, PP: $ppDiff, Rank: $rankDiff")
    } else {
      println("no diff")
    }
  }
  
  def runForever = {
    Iterator.continually { Thread.sleep(updateIntervalSeconds * 1000); getNewResult }
            .sliding(2)
            .foreach { n => singleDiff(n(0), n(1)) }
  }  
}

object PPTracker extends App {
  implicit val formats = DefaultFormats
  
  val config = ConfigFactory.parseFile(new File("pptracker.conf"))
  val apiKey = config.as[String]("apiKey")
  val userName = config.as[String]("userName")
  val updateIntervalSeconds = config.as[Int]("updateIntervalSeconds")
  
  val osuUpdater = new OsuUpdater(apiKey, userName, updateIntervalSeconds)
  osuUpdater.runForever
}