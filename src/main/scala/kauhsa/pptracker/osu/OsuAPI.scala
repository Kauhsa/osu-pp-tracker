package kauhsa.pptracker.osu

import scala.concurrent.ExecutionContext

import org.json4s.Formats
import org.json4s.jvalue2extractable

import dispatch.Http
import dispatch.as
import dispatch.host
import dispatch.implyRequestHandlerTuple

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