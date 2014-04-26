package kauhsa.pptracker

import dispatch._
import org.json4s._
import scala.concurrent.ExecutionContext
import scala.util.{Try, Success, Failure}

class OsuAPI(APIKey: String)(implicit ec: ExecutionContext, formats: Formats) {
  val APIHost = host("osu.ppy.sh") / "api" secure
  
  private def getRequest(cmd: String, params: Tuple2[String, String]*) = {
    val paramMap = Map("k" -> APIKey) ++ params
    APIHost / cmd <<? paramMap
  }
  
  def getUser(user: String) = {
    Http(getRequest("get_user", "u" -> user) OK as.json4s.Json) map { json => json(0).extract[UserResult] }
  }
}