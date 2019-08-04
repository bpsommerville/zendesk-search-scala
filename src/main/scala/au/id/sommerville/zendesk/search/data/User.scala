package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

case class User(
  _id: Int,
  url: String, // Could be converted to URL type
  externalId: String,
  name: String,
  alias: Option[String] = None,
  createdAt: String,
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
)

object User {
  implicit val rw: ZendeskPickle.ReadWriter[User] = ZendeskPickle.macroRW
}

