package team.genki.chotto.client.model

import team.genki.chotto.client.model.Command.*
import kotlin.reflect.KClass


abstract class Command<out Result : Any>(
	val descriptor: Descriptor<out Command<Result>, out Result>
) {

	interface Descriptor<Command : team.genki.chotto.client.model.Command<Result>, Result : Any> {

		val commandClass: KClass<Command>
		val name: String
		val resultClass: KClass<Result>
	}
}


inline fun <reified Command : team.genki.chotto.client.model.Command<Result>, reified Result : Any> commandDescriptor(name: String) =
	object : Descriptor<Command, Result> {

		override val commandClass = Command::class
		override val name = name
		override val resultClass = Result::class
	}
