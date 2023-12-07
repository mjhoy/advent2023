import org.scalatest.funsuite.AnyFunSuite
import scala.io.Source
import java.nio.file.Paths
import java.nio.file.Files
import scala.jdk.CollectionConverters._
import scala.collection.mutable.Buffer

class DaysSpec extends AnyFunSuite {
  val resources = getClass().getResource("day_specs").toURI()
  for (file <- Files.list(Paths.get(resources)).iterator.asScala) {
    val parts = file.getFileName().toString().split("[.]")(0).split("[_]")
    val day = parts(0)
    val half = parts(1)

    test(s"Day $day part $half") {
      println(s"Day $day part $half")
      val solver = Main.reflect(s"Day$day")
      val lines = Source.fromFile(file.toString()).getLines()
      var inputFinished = false
      val input: Buffer[String] = Buffer()
      var expected = ""
      for (line <- lines) {
        if (line == "- input:") {
          // no-op.
        } else if (line == "- expected:") {
          inputFinished = true
        } else {
          if (inputFinished) {
            expected = line
          } else {
            input += line
          }
        }
      }
      if (half == "1") {
        val answer = solver.solve_1(input.iterator)
        assert(answer == expected)
      } else {
        val answer = solver.solve_2(input.iterator)
        assert(answer == expected)
      }
    }
  }
}
