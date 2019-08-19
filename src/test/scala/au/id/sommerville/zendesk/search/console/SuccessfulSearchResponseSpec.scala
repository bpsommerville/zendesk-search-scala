package au.id.sommerville.zendesk.search.console

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.Response.SuccessfulSearchResponse
import au.id.sommerville.zendesk.search.data._

/**
 *
 */
class SuccessfulSearchResponseSpec extends UnitTestSpec {
  "Org search response" should "convert single org to output lines" in {
    val orgs = Seq(
      Organization(1, "url", "id", "name", Seq("www.test.com", "www.example.com"),
        OffsetDateTime.parse("2011-12-31T14:30:12-10"), "details", sharedTickets = false,
        Set("one", "two", "three")
      )
    )
    SuccessfulSearchResponse(orgs).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:             1",
      "url:             url",
      "external_id:     id",
      "name:            name",
      "domain_names:    [ www.test.com, www.example.com ]",
      "created_at:      2011-12-31T14:30:12-10:00",
      "details:         details",
      "shared_tickets:  false",
      "tags:            [ one, two, three ]",
      "---------------------------------------------------------------------------"
    )
  }

  "User search response" should "convert single user to output lines" in {
    val users = Seq(
      User(1, "url", "id", "name", Some("alias"), OffsetDateTime.parse("2011-12-31T14:30:12-10"), false, Some(true), true,
        Some("en-au"), Some("timezone"), OffsetDateTime.parse("2016-12-31T14:30:12-10"), Some("email"), "phone", "sig",
        Some(666), Set("one", "two", "three"), true, "role")
    )
    SuccessfulSearchResponse(users).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "name:             name",
      "alias:            alias",
      "created_at        2011-12-31T14:30:12-10:00",
      "active:           false",
      "verified:         true",
      "shared:           true",
      "locale:           en-au",
      "timezone:         timezone",
      "last_login_at:    2016-12-31T14:30:12-10:00",
      "email:            email",
      "phone:            phone",
      "signature:        sig",
      "organization_id:  666",
      "tags:             [ one, two, three ]",
      "suspended:        true",
      "role:             role",
      "---------------------------------------------------------------------------"
    )
  }

  "User search response" should "not print fields that are Nones" in {
    val users = Seq(
      User(1, "url", "id", "name", None, OffsetDateTime.parse("2011-12-31T14:30:12-10"), false, None, true,
        None, None, OffsetDateTime.parse("2016-12-31T14:30:12-10"), None, "phone", "sig",
        None, Set("one", "two", "three"), true, "role")
    )
    SuccessfulSearchResponse(users).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "name:             name",
      "created_at        2011-12-31T14:30:12-10:00",
      "active:           false",
      "shared:           true",
      "last_login_at:    2016-12-31T14:30:12-10:00",
      "phone:            phone",
      "signature:        sig",
      "tags:             [ one, two, three ]",
      "suspended:        true",
      "role:             role",
      "---------------------------------------------------------------------------"
    )
  }


  "Ticket search response" should "convert single ticket to output lines" in {
    val tickets = Seq(
      Ticket("1", "url", "id", OffsetDateTime.parse("2011-12-11T14:30:12-10"), Some("bug"), "subject", Some("description"), "priority",
        "status", 66, Some(77), Some(666), Set("one", "two", "three"), false, Some(OffsetDateTime.parse("2019-12-31T14:30:12-10")),
        "web"
      )
    )
    SuccessfulSearchResponse(tickets).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "created_at:       2011-12-11T14:30:12-10:00",
      "type:             bug",
      "subject:          subject",
      "description:      description",
      "priority:         priority",
      "status:           status",
      "submitter_id:     66",
      "assignee_id:      77",
      "organization_id:  666",
      "tags:             [ one, two, three ]",
      "has_incidents:    false",
      "due_at:           2019-12-31T14:30:12-10:00",
      "via:              web",
      "---------------------------------------------------------------------------"
    )
  }

  "Ticket search response" should "not print fields that are None" in {
    val tickets = Seq(
      Ticket("1", "url", "id", OffsetDateTime.parse("2011-12-31T14:30:12-10"), None, "subject", None, "priority",
        "status", 66, None, None, Set("one", "two", "three"), false, None,
        "web"
      )
    )
    SuccessfulSearchResponse(tickets).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "created_at:       2011-12-31T14:30:12-10:00",
      "subject:          subject",
      "priority:         priority",
      "status:           status",
      "submitter_id:     66",
      "tags:             [ one, two, three ]",
      "has_incidents:    false",
      "via:              web",
      "---------------------------------------------------------------------------"

    )
  }


  "Resolved Org search response" should "convert single org & users + tickets to output lines" in {
    val orgs = Seq(
      ResolvedOrganization(
        Organization(1, "url", "id", "name", Seq("www.test.com", "www.example.com"),
          OffsetDateTime.parse("2011-12-31T14:30:12-10"), "details", sharedTickets = false,
          Set("one", "two", "three")
        ),
        Some(Seq(
          FakeData.user(101).copy(name = "John Smith"),
          FakeData.user(102).copy(name = "Bryan Adams")
        )),
        Some(Seq(
          FakeData.ticket("aaa").copy(priority = "high", status = "waiting", subject = "Bad show"),
          FakeData.ticket("bbbbbb").copy(priority = "urgent", status = "open", subject = "Help")
        ))
      )
    )
    SuccessfulSearchResponse(orgs).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:             1",
      "url:             url",
      "external_id:     id",
      "name:            name",
      "domain_names:    [ www.test.com, www.example.com ]",
      "created_at:      2011-12-31T14:30:12-10:00",
      "details:         details",
      "shared_tickets:  false",
      "tags:            [ one, two, three ]",
      "users:",
      "               - John Smith : 101",
      "               - Bryan Adams : 102",
      "tickets:",
      "               - Bad show : ( high, waiting ) : aaa",
      "               - Help : ( urgent, open ) : bbbbbb",
      "---------------------------------------------------------------------------"
    )
  }

  "Resolved User search response" should "print user with org, submitted tickets and assigned tickets" in {
    val users = Seq(
      ResolvedUser(
        User(1, "url", "id", "name", None, OffsetDateTime.parse("2011-12-31T14:30:12-10"), false, None, true,
          None, None, OffsetDateTime.parse("2016-12-31T14:30:12-10"), None, "phone", "sig",
          Some(666), Set("one", "two", "three"), true, "role"
        ),
        Some(FakeData.org(666).copy(name = "Acme Co")),
        Some(Seq(
          FakeData.ticket("ccc").copy(priority = "high", status = "waiting", subject = "Good show"),
          FakeData.ticket("bbbbbb").copy(priority = "urgent", status = "open", subject = "Help")
        )),
        Some(Seq(
          FakeData.ticket("aaa").copy(priority = "high", status = "waiting", subject = "Bad show"),
          FakeData.ticket("bbbbbb").copy(priority = "urgent", status = "open", subject = "Help")
        ))
      )
    )
    SuccessfulSearchResponse(users).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "name:             name",
      "created_at        2011-12-31T14:30:12-10:00",
      "active:           false",
      "shared:           true",
      "last_login_at:    2016-12-31T14:30:12-10:00",
      "phone:            phone",
      "signature:        sig",
      "organization_id:  666 ( Acme Co )",
      "tags:             [ one, two, three ]",
      "suspended:        true",
      "role:             role",
      "submitted_tickets:",
      "                - Good show : ( high, waiting ) : ccc",
      "                - Help : ( urgent, open ) : bbbbbb",
      "assigned_tickets:",
      "                - Bad show : ( high, waiting ) : aaa",
      "                - Help : ( urgent, open ) : bbbbbb",
      "---------------------------------------------------------------------------"
    )
  }

  "Resolved Ticket search response" should "convert single ticket to output lines" in {
    val tickets = Seq(
      ResolvedTicket(
        Ticket("1", "url", "id", OffsetDateTime.parse("2011-12-11T14:30:12-10"), Some("bug"), "subject", Some("description"), "priority",
          "status", 66, Some(77), Some(666), Set("one", "two", "three"), false, Some(OffsetDateTime.parse("2019-12-31T14:30:12-10")),
          "web"
        ),
        Some(FakeData.org(666).copy(name = "Acme Co")),
        Some(FakeData.user(66).copy(name = "Ben Jones")),
        Some(FakeData.user(77).copy(name = "Tom Allen"))
      )
    )
    SuccessfulSearchResponse(tickets).out should contain theSameElementsInOrderAs Seq(
      "---------------------------------------------------------------------------",
      "_id:              1",
      "url:              url",
      "external_id:      id",
      "created_at:       2011-12-11T14:30:12-10:00",
      "type:             bug",
      "subject:          subject",
      "description:      description",
      "priority:         priority",
      "status:           status",
      "submitter_id:     66 ( Ben Jones )",
      "assignee_id:      77 ( Tom Allen )",
      "organization_id:  666 ( Acme Co )",
      "tags:             [ one, two, three ]",
      "has_incidents:    false",
      "due_at:           2019-12-31T14:30:12-10:00",
      "via:              web",
      "---------------------------------------------------------------------------"
    )
  }

}
