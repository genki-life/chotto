package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import kotlin.reflect.*


abstract class ClientModel<TCommandRequestMeta : CommandRequestMeta, TCommandResponseMeta : CommandResponseMeta>(
	val name: String,
	commandDescriptors: Set<Command.Typed.Descriptor<*, *>>,
	val commandRequestMetaClass: KClass<TCommandRequestMeta>,
	val commandResponseMetaClass: KClass<TCommandResponseMeta>,
	entityTypes: Set<EntityType<*, *>>
) {

	val commandDescriptors = commandDescriptors.toSet()
	val entityTypes = entityTypes.toSet()

	@UseExperimental(ImplicitReflectionSerializer::class)
	val serializationContext = serializersModuleOf(mapOf(
		CommandRequest::class to ConcreteCommandRequestSerializer(commandDescriptors = commandDescriptors, metaSerializer = commandRequestMetaClass.serializer()),
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

		commandDescriptors.groupBy { it.name }
			.forEach { (name, descriptors) ->
				check(descriptors.size == 1) { "multiple commands have the same name: $name" }
			}

		entityTypes.groupBy { it.namespace }
			.forEach { (namespace, descriptors) ->
				check(descriptors.size == 1) { "multiple entity types have the same namespace: $namespace" }
			}

		entityTypes.groupBy { it.idClass }
			.forEach { (idClass, descriptors) ->
				check(descriptors.size == 1) { "multiple entity types have the same ID class: $idClass" }
			}
	}


	protected open fun createDefaultResponseMeta(): TCommandResponseMeta? = null
	abstract fun createRequestMetaForCommand(command: Command): TCommandRequestMeta


	companion object
}
