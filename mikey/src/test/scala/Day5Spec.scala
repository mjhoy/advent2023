import org.scalatest.funsuite.AnyFunSuite

class Day5Spec extends AnyFunSuite {
  import Day5._
  test("AlmanacMap#processRange with 1 range") {
    val r = AlmanacRange(Range.BigInt(5, 10, 1), 5)
    val m = AlmanacMap(List(r))

    assert(
      m.processRange(Range.BigInt(3, 6, 1)) ==
        List(Range.BigInt(3, 5, 1), Range.BigInt(10, 11, 1))
    )
  }

  test("AlmanacMap#processRange with multiple ranges") {
    val r = List(
      AlmanacRange(Range.BigInt(5, 10, 1), 5),
      AlmanacRange(Range.BigInt(10, 15, 1), -20),
      AlmanacRange(Range.BigInt(15, 20, 1), 1)
    )
    val m = AlmanacMap(r)

    assert(
      m.processRange(Range.BigInt(3, 6, 1)) ==
        List(Range.BigInt(3, 5, 1), Range.BigInt(10, 11, 1))
    )
    assert(
      m.processRange(Range.BigInt(6, 12, 1)) ==
        List(Range.BigInt(11, 15, 1), Range.BigInt(-10, -8, 1))
    )
    assert(
      m.processRange(Range.BigInt(0, 25, 1)).sortBy(_.start) ==
        List(
          Range.BigInt(-10, -5, 1),
          Range.BigInt(0, 5, 1),
          Range.BigInt(10, 15, 1),
          Range.BigInt(16, 21, 1),
          Range.BigInt(20, 25, 1)
        )
    )
  }
}
