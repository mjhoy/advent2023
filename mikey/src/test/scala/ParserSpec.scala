import org.scalatest.funsuite.AnyFunSuite

class ParserSpec extends AnyFunSuite {
  test("string parser") {
    val parser = P.string("foop")

    val a = P.runParser("foop bloop", parser)
    assert(a == Right("foop"))

    val b = P.runParser("bloop foop", parser)
    assert(b == Left("expected foop"))
  }

  test("maybe parser") {
    val parser = P.maybe(P.string("foop"))

    val a = P.runParser("foop bloop", parser)
    assert(a == Right(Some("foop")))

    val b = P.runParser("bloop foop", parser)
    assert(b == Right(None))
  }

  test("many parser") {
    val parser = P.many(P.string("foop"))

    val a = P.runParser("foopfoopfoop foop", parser)
    assert(a == Right(List("foop", "foop", "foop")))

    val b = P.runParser("zoop", parser)
    assert(b == Right(List()))
  }

  test("sepBy parser") {
    val parser = P.sepBy(P.char(','), P.string("foop"))

    val a = P.runParser("foop,foop,foop, foop", parser)
    assert(a == Right(List("foop", "foop", "foop")))

    val b = P.runParser("foop, foop, zoop", parser)
    assert(b == Right(List("foop")))

    val c = P.runParser("zoop, foop, foop, zoop", parser)
    assert(c == Right(List()))

    val parser2 = P.sepBy(P.char(','), P.number)

    val d = P.runParser("1,2,3,4", parser2)
    assert(d == Right(List(1, 2, 3, 4)))
  }

  test("takeWhile parser") {
    val parser = P.takeWhile(_.isDigit)

    val a = P.runParser("1234 234", parser)
    assert(a == Right("1234"))

    val b = P.runParser(
      "foop    zoo",
      for {
        f <- P.string("foop")
        ws <- P.takeWhile(_ == ' ')
        z <- P.string("zoo")
      } yield z
    )
    assert(b == Right("zoo"))
  }

  test("number parser") {
    val a = P.runParser("1234", P.number)
    assert(a == Right(1234))

    val b = P.runParser("-1234", P.number)
    assert(b == Right(-1234))
  }
}
