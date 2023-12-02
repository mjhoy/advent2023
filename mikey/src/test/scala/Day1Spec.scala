import org.scalatest.funsuite.AnyFunSuite

class Day1Spec extends AnyFunSuite {
  test("works: part 1") {
    val input =
      Seq("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet").iterator

    val answer = Day1.solve_1(input)

    assert(answer == "142")
  }

  test("works: part 2") {
    val input =
      Seq(
        "two1nine",
        "eightwothree",
        "abcone2threexyz",
        "xtwone3four",
        "4nineeightseven2",
        "zoneight234",
        "7pqrstsixteen"
      ).iterator

    val answer = Day1.solve_2(input)

    assert(answer == "281")
  }
}
