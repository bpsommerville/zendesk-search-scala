package au.id.sommerville.zendesk.search.data

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class SeachableFieldsSpec extends UnitTestSpec {
  "Organization" should "specify all fields" in {
    Organization.fields.map(_.name) should equal(Seq(
      "_id",
      "url",
      "external_id",
      "name",
      "domain_names", //, FieldType.String, collection = true,
      "details",
      "created_at", //, FieldType.DateTime,
      "shared_tickets",
      "tags" //, FieldType.String, collection = tru)
    ))
  }
  "Users" should "specify all fields" in {
    User.fields.map(_.name) should equal(Seq(
      "_id",
      "url",
      "external_id",
      "name",
      "alias",
      "created_at",
      "active",
      "verified",
      "shared",
      "locale",
      "timezone",
      "last_login_at",
      "email",
      "phone",
      "signature",
      "organization_id",
      "tags",
      "suspended",
      "role"
    ))
  }

  "Tickets" should "specify all fields" in {
    Ticket.fields.map(_.name) should equal(Seq(
      "_id",
      "url",
      "external_id",
      "created_at",
      "type",
      "subject",
      "description",
      "priority",
      "status",
      "submitter_id",
      "assignee_id",
      "organization_id",
      "tags",
      "has_incidents",
      "due_at",
      "via"
    ))
  }
}
