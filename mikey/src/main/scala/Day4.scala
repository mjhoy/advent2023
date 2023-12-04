import Day2.P
import scala.math.pow
import scala.collection.mutable

object Day4 extends Solver {
  val nums: P.Parser[List[Int]] =
    P.sepBy(P.takeWhile(_.isWhitespace), P.number)(_)

  val card: P.Parser[(Set[Int], Set[Int])] = { input =>
    for {
      prelude <- P.takeWhile(c => c != ':')(input)
      w <- P.takeWhile(c => !c.isDigit)(prelude._2)
      winning <- nums(w._2)
      sep <- P.str(" | ")(winning._2)
      w <- P.takeWhile(c => !c.isDigit)(sep._2)
      guesses <- nums(w._2)
    } yield ((winning._1.toSet, guesses._1.toSet), guesses._2)
  }

  override def solve_1(input: Iterator[String]): String = {
    input
      .map(line => {
        card(line)
          .map({
            case ((winning, guesses), _) => {
              guesses.intersect(winning).size match {
                case 0 => 0
                case n => pow(2, n - 1).toInt
              }
            }
          })
          .getOrElse(0)
      })
      .sum
      .toString()
  }

  case class CardCounter(inner: mutable.Map[Int, Int]) {
    def get(key: Int): Int = inner.getOrElse(key, 0)
    def add(key: Int): Unit = {
      inner.updateWith(key)({
        case Some(v) => Some(v + 1)
        case None    => Some(1)
      })
    }
    def count: Int = inner.values.sum
  }

  object CardCounter {
    def apply(): CardCounter = CardCounter(mutable.Map())
  }

  override def solve_2(input: Iterator[String]): String = {
    val counter = CardCounter()
    for ((line, index) <- input.zipWithIndex) {
      counter.add(index)

      val matches = card(line)
        .map({ case ((winning, guesses), _) =>
          guesses.intersect(winning).size
        })
        .getOrElse(0)

      for (n <- 0 until counter.get(index); m <- 0 until matches) {
        counter.add(index + 1 + m)
      }
    }
    counter.count.toString()
  }
}
