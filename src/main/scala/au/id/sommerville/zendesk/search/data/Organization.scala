package au.id.sommerville.zendesk.search.data
import java.time.OffsetDateTime

case class Organization(
  _id: Int,
  url: String,  // Could be converted to URL type
  externalId: String,
  name: String,
  domainNames: Seq[String],
  createdAt: OffsetDateTime,
  details: String,
  sharedTickets: Boolean,
  tags: Set[String]
)

object Organization {
  implicit val rw: ZendeskPickle.ReadWriter[Organization] = ZendeskPickle.macroRW
}
