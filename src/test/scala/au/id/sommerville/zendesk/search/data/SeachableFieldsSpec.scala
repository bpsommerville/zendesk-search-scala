package au.id.sommerville.zendesk.search.data

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class SeachableFieldsSpec extends UnitTestSpec {
  "Organization" should "specify all fields" in {
    Organization.fields should equal( Seq(
      SearchableField("_id", FieldType.Int),
      SearchableField("url", FieldType.String),
      SearchableField("externalId", FieldType.String),
      SearchableField("name", FieldType.String),
      SearchableField("domainNames", FieldType.String, collection = true),
      SearchableField("details", FieldType.String),
      SearchableField("createdAt", FieldType.DateTime),
      SearchableField("sharedTickets", FieldType.String),
      SearchableField("tags", FieldType.String, collection = true)
    ))
  }
}
