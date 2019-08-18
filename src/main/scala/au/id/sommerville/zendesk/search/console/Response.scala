package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.{NoResultsError, SearchError, UnknownFieldError}
import au.id.sommerville.zendesk.search.data.Organization.fields
import au.id.sommerville.zendesk.search.data.{Organization, Searchable, SearchableField, SearchableFields, Ticket, User, ZendeskPickle}

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

  trait SearchResponse extends Response

  case class SuccessfulSearchResponse(entities: Seq[Searchable]) extends SearchResponse {
    import au.id.sommerville.zendesk.search.data.ZendeskPickle._

    override def out: Seq[String] = {
      def writeEntity[T](t: T) = {
        (t match {
          case o: Organization => write(o, 2)
          case u: User => write(u, 2)
          case t: Ticket => write(t, 2)
          case _ => ""
        }).split("[\n\r]+")
          .filter(_.trim.nonEmpty)
      }

      entities.flatMap( writeEntity(_))
    }
  }

  case class NotFoundSearchResponse(entity: Entity, field: String, value: Option[String]) extends SearchResponse{
    override def out: Seq[String] = Seq(s"No results found for ${entity} with ${field} ${value.map( v => s"= '${v}'").getOrElse("not set")}")
  }

  case class UnknownFieldSearchResponse(entity: Entity, field: String) extends SearchResponse with ErrorResponse {
    override def out: Seq[String] = Seq(s"${entity} does not have a searchable field: ${field}")
  }

  case class EntityFields[T <: Searchable](entity: Entity)(implicit fields: SearchableFields[T] ) extends Response {
    override def out: Seq[String] = {
      Seq(s"Search ${entity} with:") ++ fields.map( f =>s"  ${f.name}")
    }
  }

  trait ErrorResponse extends Response;

  case class UnknownCommandResponse(line: String) extends ErrorResponse {
    override def out: Seq[String] = Seq(s"'${line}' is not a valid command.")
  }

  case class UnknownSubCommandResponse(command: String, subCommand: String) extends ErrorResponse {
    override def out: Seq[String] = Seq(s"'${subCommand}' is not a valid option for the ${command} command")
  }

  case class UnexpectedErrorResponse(error: SearchError ) extends ErrorResponse with SearchResponse {
    override def out: Seq[String] = Seq(s"Unexpected error : ${error}")
  }

}
