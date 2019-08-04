package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

/**
 *
 */
case class Ticket(
  _id: String,
  url: String,
  externalId: String,
  createdAt: OffsetDateTime,
  `type`: String,
  subject: String,
  description: String,
  priority: String,
  status: String,
  submitterId: Int,
  assigneeId: Int,
  organizationId: Int,
  tags: Set[String],
  hasIncidents: Boolean,
  dueAt: OffsetDateTime,
  via: String
)

object Ticket {
  implicit val rw: ZendeskPickle.ReadWriter[Ticket] = ZendeskPickle.macroRW
}
