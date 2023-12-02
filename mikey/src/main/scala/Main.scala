import scala.io.Source
import scala.reflect.runtime.universe
import cats.implicits._
import java.io.FileNotFoundException

trait Solver {
  def solve_1(input: Iterator[String]): String = "[tbd]"
  def solve_2(input: Iterator[String]): String = "[tbd]"
}

object Main {
  def err(s: String) = System.err.println(s"error: $s")

  def reflect(name: String): Solver = {
    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
    val module = runtimeMirror.staticModule(name)
    runtimeMirror.reflectModule(module).instance.asInstanceOf[Solver]
  }

  def main(args: Array[String]): Unit = {
    args.toList match {
      case day :: half :: Nil => {
        val objName = s"Day$day"
        val inputFile = s"input/day_$day"
        try {
          val solver = reflect(objName)
          val lines = Source.fromFile(inputFile).getLines()
          val answer =
            if (half == "1") solver.solve_1(lines) else solver.solve_2(lines)
          println(answer)
        } catch {
          case e: ScalaReflectionException => err(s"no object $objName")
          case e: FileNotFoundException    => err(s"no input $inputFile")
        }
      }
      case _ => err("usage: advent [day] [1|2]")
    }
  }
}
