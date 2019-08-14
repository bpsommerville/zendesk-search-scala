package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets, Users}
import au.id.sommerville.zendesk.search.console.Response.{NotFoundSearchResponse, UnknownFieldSearchResponse}
import au.id.sommerville.zendesk.search.{NoResultsError, UnitTestSpec, UnknownFieldError}
import org.scalatest.prop.TableDrivenPropertyChecks

/**
 *
 */
class ErrorSearchResponseSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  val notFound = Table(
    ("nf", "out"),
    (NotFoundSearchResponse(Organizations, "_id", "124412"), "No results found for Organizations with _id = 124412"),
    (NotFoundSearchResponse(Users, "_id", "124412"), "No results found for Users with _id = 124412"),
    (NotFoundSearchResponse(Tickets, "_id", "124412"), "No results found for Tickets with _id = 124412")
  )

  "NotFound" should "output message describing entity, search field and value" in {

    forAll(notFound) { (nf: NotFoundSearchResponse, out: String) =>
      nf.out should equal(Seq(out))
    }
  }

  val unknownField = Table(
    ("uf", "out"),
    (UnknownFieldSearchResponse(Organizations, "bad"), "No results found for Organizations with _id = 124412"),
    (UnknownFieldSearchResponse(Users, "bad"), "No results found for Users with _id = 124412"),
    (UnknownFieldSearchResponse(Tickets, "bad"), "No results found for Tickets with _id = 124412")
  )

  "UnknownField" should "output message describing entity and bad field" in {

    forAll(notFound) { (nf: NotFoundSearchResponse, out: String) =>
      nf.out should equal(Seq(out))
    }
  }
}