package io.circe.generic.extras.decoding

import io.circe.{ Decoder, HCursor }
import shapeless.{ ::, Generic, HNil, Lazy }

abstract class UnwrappedDecoder[A] extends Decoder[A]

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
object UnwrappedDecoder {
  implicit def decodeUnwrapped[A <: AnyVal, R](
      implicit
      gen: Lazy[Generic.Aux[A, R :: HNil]],
      decode: Decoder[R]
  ): UnwrappedDecoder[A] =
    (c: HCursor) ⇒
      decode(c) match {
        case Right(unwrapped) ⇒ Right(gen.value.from(unwrapped :: HNil))
        case l @ Left(_)      ⇒ l.asInstanceOf[Decoder.Result[A]]
    }
}
