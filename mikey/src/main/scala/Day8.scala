object Day8 extends Solver {
  sealed abstract class Move
  final case object L extends Move
  final case object R extends Move

  import P._

  val moves: Parser[List[Move]] = for {
    cs <- takeWhile(x => x == 'L' || x == 'R')
  } yield cs.map(c => if (c == 'L') L else R).toList

  val keyVal: Parser[(String, (String, String))] = for {
    key <- takeWhile(x => !x.isWhitespace)
    _ <- ws
    _ <- string("=")
    _ <- ws
    _ <- string("(")
    a <- takeWhile(x => x != ',')
    _ <- string(", ")
    b <- takeWhile(x => x != ')')
    _ <- string(")")
  } yield (key, (a, b))

  val map: Parser[Map[String, (String, String)]] = for {
    kvs <- sepBy(char('\n'), keyVal)
  } yield kvs.toMap

  override def solve_1(input: Iterator[String]): String = {
    val i = input.mkString("\n")
    runParser(
      i,
      for {
        ms <- moves
        _ <- string("\n\n")
        m <- map
      } yield (ms, m)
    ) match {
      case Left(err) => s"error parsing: $err"
      case Right((ms, moveMap)) => {
        val moveIter =
          LazyList
            .continually(ms.to(LazyList))
            .flatten
            .iterator

        var next = "AAA"
        var steps = 0

        // tried to figure out how to do this with a lazy fold right;
        // in the end mutation just seems simpler :(
        while (next != "ZZZ") {
          val move = moveIter.next()
          val (l, r) = moveMap.get(next).get
          next = move match {
            case L => l
            case R => r
          }
          steps += 1
        }

        steps.toString()
      }
    }
  }
}
