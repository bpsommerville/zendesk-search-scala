package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.{SearchError, UnknownFieldError}
import au.id.sommerville.zendesk.search.console.Entity
import au.id.sommerville.zendesk.search.console.Entity.{Tickets, Users}

case class Ticket(
  _id: String,
  url: String,
  externalId: String,
  createdAt: OffsetDateTime,
  `type`: Option[String] = None,
  subject: String,
  description: Option[String] = None,
  priority: String,
  status: String,
  submitterId: Int,
  assigneeId: Option[Int] = None,
  organizationId: Option[Int] = None,
  tags: Set[String],
  hasIncidents: Boolean,
  dueAt: Option[OffsetDateTime] = None,
  via: String
) extends Searchable {
  type IdType = String
}

object Ticket {
  implicit val rw: ZendeskPickle.ReadWriter[Ticket] = ZendeskPickle.macroRW

  implicit object fields extends SearchableFields[Ticket] {
    val entity: Entity = Tickets
    override def fromString(s: String): Either[SearchError, SearchableField[Ticket]] = {
      f.find( _.name == s) match {
        case Some(sf) => Right(sf)
        case None => Left(UnknownFieldError(s))
      }
    }

    override def iterator: Iterator[SearchableField[Ticket]] = f.iterator

    private val f: Seq[SearchableField[Ticket]] = Seq(
       SearchableStringField("_id",e=>Some(e._id)),
       SearchableStringField("url",e=>Some(e.url)),
       SearchableStringField("externalId",e=>Some(e.externalId)),
       SearchableDateTimeField("createdAt",e=>Some(e.createdAt)),
       SearchableStringField("type", _.`type`),
       SearchableStringField("subject",e=>Some(e.subject)),
       SearchableStringField("description",_.description),
       SearchableStringField("priority",e=>Some(e.priority)),
       SearchableStringField("status",e=>Some(e.status)),
       SearchableIntField("submitterId",e=>Some(e.submitterId)),
       SearchableIntField("assigneeId",_.assigneeId),
       SearchableIntField("organizationId",_.organizationId),
       SearchableStringCollectionField("tags",e=>Some(e.tags)),
       SearchableBoolField("hasIncidents",e=>Some(e.hasIncidents)),
       SearchableDateTimeField("dueAt",_.dueAt),
       SearchableStringField("via",e=>Some(e.via))
    )
  }
}
