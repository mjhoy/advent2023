import scala.io.Source
import cats.implicits._

object Main {
  def main(args: Array[String]): Unit = {
    (args.lift(0), args.lift(1)) match {
      case (Some(path), Some("1")) => {
        val answer = Day1.process(Source.fromFile(path).getLines())
        println(answer)
      }
      case (Some(path), Some("2")) => {
        val answer = Day1.process2(Source.fromFile(path).getLines())
        println(answer)
      }
      case _ => System.err.println("usage: advent [file] [1|2]")
    }
  }
}

object Day1 {
  def process(iter: Iterator[String]): Int = {
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
  }

  def process2(iter: Iterator[String]): Int = {
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
  }
}
