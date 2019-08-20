package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets, Users}
import au.id.sommerville.zendesk.search.console.Response.{NotFoundSearchResponse, UnknownFieldSearchResponse}
import org.scalatest.prop.TableDrivenPropertyChecks

/**
 *
 */
class ErrorSearchResponseSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  val notFound = Table(
    ("nf", "out"),
    (NotFoundSearchResponse(Organizations, "_id", Some("124412")), "No results found for Organizations with _id = '124412'"),
    (NotFoundSearchResponse(Users, "_id", Some("124412")), "No results found for Users with _id = '124412'"),
    (NotFoundSearchResponse(Tickets, "_id", Some("124412")), "No results found for Tickets with _id = '124412'"),
    (NotFoundSearchResponse(Tickets, "_id", None), "No results found for Tickets with _id not set")
  )

  "NotFound" should "output message describing entity, search field and value" in {

    forAll(notFound) { (nf: NotFoundSearchResponse, out: String) =>
      nf.out should equal(Seq(out))
    }
  }

  val unknownField = Table(
    ("uf", "out"),
    (UnknownFieldSearchResponse(Organizations, "bad"), "Organizations does not have a searchable field: bad"),
    (UnknownFieldSearchResponse(Users, "bad"), "Users does not have a searchable field: bad"),
    (UnknownFieldSearchResponse(Tickets, "bad"), "Tickets does not have a searchable field: bad")
  )

  "UnknownField" should "output message describing entity and bad field" in {

    forAll(unknownField) { (nf: UnknownFieldSearchResponse, out: String) =>
      nf.out should equal(Seq(out))
    }
  }
}
