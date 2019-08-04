package au.id.sommerville.zendesk.search.data

import java.nio.file.Path

/**
 *
 */
object Loader {

  def loadOrganizations(inputPath: Path): Seq[Organization] = {
     upickle.default.read[Seq[Organization]](inputPath)
  }

}
