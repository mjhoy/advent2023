import scala.collection.immutable.NumericRange

object Day5 extends Solver {
  case class AlmanacRange(
      inputRange: NumericRange.Exclusive[BigInt],
      offset: BigInt
  ) {
    val outputRange: NumericRange.Exclusive[BigInt] = {
      Range.BigInt(inputRange.start + offset, inputRange.end + offset, 1)
    }
  }

  case class AlmanacMap(ranges: List[AlmanacRange]) {
    def process(input: BigInt): BigInt = ranges
      .collectFirst {
        case r if r.inputRange.contains(input) => input + r.offset
      }
      .getOrElse(input)
  }

  case class Almanac(maps: Seq[AlmanacMap]) {
    def process(input: BigInt): BigInt = maps.foldLeft(input) { case (i, m) =>
      m.process(i)
    }
  }

  // Parsers
  import P._

  def almanacRange: Parser[AlmanacRange] = for {
    to <- bigNumber
    _ <- ws
    from <- bigNumber
    _ <- ws
    length <- bigNumber
  } yield AlmanacRange(Range.BigInt(from, from + length, 1), to - from)

  def almanacMap: Parser[AlmanacMap] = for {
    _ <- line // skip first line
    ranges <- sepBy(char('\n'), almanacRange)
  } yield AlmanacMap(ranges)

  def almanac: Parser[Almanac] = for {
    maps <- sepBy(line, almanacMap)
  } yield Almanac(maps)

  def seeds: Parser[List[BigInt]] = for {
    _ <- string("seeds: ")
    nums <- sepBy(ws, bigNumber)
  } yield nums

  override def solve_1(input: Iterator[String]): String = {
    val i = input.mkString("\n")
    val parsed = runParser(
      i,
      for {
        s <- seeds
        _ <- line
        a <- almanac
      } yield (s, a)
    )
    parsed match {
      case Left(err) => err
      case Right((s, a)) => {
        s.map(a.process(_)).min.toString()
      }
    }
  }

  // naive approach! does not work.

  def seeds2: Parser[List[BigInt]] = {
    val pair: Parser[List[BigInt]] = for {
      start <- bigNumber
      _ <- ws
      length <- bigNumber
    } yield Range.BigInt(start, start + length, 1).toList

    for {
      _ <- string("seeds: ")
      pairs <- sepBy(ws, pair)
    } yield pairs.flatten
  }

  override def solve_2(input: Iterator[String]): String = {
    val i = input.mkString("\n")
    val parsed = runParser(
      i,
      for {
        s <- seeds2
        _ <- line
        a <- almanac
      } yield (s, a)
    )
    parsed match {
      case Left(err) => err
      case Right((s, a)) => {
        s.map(a.process(_)).min.toString()
      }
    }
  }
}
