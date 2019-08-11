package au.id.sommerville.zendesk.search.console

import java.time.{OffsetDateTime, ZoneOffset}

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.Response.SuccessfulSearchResponse
import au.id.sommerville.zendesk.search.data.Organization

/**
 *
 */
class SuccessfulSearchResponseSpec extends UnitTestSpec {
  "Org search response" should "convert single org to output lines" in {
    val orgs = Seq(Organization(1, "url", "id", "name", Seq(), OffsetDateTime.of(2011, 12, 31, 14, 30, 12, 0, ZoneOffset.ofHours(-10)), "details", sharedTickets = false, Set[String]()))
    SuccessfulSearchResponse(orgs).out should contain theSameElementsInOrderAs (
      Seq(
        "{",
        "  \"_id\": 1,",
        "  \"url\": \"url\",",
        "  \"external_id\": \"id\",",
        "  \"name\": \"name\",",
        "  \"domain_names\": [",
        "  ],",
        "  \"created_at\": \"2011-12-31T14:30:12-10:00\",",
        "  \"details\": \"details\",",
        "  \"shared_tickets\": false,",
        "  \"tags\": [",
        "  ]",
        "}")
      )
  }
}
