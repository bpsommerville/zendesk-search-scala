package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets}
import au.id.sommerville.zendesk.search.console.Response.EntityFields
import au.id.sommerville.zendesk.search.data.{FieldType, SearchableField}

/**
 *
 */
class EntityFieldsSpec extends UnitTestSpec {
  "EntityFields" should "list the entity and fields" in {

    EntityFields(Organizations, Seq(
      SearchableField("one", FieldType.String),
      SearchableField("two", FieldType.String),
      SearchableField("three", FieldType.String)
    )).out should equal(Seq(
      "Search Organizations with:",
      "  one",
      "  two",
      "  three"
    ))

    EntityFields(Tickets, Seq(
      SearchableField("one", FieldType.String),
      SearchableField("two", FieldType.String),
      SearchableField("three", FieldType.String)
    )).out should equal(Seq(
      "Search Tickets with:",
      "  one",
      "  two",
      "  three"
    ))
  }
}
