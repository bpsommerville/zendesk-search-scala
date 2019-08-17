package au.id.sommerville.zendesk.search.data

import au.id.sommerville.zendesk.search.UnitTestSpec

/**
 *
 */
class SearchableCollectionFieldSpec extends UnitTestSpec {
  case class Dummy(
    _id: Int,
    tags: Set[String] = Set(),
    optTags: Option[Set[String]] = None

  ) extends Searchable {
    override type IdType = Int
  }

  val tagField = SearchableStringCollectionField[Dummy]("tags", v => Some(v.tags))
  val optTagField = SearchableStringCollectionField[Dummy]("optTags", _.optTags)

  "search terms" should "be list of elements when collection has values" in {
    val test = Dummy(1, tags = Set("one", "two"))
    tagField.toSearchTerms(test) should contain allOf(Some("one"), Some("two"))
  }

  "search terms" should "be list of None when collection is empty" in {
    val test = Dummy(1, tags = Set())
    tagField.toSearchTerms(test) should equal(Seq(None))
  }

  "search terms" should "be list of elements when optional collection has values" in {
    val test = Dummy(1, optTags = Some(Set("one", "two")))
    optTagField.toSearchTerms(test) should contain allOf(Some("one"), Some("two"))
  }

  "search terms" should "be list of None when optional collection is empty" in {
    val test = Dummy(1, optTags = Some(Set()))
    optTagField.toSearchTerms(test) should equal(Seq(None))
  }

  "search terms" should "be list of None when optional collection is None" in {
    val test = Dummy(1, optTags =None)
    optTagField.toSearchTerms(test) should equal(Seq(None))
  }

}
