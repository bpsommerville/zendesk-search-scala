package au.id.sommerville.zendesk.search.data

import java.nio.file.Paths

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class LoaderSpec extends UnitTestSpec {
  "loadOrganizations" should "load single organization from file" in {
    val inputPath = Paths.get(ClassLoader.getSystemResource("orgs/single.json").toURI)

    val orgs = Loader.loadOrganizations( inputPath )

    orgs should have size 1
    orgs(0) should have(
      Symbol("_id")(453)
    )
  }

  "loadOrganizations" should "load multiple organization from file" in {
    val inputPath = Paths.get(ClassLoader.getSystemResource("orgs/multiple.json").toURI)

    val orgs = Loader.loadOrganizations( inputPath )

    orgs should have size 5
  }
}
