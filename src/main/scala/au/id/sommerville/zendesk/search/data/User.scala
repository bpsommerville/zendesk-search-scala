package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.{SearchError, UnknownFieldError}
import au.id.sommerville.zendesk.search.console.Entity
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Users}

case class User(
  _id: Int,
  url: String, // Could be converted to URL type
  externalId: String,
  name: String,
  alias: Option[String] = None,
  createdAt: OffsetDateTime,
  active: Boolean,
  verified: Option[Boolean] = None,
  shared: Boolean,
  locale: Option[String] = None,
  timezone: Option[String] = None,
  lastLoginAt: OffsetDateTime,
  email: Option[String] = None,
  phone: String,
  signature: String,
  organizationId: Option[Int] = None,
  tags: Set[String],
  suspended: Boolean,
  role: String
) extends Searchable {
  type IdType = Int
}

object User {
  implicit val rw: ZendeskPickle.ReadWriter[User] = ZendeskPickle.macroRW

  implicit object fields extends SearchableFields[User] {
    val entity: Entity = Users
    override def fromString(s: String): Either[SearchError, SearchableField[User]] = {
      f.find( _.name == s) match {
        case Some(sf) => Right(sf)
        case None => Left(UnknownFieldError(s))
      }
    }

    override def iterator: Iterator[SearchableField[User]] = f.iterator

    private val f: Seq[SearchableField[User]] = Seq(
       SearchableIntField("_id",e=>Some(e._id)),
       SearchableStringField("url",e=>Some(e.url)),
       SearchableStringField("externalId",e=>Some(e.externalId)),
       SearchableStringField("name",e=>Some(e.name)),
       SearchableStringField("alias",_.alias),
       SearchableDateTimeField("createdAt",e=>Some(e.createdAt)),
       SearchableBoolField("active",e=>Some(e.active)),
       SearchableBoolField("verified",_.verified),
       SearchableBoolField("shared",e=>Some(e.shared)),
       SearchableStringField("locale",_.locale) ,
       SearchableStringField("timezone",_.timezone) ,
       SearchableDateTimeField("lastLoginAt",e=>Some(e.lastLoginAt)),
       SearchableStringField("email",_.email),
       SearchableStringField("phone",e=>Some(e.phone)),
       SearchableStringField("signature",e=>Some(e.signature)),
       SearchableIntField("organizationId",_.organizationId) ,
       SearchableStringCollectionField("tags",e=>Some(e.tags)),
       SearchableBoolField("suspended",e=>Some(e.suspended)),
       SearchableStringField("role",e=>Some(e.role))
     )
  }
}

