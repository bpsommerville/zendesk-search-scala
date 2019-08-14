package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets}
import au.id.sommerville.zendesk.search.console.Response.EntityFields
import au.id.sommerville.zendesk.search.data.{FieldType, Organization, SearchableField, SearchableStringField, Ticket}

/**
 *
 */
class EntityFieldsSpec extends UnitTestSpec {
  "EntityFields" should "list the entity and fields" in {

    EntityFields[Organization](Organizations).out should equal(Seq(
      "Search Organizations with:")
      ++ Organization.fields.map(f =>  s"  ${f.name}")
    )

//    EntityFields[Ticket](Tickets).out should equal(Seq(
//      "Search Tickets with:",
//      "  one",
//      "  two",
//      "  three"
//    ))
  }
}
