import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.math.log10

object Day3 extends Solver {
  // a map that holds a growable buffer, with some convenience functions.
  case class BufMap[A](inner: Map[Int, Buffer[A]]) {
    def insert(key: Int, value: A): Unit = {
      inner.get(key) match {
        case None      => inner += (key -> Buffer(value))
        case Some(buf) => buf += value
      }
    }

    def get(key: Int): Buffer[A] = {
      inner.get(key) match {
        case None      => Buffer()
        case Some(buf) => buf
      }
    }
  }

  object BufMap {
    def apply[A](): BufMap[A] = BufMap(Map())
  }

  override def solve_1(input: Iterator[String]): String = {
    val nums: BufMap[(Int, Int)] = BufMap()
    val symbols: BufMap[Int] = BufMap()

    for ((line, row) <- input.zipWithIndex) {
      var idx = 0
      while (idx < line.length()) {
        val digits =
          line.substring(idx, line.length()).takeWhile(c => c.isDigit)
        if (digits.length() > 0) {
          nums.insert(row, (digits.toInt, idx))
          idx += digits.length()
        } else if (line.charAt(idx) == '.') {
          idx += 1
        } else {
          symbols.insert(row, idx)
          idx += 1
        }
      }
    }

    nums.inner
      .map({
        case (row, rowNums) => {
          rowNums
            .map({
              case (value, col) => {
                val length = log10(value).toInt + 1
                val touchingSymbol = Seq(row - 1, row, row + 1).exists(r => {
                  symbols
                    .get(r)
                    .exists(symbolCol =>
                      (symbolCol >= col - 1) && (symbolCol <= col + length)
                    )
                })
                if (touchingSymbol) {
                  value
                } else {
                  0
                }
              }
            })
            .reduceLeft(_ + _)
        }
      })
      .reduceLeft(_ + _)
      .toString()
  }
}
