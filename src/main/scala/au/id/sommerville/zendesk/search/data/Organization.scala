package au.id.sommerville.zendesk.search.data
import java.time.OffsetDateTime

import upickle.default.{ReadWriter, macroRW}

case class Organization(
  _id: Int,
  url: String,
  external_id: String,
  name: String,
  domain_names: Seq[String],
  created_at: OffsetDateTime,
  details: String,
  shared_tickets: Boolean,
  tags: Set[String]
)

object Organization {
  import PickleHelper._

  implicit val rw: ReadWriter[Organization] = macroRW
}
