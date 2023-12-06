import scala.collection.immutable.NumericRange

object Day5 extends Solver {
  type Range = NumericRange.Exclusive[BigInt]

  case class AlmanacRange(
      inputRange: Range,
      offset: BigInt
  ) {
    val outputRange: Range = {
      Range.BigInt(inputRange.start + offset, inputRange.end + offset, 1)
    }
  }

  case class AlmanacMap(ranges: List[AlmanacRange]) {
    def process(input: BigInt): BigInt = ranges
      .collectFirst {
        case r if r.inputRange.contains(input) => input + r.offset
      }
      .getOrElse(input)

    // Apply an input range to the mapping ranges; the output is a
    // list of possible output ranges given the input range.
    def processRange(
        input: NumericRange.Exclusive[BigInt]
    ): List[NumericRange.Exclusive[BigInt]] = {
      ranges.foldLeft((List(input), List(): List[Range])) {
        case ((unmodified, modified), mapping) => {
          val range = mapping.inputRange
          unmodified.foldLeft((List(): List[Range], List(): List[Range])) {
            // case 1: map completely contains range
            case ((us, ms), u)
                if range.contains(u.start) && range.contains(u.last) => {
              (
                us,
                Range.BigInt(
                  u.start + mapping.offset,
                  u.end + mapping.offset,
                  1
                ) :: ms
              )
            }
            // case 2: map bisects start of range
            case ((us, ms), u) if range.contains(u.start) => {
              assert(range.end < u.end)
              assert(range.start <= u.start)
              assert(u.start < range.end)
              (
                Range.BigInt(range.end, u.end, 1) :: us,
                Range.BigInt(
                  u.start + mapping.offset,
                  range.end + mapping.offset,
                  1
                ) :: ms
              )
            }
            // case 3: map bisects end of range
            case ((us, ms), u) if range.contains(u.last) => {
              assert(range.end >= u.end)
              assert(range.start > u.start)
              assert(u.last >= range.start)
              (
                Range.BigInt(u.start, range.start, 1) :: us,
                Range.BigInt(
                  range.start + mapping.offset,
                  u.end + mapping.offset,
                  1
                ) :: ms
              )
            }
            // case 4: map is inside of range
            case ((us, ms), u)
                if u.start < range.start && u.end > range.end => {
              (
                Range.BigInt(u.start, range.start, 1) ::
                  Range.BigInt(range.end, u.end, 1) ::
                  us,
                Range.BigInt(
                  range.start + mapping.offset,
                  range.end + mapping.offset,
                  1
                ) :: ms
              )
            }
            // case 5: map is outside of range
            case ((us, ms), u) => (u :: us, ms)
          } match { case (us, ms) => (us, modified.concat(ms)) }
        }
      } match { case (a, b) => a.concat(b) }
    }

    def processRanges(input: List[Range]): List[Range] = {
      input.flatMap(r => processRange(r))
    }
  }

  case class Almanac(maps: Seq[AlmanacMap]) {
    def process(input: BigInt): BigInt = maps.foldLeft(input) { case (i, m) =>
      m.process(i)
    }

    def processRanges(input: List[Range]): List[Range] = maps.foldLeft(input) {
      case (is, m) =>
        m.processRanges(is)
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

  def seeds2: Parser[List[Range]] = {
    val pair: Parser[Range] = for {
      start <- bigNumber
      _ <- ws
      length <- bigNumber
    } yield Range.BigInt(start, start + length, 1)

    for {
      _ <- string("seeds: ")
      pairs <- sepBy(ws, pair)
    } yield pairs
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
        val ranges = a.processRanges(s)
        ranges.minBy(_.start).start.toString()
      }
    }
  }
}
