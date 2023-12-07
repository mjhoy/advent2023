object Day7 extends Solver {
  type Cards = (Int, Int, Int, Int, Int)

  case class Hand(cards: Cards, bid: Int) {
    def typ: Int = {
      val groups =
        List(cards._1, cards._2, cards._3, cards._4, cards._5)
          .groupBy(x => x)
          .view
          .mapValues(v => v.length)

      val wildcards = groups.get(1).getOrElse(0)
      val otherGroups = groups.filterKeys(k => k != 1)

      val resolved =
        if (otherGroups.nonEmpty) { // add wildcards to the next best group
          otherGroups.toList.sortBy(_._2).reverse.map { case (_, len) =>
            len
          } match {
            case head :: rest => head + wildcards :: rest
            case x            => x
          }
        } else {
          groups.toList.map { case (_, vs) => vs }.sorted.reverse
        }

      resolved match {
        case 5 :: Nil                     => 6
        case 4 :: 1 :: Nil                => 5
        case 3 :: 2 :: Nil                => 4
        case 3 :: 1 :: 1 :: Nil           => 3
        case 2 :: 2 :: 1 :: Nil           => 2
        case 2 :: 1 :: 1 :: 1 :: Nil      => 1
        case 1 :: 1 :: 1 :: 1 :: 1 :: Nil => 0
        case _                            => -1
      }
    }

    val comparable: (Int, Cards) = (typ, cards)
  }

  object Hand {
    def newFromChars(
        string: String,
        bid: Int,
        jokersAreWildcard: Boolean
    ): Option[Hand] = {
      val values = string
        .map(_ match {
          case 'A' => 14
          case 'K' => 13
          case 'Q' => 12
          case 'J' => if (jokersAreWildcard) 1 else 11
          case 'T' => 10
          case x   => x.asDigit
        })
        .toList

      values match {
        case a :: b :: c :: d :: e :: Nil => Some(Hand((a, b, c, d, e), bid))
        case _                            => None
      }
    }
  }

  // Parser
  import P._

  def hand(wildcard: Boolean): Parser[Hand] = for {
    cards <- takeWhile(c => c != ' ')
    _ <- char(' ')
    bid <- number
  } yield Hand.newFromChars(cards, bid, wildcard).get

  def hands(wildcard: Boolean): Parser[List[Hand]] = for {
    hs <- sepBy(char('\n'), hand(wildcard))
  } yield hs

  def runHands(hands: List[Hand]): Int = {
    hands
      .sortBy(_.comparable)
      .zipWithIndex
      .map { case (h, i) => h.bid * (i + 1) }
      .sum
  }

  override def solve_1(input: Iterator[String]): String = {
    val i = input.mkString("\n")
    runParser(i, hands(false)) match {
      case Left(err)    => s"ERR: $err"
      case Right(hands) => { runHands(hands).toString() }
    }
  }

  override def solve_2(input: Iterator[String]): String = {
    val i = input.mkString("\n")
    runParser(i, hands(true)) match {
      case Left(err)    => s"ERR: $err"
      case Right(hands) => { runHands(hands).toString() }
    }
  }
}
