package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

/**
 *
 */
case class User(
  _id: Int,
  url: String, // Could be converted to URL type
  externalId: String,
  name: String,
  alias: String,
  createdAt: String,
  active: Boolean,
  verified: Boolean,
  shared: Boolean,
  locale: String,
  timezone: String,
  lastLoginAt: OffsetDateTime,
  email: String,
  phone: String,
  signature: String,
  tags: Set[String],
  suspended: Boolean,
  role: String
)

object User {
  implicit val rw: ZendeskPickle.ReadWriter[User] = ZendeskPickle.macroRW
}

