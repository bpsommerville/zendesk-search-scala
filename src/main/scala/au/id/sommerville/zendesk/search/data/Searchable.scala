package au.id.sommerville.zendesk.search.data

trait Searchable {
  val _id: Int
}


trait FieldType

object FieldType {
  case object String extends FieldType

  case object Int extends FieldType

  case object DateTime extends FieldType


}

case class SearchableField(
  name: String,
  `type`: FieldType,
  collection: Boolean = false
)