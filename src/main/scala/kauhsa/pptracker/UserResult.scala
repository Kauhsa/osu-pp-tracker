package kauhsa.pptracker

case class UserResult(user_id: String, username: String, playcount: String, pp_rank: String, pp_raw: String) {
  def plays: Int = playcount.toInt
  def rank: Int = pp_rank.toInt
  def pp: Double = pp_raw.toDouble
}