package au.id.sommerville.zendesk.search

import java.io.{ByteArrayOutputStream, StringReader}

import au.id.sommerville.zendesk.search.cli.Config
import org.scalatest.{FlatSpec, Matchers}

/**
 *
 */
class ZendeskSearchIT extends FlatSpec with Matchers {

  "App" should "read organisation data and retrieve by id" in {
    val input = new StringReader(s"s o _id 101\nq\n")
    val outCapture = new ByteArrayOutputStream
    val errCapture = new ByteArrayOutputStream

    Console.withIn(input) {
      Console.withOut(outCapture) {
        Console.withErr(errCapture) {
          ZendeskSearch.run(Config())
        }
      }
    }
    outCapture.toString.split("\r\n") should contain inOrder(
      "Welcome to Zendesk Search",
      ">---------------------------------------------------------------------------",
      "_id:             101",
      "---------------------------------------------------------------------------",
      ">"
    )


  }

}
