package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.{NoResultsError, UnknownFieldError}
import au.id.sommerville.zendesk.search.data.Organization.fields
import au.id.sommerville.zendesk.search.data.{Organization, Searchable, SearchableField, SearchableFields, ZendeskPickle}

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
          case _ => ""
        }).split("[\n\r]+")
          .filter(_.trim.nonEmpty)
      }

      entities.flatMap( writeEntity(_))
    }
  }

  case class NotFoundSearchResponse(entity: Entity, field: String, value: String) extends SearchResponse{
    override def out: Seq[String] = Seq(s"No results found for ${entity} with ${field} = ${value}")
  }
  case class UnknownFieldSearchResponse(entity: Entity, field: String) extends SearchResponse{
    override def out: Seq[String] = Seq(s"${entity} does not have a searchable field: ${field}")
  }

  case class EntityFields[T <: Searchable](entity: Entity)(implicit fields: SearchableFields[T] ) extends Response {
    override def out: Seq[String] = {
      Seq(s"Search ${entity} with:") ++ fields.map( f =>s"  ${f.name}")
    }
  }

}
