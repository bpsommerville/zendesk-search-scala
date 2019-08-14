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


  implicit object fields extends SearchableFields[Organization] {
    override def fromString(s: String): SearchableField[Organization] = {
      f.find( _.name == s).get
    }

    override def iterator: Iterator[SearchableField[Organization]] = f.iterator

    private val f: Seq[SearchableField[Organization]] = Seq(
   //    SearchableField("_id", FieldType.Int)(_._id),
   //    SearchableField("url", FieldType.String)(_.url),
   //    SearchableField("externalId", FieldType.String)(_.externalId),
   //    SearchableField("name", FieldType.String)(_.name),
   //    SearchableField("domainNames", FieldType.String, collection = true)(_.domainNames(0)),
   //    SearchableField("details", FieldType.String)(_.details),
   //    SearchableField("createdAt", FieldType.DateTime)(_.createdAt),
   //    SearchableField("sharedTickets", FieldType.Bool)(_.sharedTickets),
   //    SearchableField("tags", FieldType.String, collection = true)(_ => "_.tags(0)")
       SearchableIntField("_id", _._id),
       SearchableStringField("url", _.url),
       SearchableStringField("externalId",_.externalId),
       SearchableStringField("name", _.name),
   //    SearchableField("domainNames", FieldType.String, collection = true)(_.domainNames(0)),
       SearchableStringField("details",_.details),
   //    SearchableField("createdAt", FieldType.DateTime)(_.createdAt),
   //    SearchableField("sharedTickets", FieldType.Bool)(_.sharedTickets),
   //    SearchableField("tags", FieldType.String, collection = true)(_ => "_.tags(0)")
     )
  }
}
