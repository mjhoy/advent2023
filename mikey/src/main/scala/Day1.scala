import scala.io.Source
import cats.implicits._

object Day1 extends Solver {
  override def solve_1(iter: Iterator[String]): String = {
    iter
      .map(line => {
        (line.find(_.isDigit), line.findLast(_.isDigit)).bisequence match {
          case Some((fst, snd)) => {
            (fst.asDigit * 10) + snd.asDigit
          }
          case None => 0
        }

      })
      .reduceLeft(_ + _)
      .toString()
  }

  override def solve_2(iter: Iterator[String]): String = {
    val numberAtIndex: (String, Boolean) => PartialFunction[Int, Int] =
      (str: String, backwards: Boolean) => {
        def compare_at(start: Int, check: String): Boolean = {
          val end = start + check.length()
          if (str.length() < end) {
            false
          } else {
            val checkResolved = if (backwards) check.reverse else check
            str.substring(start, end) == checkResolved
          }
        }
        {
          case i if str.charAt(i).isDigit  => str.charAt(i).asDigit
          case i if compare_at(i, "one")   => 1
          case i if compare_at(i, "two")   => 2
          case i if compare_at(i, "three") => 3
          case i if compare_at(i, "four")  => 4
          case i if compare_at(i, "five")  => 5
          case i if compare_at(i, "six")   => 6
          case i if compare_at(i, "seven") => 7
          case i if compare_at(i, "eight") => 8
          case i if compare_at(i, "nine")  => 9
        }
      }

    iter
      .map(line => {
        (
          line.indices.collectFirst(numberAtIndex(line, false)),
          line.indices.collectFirst(numberAtIndex(line.reverse, true))
        ).bisequence match {
          case Some((fst, snd)) => {
            (fst * 10) + snd
          }
          case None => 0
        }

      })
      .reduceLeft(_ + _)
      .toString()
  }
}
