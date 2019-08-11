package au.id.sommerville.zendesk.search.data
import java.time.OffsetDateTime

case class Organization (
  _id: Int,
  url: String,  // Could be converted to URL type
  externalId: String,
  name: String,
  domainNames: Seq[String],
  createdAt: OffsetDateTime,
  details: String,
  sharedTickets: Boolean,
  tags: Set[String]
) extends Searchable

object Organization {
  implicit val rw: ZendeskPickle.ReadWriter[Organization] = ZendeskPickle.macroRW

  val fields: Seq[SearchableField] = Seq(
    SearchableField("_id", FieldType.Int),
    SearchableField("url", FieldType.String),
    SearchableField("externalId", FieldType.String),
    SearchableField("name", FieldType.String),
    SearchableField("domainNames", FieldType.String, collection = true),
    SearchableField("details", FieldType.String),
    SearchableField("createdAt", FieldType.DateTime),
    SearchableField("sharedTickets", FieldType.String),
    SearchableField("tags", FieldType.String, collection = true)
  )
}
