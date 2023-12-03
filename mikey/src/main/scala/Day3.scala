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

  def buildMaps(
      input: Iterator[String]
  ): (BufMap[(Int, Int)], BufMap[(Int, Char)]) = {
    val nums: BufMap[(Int, Int)] = BufMap()
    val symbols: BufMap[(Int, Char)] = BufMap()

    for ((line, row) <- input.zipWithIndex) {
      var idx = 0
      while (idx < line.length()) {
        val digits =
          line.substring(idx, line.length()).takeWhile(c => c.isDigit)
        if (digits.length() > 0) {
          nums.insert(row, (digits.toInt, idx))
          idx += digits.length()
        } else {
          val char = line.charAt(idx)
          if (char == '.') {
            idx += 1
          } else {
            symbols.insert(row, (idx, char))
            idx += 1
          }
        }
      }
    }

    (nums, symbols)
  }

  override def solve_1(input: Iterator[String]): String = {
    val (nums, symbols) = buildMaps(input)

    // Map over each number, and see if a symbol is touching it.
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
                    .exists(symbol =>
                      (symbol._1 >= col - 1) && (symbol._1 <= col + length)
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

  override def solve_2(input: Iterator[String]): String = {
    val (nums, symbols) = buildMaps(input)

    // Map over each symbol; for every *, see if two numbers are
    // touching it.
    symbols.inner
      .map({
        case (row, symbols) => {
          symbols
            .filter({ case (_, symbol) => { symbol == '*' } })
            .map({
              case (col, symbol) => {
                val touchingNums = Seq(row - 1, row, row + 1).flatMap(r => {
                  nums
                    .get(r)
                    .filter({
                      case (value, numCol) => {
                        val length = log10(value).toInt + 1
                        (col >= numCol - 1) && (col <= numCol + length)
                      }
                    })
                })
                if (touchingNums.length == 2) {
                  touchingNums.map({ case (value, numCol) => value }).product
                } else {
                  0
                }
              }
            })
            .foldLeft(0)(_ + _)
        }
      })
      .foldLeft(0)(_ + _)
      .toString()
  }
}
