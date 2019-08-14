package au.id.sommerville.zendesk.search.repo

import java.time.{OffsetDateTime, ZoneOffset}

import au.id.sommerville.zendesk.search.console.Entity.Organizations
import au.id.sommerville.zendesk.search.{NoResultsError, UnitTestSpec, UnknownFieldError}
import au.id.sommerville.zendesk.search.data.Organization
import faker._

import scala.util.Random


/**
 *
 */
class MemoryRepositorySpec extends UnitTestSpec {

  def genSeq( max: Int, gen: () => String ) = {
    for (i <- 0 to Random.nextInt(max)) yield gen()
  }

  def randIntBetween(low: Int, high: Int) : Int = {
    Random.nextInt(high - low + 1) + low
  }

  def generateOrgs(count: Int) = {
    (1 to count).map( id =>
      Organization(
        _id = id,
        url = s"http://initech.zendesk.com/api/v2/organizations/id.json",
        externalId = java.util.UUID.randomUUID.toString,
        name = Name.name,
        domainNames = genSeq(4, () => Internet.domain_name),
        createdAt = OffsetDateTime.of(
          randIntBetween(1983, 2019),
          randIntBetween(1, 12),
          randIntBetween(1, 28),
          randIntBetween(0, 23),
          randIntBetween(0, 59),
          randIntBetween(0, 59),
          0,
          ZoneOffset.ofHours(randIntBetween(-11, 11))
        ),
        details = Lorem.paragraph(),
        sharedTickets = Random.nextBoolean(),
        tags = Lorem.words(Random.nextInt(4)).toSet
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
    results.right.value should have length(1)
    results.right.value(0)._id should equal(4)
  }

  "search by id" should "return NoResultsError if nothing matches" in {
    val repo = new MemoryRepository[Organization]
    repo.add( generateOrgs(20))

    val results = repo.search("_id", "412431")
    results.left.value should equal(NoResultsError)
  }

  "search by field" should "return matching organization" in {
    val repo = new MemoryRepository[Organization]
    val orgs = generateOrgs(20)
    repo.add(orgs)

    Organization.fields.foreach( f => {
      val searchOrg = orgs(randIntBetween(0, 19))
      f.toSearchTerms(searchOrg).map(
        repo.search(f.name, _ ).right.value should contain(searchOrg)
      )
    })

  }

  "search by field" should "return UnknownFieldError when field not found" in {
    val repo = new MemoryRepository[Organization]
    val orgs = generateOrgs(20)
    repo.add(orgs)

    repo.search("bad","anything").left.value should equal(UnknownFieldError("bad"))

  }
}
