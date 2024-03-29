package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.SearchError
import au.id.sommerville.zendesk.search.data._

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
    "* (s)earch  [ (o)organization | (u) user | (t)ticket ] <field> <search term>",
    "* (f)ields  [ (o)organization | (u) user | (t)ticket ]",
    "* (h)elp",
    "* (q)uit",
    "",
    "Examples:",
    " - to search tickets by type:  search tickets type bug",
    " - to search a field with an empty value '':  search tickets type",
    " - to search for a field that is not set: search tickets !assignee_id ",
  )

  trait SearchResponse extends Response

  case class SuccessfulSearchResponse(entities: Seq[Searchable]) extends SearchResponse {

    override def out: Seq[String] = {
      val separator = Seq("---------------------------------------------------------------------------")

      def writeEntity[T](t: T) = {
        separator ++ (
          t match {
            case o: Organization => ConsoleWriter.write(o)
            case ro: ResolvedOrganization => ConsoleWriter.write(ro)
            case u: User => ConsoleWriter.write(u)
            case ru: ResolvedUser => ConsoleWriter.write(ru)
            case t: Ticket => ConsoleWriter.write(t)
            case rt: ResolvedTicket => ConsoleWriter.write(rt)
            case _ => Seq()
          })
      }

      entities.flatMap(writeEntity(_)) ++ separator
    }

  }

  case class NotFoundSearchResponse(entity: Entity, field: String, value: Option[String]) extends SearchResponse {
    override def out: Seq[String] = Seq(s"No results found for ${entity} with ${field} ${value.map(v => s"= '${v}'").getOrElse("not set")}")
  }

  case class UnknownFieldSearchResponse(entity: Entity, field: String) extends SearchResponse with ErrorResponse {
    override def out: Seq[String] = Seq(s"${entity} does not have a searchable field: ${field}")
  }

  case class EntityFields[T <: Searchable](entity: Entity)(implicit fields: SearchableFields[T]) extends Response {
    override def out: Seq[String] = {
      Seq(s"Search ${entity} with:") ++ fields.map(f => s"  ${f.name}")
    }
  }

  trait ErrorResponse extends Response;

  case class UnknownCommandResponse(line: String) extends ErrorResponse {
    override def out: Seq[String] = Seq(s"'${line}' is not a valid command.")
  }

  case class UnknownSubCommandResponse(command: String, subCommand: String) extends ErrorResponse {
    override def out: Seq[String] = Seq(s"'${subCommand}' is not a valid option for the ${command} command")
  }

  case class UnexpectedErrorResponse(error: SearchError) extends ErrorResponse with SearchResponse {
    override def out: Seq[String] = Seq(s"Unexpected error : ${error}")
  }

}
