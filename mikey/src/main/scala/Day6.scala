object Day6 extends Solver {
  import scala.math._

  case class Race(time: BigInt, distance: BigInt) {
    val bound: (Double, Double) = {
      // b = time, c = distance, x = seconds pressed
      // x(b - x) = c
      // bx - x^2 - c = 0
      // x^2 + (-b)x + c = 0
      // quadratic:
      // (b + sqrt(pow(b, 2) - 4 * c)) / 2
      // (b - sqrt(pow(b, 2) - 4 * c)) / 2

      val b = time
      val c = distance
      val sqrtVal = sqrt((b.pow(2) - (4 * c)).toDouble)
      (
        ((b.toDouble + sqrtVal) / 2), // upper bound
        ((b.toDouble - sqrtVal) / 2), // lower bound
      )
    }

    def press(x: BigInt): BigInt = {
      x * (time - x)
    }

    val waysToWin: BigInt = {
      val (upper, lower) = bound
      val upperInt = upper.toInt
      val lowerInt = lower.toInt

      val upperWays =
        if (press(upperInt) > distance) upper.toInt else upper.toInt - 1
      val lowerWays =
        if (press(lowerInt) > distance) lower.toInt else lower.toInt + 1
      (upperWays - lowerWays) + 1
    }
  }

  // parsers
  import P._

  val races: Parser[List[Race]] = for {
    _ <- string("Time:")
    _ <- ws
    times <- sepBy(ws, number)
    _ <- string("Distance:")
    _ <- ws
    distances <- sepBy(ws, number)
  } yield times.zip(distances).map { case (t, d) => Race(t, d) }

  override def solve_1(input: Iterator[String]): String = {
    runParser(input.mkString("\n"), races) match {
      case Right(races) => races.map(_.waysToWin).product.toString()
      case Left(err)    => println(err); err
    }
  }

  val numSplitUp: Parser[BigInt] = for {
    l <- takeWhile(c => c != '\n')
  } yield BigInt(l.split("\\s+").mkString(""))

  val races2: Parser[Race] = for {
    _ <- string("Time:")
    _ <- ws
    time <- numSplitUp
    _ <- char('\n')
    _ <- string("Distance:")
    _ <- ws
    distance <- numSplitUp
  } yield Race(time, distance)

  override def solve_2(input: Iterator[String]): String = {
    runParser(input.mkString("\n"), races2) match {
      case Right(race) => { race.waysToWin.toString() }
      case Left(err)   => println(err); err
    }
  }
}
