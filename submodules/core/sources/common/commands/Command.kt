package team.genki.chotto.core

import team.genki.chotto.core.Command.*
import kotlin.reflect.KClass


interface Command {

	val descriptor: Descriptor


	interface Descriptor {

		val commandClass: KClass<out Command>
		val name: String
		val resultClass: KClass<*>
	}


	abstract class Typed<TCommand : Typed<TCommand, TResult>, TResult : Any>(
		override val descriptor: Descriptor<TCommand, TResult>
	) : Command {

		interface Descriptor<TCommand : Typed<TCommand, TResult>, TResult : Any> : Command.Descriptor {

			override val commandClass: KClass<TCommand>
			override val name: String
			override val resultClass: KClass<TResult>
		}
	}
}


inline fun <reified TCommand : Typed<TCommand, TResult>, reified TResult : Any> commandDescriptor(name: String) =
	object : Typed.Descriptor<TCommand, TResult> {

		override val commandClass = TCommand::class
		override val name = name
		override val resultClass = TResult::class
	}
