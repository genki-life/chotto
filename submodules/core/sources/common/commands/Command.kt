package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.modules.*


interface Command : CommandMeta {

	companion object
}


interface CommandDefinition : CommandMeta {

	val name: String
	val resultSerializer: KSerializer<*>
	val serializer: KSerializer<out Command>
	val serialModule: SerialModule

	override val definition
		get() = this
}


interface CommandMeta {

	val definition: CommandDefinition
}


interface TypedCommand<TCommand : TypedCommand<TCommand, TResult>, TResult : Any> : Command, TypedCommandMeta<TCommand, TResult>


class TypedCommandDefinition<TCommand : TypedCommand<TCommand, TResult>, TResult : Any>(
	override val name: String,
	override val resultSerializer: KSerializer<TResult>,
	override val serializer: KSerializer<TCommand>,
	override val serialModule: SerialModule = EmptyModule
) : CommandDefinition, TypedCommandMeta<TCommand, TResult> {

	init {
		freeze()
	}


	override val definition
		get() = this


	override fun toString() =
		name
}


interface TypedCommandMeta<TCommand : TypedCommand<TCommand, TResult>, TResult : Any> : CommandMeta {

	override val definition: TypedCommandDefinition<TCommand, TResult>
}
