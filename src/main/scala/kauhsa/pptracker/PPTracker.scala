package kauhsa.pptracker

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import scala.Console.BLACK
import scala.Console.BOLD
import scala.Console.RESET
import scala.Console.WHITE
import scala.Console.println
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

import org.json4s.DefaultFormats
import org.json4s.Formats

import com.typesafe.config.ConfigFactory

import net.ceedubs.ficus.FicusConfig.intValueReader
import net.ceedubs.ficus.FicusConfig.stringValueReader
import net.ceedubs.ficus.FicusConfig.toFicusConfig
import osu.OsuAPI
import osu.UserResult

class OsuUpdater(apiKey: String, userName: String, updateIntervalSeconds: Int)(implicit ec: ExecutionContext, formats: Formats) {
  private val api = new OsuAPI(apiKey)
  
  private def getNewResult = {
    Await.result(api.getUser(userName), 5.seconds)
  }
  
  private def singleDiff(oldUserResult: UserResult, newUserResult: UserResult) = {
    val rankDiff = -(newUserResult.rank - oldUserResult.rank)
    val ppDiff = newUserResult.pp - oldUserResult.pp

    if (rankDiff != 0 && ppDiff != 0) {
      val dateFormat = new SimpleDateFormat("HH:mm:ss")
      val time = dateFormat.format(new Date())
      println(f"$BOLD$BLACK[$time]$WHITE PP: $ppDiff%+2.2f $BLACK|$WHITE Rank: $rankDiff%+d$RESET")
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