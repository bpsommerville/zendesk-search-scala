package au.id.sommerville.zendesk.search.console

trait Response {
  def out: Seq[String]
}

object Response {

  case class FixedResponse(lines: String*) extends Response {
    override def out: Seq[String] = lines.toSeq
  }

  object Welcome extends FixedResponse(
      "Welcome to Zendesk Search"
  )

  object Help extends FixedResponse(
    "Enter command:",
    "* search (s)",
    "* fields (f)",
    "* help (h)",
    "* quit (q)"
  )
}
