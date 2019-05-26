package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*


abstract class ClientModel<TCommandRequestMeta : CommandRequestMeta, TCommandResponseMeta : CommandResponseMeta>(
	val name: String,
	commandDefinitions: Set<TypedCommandMeta<*, *>>,
	val commandRequestMetaSerializer: KSerializer<TCommandRequestMeta>,
	val commandResponseMetaSerializer: KSerializer<TCommandResponseMeta>,
	entityTypes: Set<EntityType<*, *>>
) {

	val commandDefinitions: Set<TypedCommandDefinition<*, *>> = commandDefinitions.mapTo(hashSetOf()) { it.definition }
	val entityTypes = entityTypes.toSet()

	val serializationContext = serializersModuleOf(mapOf(
		CommandRequest::class to ConcreteCommandRequestSerializer(commandDefinitions = this.commandDefinitions, metaSerializer = commandRequestMetaSerializer),
		EntityId::class to ConcreteEntityIdSerializer(types = entityTypes)
	))

	val json = Json(
		configuration = JsonConfiguration.Stable,
		context = serializationContext
	)

	val prettyJson = Json(
		configuration = JsonConfiguration.Stable.copy(prettyPrint = true, indent = "\t"),
		context = serializationContext
	)


	init {
		require(name.matches("^[a-zA-Z0-9_.-]+$".toRegex())) { "name must not be empty and contain only letters, digits, '_', '-' and '.'" }

		this.commandDefinitions.groupBy { it.name }
			.forEach { (name, definitions) ->
				check(definitions.size == 1) { "multiple commands have the same name: $name" }
			}

		this.entityTypes.groupBy { it.namespace }
			.forEach { (namespace, types) ->
				check(types.size == 1) { "multiple entity types have the same namespace: $namespace" }
			}

		this.entityTypes.groupBy { it.idClass }
			.forEach { (idClass, types) ->
				check(types.size == 1) { "multiple entity types have the same ID class: $idClass" }
			}
	}


	protected open fun createDefaultResponseMeta(): TCommandResponseMeta? = null
	abstract fun createRequestMetaForCommand(command: Command): TCommandRequestMeta


	companion object
}
