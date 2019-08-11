package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.data.{Organization, Searchable, ZendeskPickle}

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

  case class SearchResponse(entities: Seq[Searchable]) extends Response {
    import au.id.sommerville.zendesk.search.data.ZendeskPickle._

    override def out: Seq[String] = {
      def writeEntity[T](t: T) = {
        (t match {
          case o: Organization => write(o, 2)
          case _ => ""
        }).split("[\n\r]+")
          .filter(_.trim.nonEmpty)
      }

      entities.flatMap( writeEntity(_))
    }
  }
}
