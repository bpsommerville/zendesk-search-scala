package au.id.sommerville.zendesk.search.data
import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.console.Entity
import au.id.sommerville.zendesk.search.console.Entity.Organizations
import au.id.sommerville.zendesk.search.{SearchError, UnknownFieldError}

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
    val entity: Entity = Organizations
    override def fromString(s: String): Either[SearchError, SearchableField[Organization]] = {
      f.find( _.name == s) match {
        case Some(sf) => Right(sf)
        case None => Left(UnknownFieldError(s))
      }
    }

    override def iterator: Iterator[SearchableField[Organization]] = f.iterator

    private val f: Seq[SearchableField[Organization]] = Seq(
       SearchableIntField("_id", _._id),
       SearchableStringField("url", _.url),
       SearchableStringField("externalId",_.externalId),
       SearchableStringField("name", _.name),
       SearchableStringCollectionField("domainNames",_.domainNames),
       SearchableStringField("details",_.details),
       SearchableDateTimeField("createdAt",_.createdAt),
       SearchableBoolField("sharedTickets", _.sharedTickets),
       SearchableStringCollectionField("tags", _.tags)
     )
  }
}
