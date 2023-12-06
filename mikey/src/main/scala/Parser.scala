import cats.data.{State, EitherT}

// a lil parser combinator library, now using EitherT and State monads
// to clean up all the state management.
object P {
  case class ParserState(s: String, offset: Int) {
    def add(n: Int): ParserState = {
      ParserState(s, offset + n)
    }
  }

  type PState[A] = State[ParserState, A]

  type Parser[A] = EitherT[PState, String, A]

  def pure[A](a: A): Parser[A] = {
    EitherT(State(s => (s, Right(a))))
  }

  def string(str: String): Parser[String] = {
    EitherT(State(s => {
      if (s.s.slice(s.offset, s.offset + str.length()) == str) {
        (s.add(str.length()), Right(str))
      } else {
        (s, Left(s"expected $str"))
      }
    }))
  }

  def char(c: Char): Parser[Char] =
    for {
      s <- string(c.toString())
    } yield s.charAt(0)

  def ignoreFirst[A, B](fst: Parser[A], snd: Parser[B]): Parser[B] =
    for {
      _ <- fst
      b <- snd
    } yield b

  def maybe[A](p: Parser[A]): Parser[Option[A]] = {
    p.map(Some(_)).orElse(pure(None))
  }

  def many[A](p: Parser[A]): Parser[List[A]] = {
    def tryNext(l: List[A], p: Parser[A]): Parser[List[A]] =
      for {
        next <- maybe(p)
        ls <- next match {
          case Some(a) => tryNext(l.appended(a), p)
          case None    => pure(l)
        }
      } yield ls

    tryNext(List.empty, p)
  }

  def sepBy[A, B](sep: Parser[A], item: Parser[B]): Parser[List[B]] = {
    for {
      a <- maybe(item).flatMap(first => {
        first match {
          case None => pure(List(): List[B])
          case Some(a) =>
            for {
              ls <- many(ignoreFirst(sep, item))
            } yield a :: ls
        }
      })
    } yield a
  }

  def takeWhile(pred: (Char => Boolean)): Parser[String] = {
    EitherT(State(s => {
      val taken = s.s.slice(s.offset, s.s.length()).takeWhile(pred)
      (s.add(taken.length()), Right(taken))
    }))
  }

  val line: Parser[String] = for {
    str <- takeWhile(c => c != '\n')
    _ <- char('\n')
  } yield str

  val ws: Parser[Unit] = for {
    _ <- takeWhile(_.isWhitespace)
  } yield ()

  def err[A](errorString: String): Parser[A] = EitherT(State(s => {
    (s, Left(errorString))
  }))

  val number: Parser[Int] = {
    val signAndStr = for {
      sign <- maybe(char('-')).map(_.map(_ => -1).getOrElse(1))
      str <- takeWhile(_.isDigit)
    } yield (sign, str)
    signAndStr.flatMap({
      case (sign, str) => {
        if (str.length() > 0) {
          pure(sign * str.toInt)
        } else {
          err("expected number")
        }
      }
    })
  }

  val bigNumber: Parser[BigInt] = {
    val signAndStr = for {
      sign <- maybe(char('-')).map(_.map(_ => -1).getOrElse(1))
      str <- takeWhile(_.isDigit)
    } yield (sign, str)
    signAndStr.flatMap({
      case (sign, str) => {
        if (str.length() > 0) {
          pure(sign * BigInt(str))
        } else {
          err("expected number")
        }
      }
    })
  }

  def runParser[A](s: String, parser: Parser[A]): Either[String, A] = {
    parser.value.runA(ParserState(s, 0)).value
  }
}
