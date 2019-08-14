package au.id.sommerville.zendesk.search.data

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class SeachableFieldsSpec extends UnitTestSpec {
  "Organization" should "specify all fields" in {
    Organization.fields.map(_.name) should equal( Seq(
      "_id",
      "url",
      "externalId",
      "name" ,
      "domainNames", //, FieldType.String, collection = true,
      "details",
      "createdAt", //, FieldType.DateTime,
      "sharedTickets",
      "tags" //, FieldType.String, collection = tru)
    ))
  }
}
