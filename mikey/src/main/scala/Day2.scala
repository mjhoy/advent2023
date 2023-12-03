import cats.implicits._

object Day2 extends Solver {
  // P: a lil parser combinator library
  object P {
    case class Err(str: String)
    type Parser[A] = String => Either[Err, (A, String)]

    def ignoreFirst[A, B](fst: Parser[A], snd: Parser[B]): Parser[B] = {
      input =>
        for {
          a <- fst(input)
          b <- snd(a._2)
        } yield b
    }

    def many[A](p: Parser[A]): Parser[List[A]] = { input =>
      p(input) match {
        case Left(_) => Right((List.empty, input))
        case Right((a, rest)) => {
          for {
            as <- many(p)(rest)
          } yield (a :: as._1, as._2)
        }
      }
    }

    def sepBy[A, B](sep: Parser[A], p: Parser[B]): Parser[List[B]] = { input =>
      p(input) match {
        case Left(_) => Right((List.empty, input))
        case Right((a, rest)) => {
          for {
            as <- many(ignoreFirst(sep, p))(rest)
          } yield (a :: as._1, as._2)
        }
      }
    }

    val takeWhile: (Char => Boolean) => Parser[String] = { predicate =>
      { input =>
        val matched = input.takeWhile(predicate)
        Right((matched, input.slice(matched.length(), input.length())))
      }
    }

    val number: Parser[Int] = { input =>
      val n = input.takeWhile(c => c.isDigit)
      if (n.length() > 0) {
        Right((n.toInt, input.slice(n.length(), input.length())))
      } else {
        Left(Err("expected number"))
      }
    }

    val str: String => Parser[String] = { str =>
      { input =>
        val stripped = input.stripPrefix(str)
        if (input.length - stripped.length() == str.length()) {
          Right((str, stripped))
        } else {
          Left(Err(s"expected $str"))
        }
      }
    }
  }

  // question-specific parsers.
  object BagParsers {
    import P._

    val game: Parser[Int] = ignoreFirst(str("Game "), number)
    val cubeReach: Parser[(String, Int)] = { input =>
      for {
        n <- number(input)
        s <- str(" ")(n._2)
        key <- takeWhile(_.isLetter)(s._2)
      } yield ((key._1, n._1), key._2)
    }
    val bagReach: Parser[Map[String, Int]] = { input =>
      for {
        cubeReaches <- sepBy(str(", "), cubeReach)(input)
      } yield (cubeReaches._1.toMap, cubeReaches._2)
    }
    val row: Parser[(Int, List[Map[String, Int]])] = { input =>
      for {
        g <- game(input)
        s <- str(": ")(g._2)
        bagReaches <- sepBy(str("; "), bagReach)(s._2)
      } yield ((g._1, bagReaches._1), bagReaches._2)
    }
  }

  override def solve_1(input: Iterator[String]): String = {
    import BagParsers._

    input
      .map((line) => {
        row(line).map({
          case ((gameId, values), _) => {
            if (
              !values.exists(m => {
                m.getOrElse("red", 0) > 12 ||
                m.getOrElse("green", 0) > 13 ||
                m.getOrElse("blue", 0) > 14
              })
            ) {
              gameId
            } else {
              0
            }
          }
        })
      })
      .toList
      .sequence match {
      case Right(ls)      => ls.reduceLeft(_ + _).toString()
      case Left(P.Err(s)) => s"ERROR: $s"
    }
  }

  override def solve_2(input: Iterator[String]): String = {
    import BagParsers._
    input
      .map((line) => {
        row(line).map({
          case ((gameId, values), _) => {
            val nums = List(
              "red",
              "green",
              "blue"
            ).map(key => {
              values.map(_.getOrElse(key, 0)).max
            })
            nums.product
          }
        })
      })
      .toList
      .sequence match {
      case Right(ls) => {
        ls.reduceLeft(_ + _).toString()
      }
      case Left(P.Err(s)) => s"ERROR: $s"
    }
  }
}
