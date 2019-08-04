package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

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
)

object Ticket {
  implicit val rw: ZendeskPickle.ReadWriter[Ticket] = ZendeskPickle.macroRW
}
