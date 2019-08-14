package au.id.sommerville.zendesk.search.data

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class SeachableFieldsSpec extends UnitTestSpec {
  "Organization" should "specify all fields" in {
    Organization.fields should equal( Seq(
      SearchableIntField("_id"),
      SearchableStringField("url"),
      SearchableStringField("externalId"),
      SearchableStringField("name" ),
      SearchableStringField("domainNames"), //, FieldType.String, collection = true),
      SearchableStringField("details"),
      SearchableStringField("createdAt"), //, FieldType.DateTime),
      SearchableStringField("sharedTickets"),
      SearchableStringField("tags") //, FieldType.String, collection = true)
    ))
  }
}
