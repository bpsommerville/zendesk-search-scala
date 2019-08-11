package au.id.sommerville.zendesk.search.repo

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.data.Organization


/**
 *
 */
class MemoryRepositorySpec extends UnitTestSpec {

  def generateOrgs(count: Int) = {
    (1 to count).map( id =>
      Organization(
        _id = id,
        url = "",
        externalId = "",
        name = "",
        domainNames = Seq(""),
        createdAt = OffsetDateTime.now(),
        details = "",
        sharedTickets = false,
        tags = Set()
      )
    )
  }

  "add" should "add data to repository and allow them to be retrieved by _id" in {
    val repo = new MemoryRepository[Organization]
    repo.add( generateOrgs(20))
    (1 to 20).foreach( id => {
      val found = repo.find(id)
      found.loneElement should have(
        Symbol("_id")(id)
      )
    })
  }

  "search by id" should "retrieve org with matching id" in {
    val repo = new MemoryRepository[Organization]
    repo.add( generateOrgs(20))

    val results = repo.search("_id", "4")
    results.value should have length(1)
    results.value(0)._id should equal(4)
  }

  "search by id" should "return Noes if nothing matches" in {
    val repo = new MemoryRepository[Organization]
    repo.add( generateOrgs(20))

    val results = repo.search("_id", "412431")
    results should equal(None)
  }
}
