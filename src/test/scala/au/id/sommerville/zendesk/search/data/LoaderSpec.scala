package au.id.sommerville.zendesk.search.data

import java.nio.file.Paths

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class LoaderSpec extends UnitTestSpec {
  "loadOrganizations" should "load single organization from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("orgs/single.json").toURI)

    val orgs = Loader.loadOrganizations(inputPath)

    orgs should have size 1
    orgs(0) should have(
      Symbol("_id")(453)
    )
  }

  "loadOrganizations" should "load multiple organization from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("orgs/multiple.json").toURI)

    val orgs = Loader.loadOrganizations(inputPath)

    orgs should have size 5
  }

  "loadUsers" should "load single user from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("users/single.json").toURI)

    val users = Loader.loadUsers(inputPath)

    users should have size 1
    users(0) should have(
      Symbol("_id")(44)
    )
  }

  "loadUsers" should "load multiple users from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("users/multiple.json").toURI)

    val users = Loader.loadUsers(inputPath)

    users should have size 4
  }

  "loadUsers" should "load user with minimal set of properties from sample data" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("users/minimalPropertiesFromSampleData.json").toURI)

    val users = Loader.loadUsers(inputPath)

    users should have size 1
    users(0) should have(
      Symbol("_id")(44)
    )
  }

  "loadTickets" should "load single ticket from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("tickets/single.json").toURI)

    val tickets = Loader.loadTickets(inputPath)

    tickets should have size 1
    tickets(0) should have(
      Symbol("_id")("436bf9b0-1147-4c0a-8439-6f79833bff5b")
    )
  }

  "loadTickets" should "load multiple ticket from file" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("tickets/multiple.json").toURI)

    val tickets = Loader.loadTickets(inputPath)

    tickets should have size 14
  }


  "loadTickets" should "load ticket with minimal set of properties from sample data" in {
    val inputPath = Paths.get(this.getClass.getClassLoader.getResource("tickets/minimalPropertiesFromSampleData.json").toURI)

    val tickets = Loader.loadTickets(inputPath)

    tickets should have size 1
    tickets(0) should have(
      Symbol("_id")("436bf9b0-1147-4c0a-8439-6f79833bff5b")
    )
  }
}
