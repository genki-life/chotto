package team.genki.chotto.core

import kotlin.reflect.*


abstract class ClientModel<TCommandRequestMeta : CommandRequest.Meta, TCommandResponseMeta : CommandResponse.Meta>(
	val name: String,
	commandDescriptors: Collection<Command.Typed.Descriptor<*, *>>,
	commandRequestMetaClass: KClass<TCommandRequestMeta>,
	commandResponseMetaClass: KClass<TCommandResponseMeta>,
	entityTypes: Collection<EntityType<*, *>>,
	jsonConfiguration: JsonConfiguration
) {

	val commandDescriptors: Collection<Command.Descriptor> = commandDescriptors.toList()
	val entityTypes: Collection<EntityType<*, *>> = entityTypes.toList()

	@Suppress("LeakingThis")
	val jsonConverter = JsonConverter(
		jsonConfiguration,
		commandDescriptors,
		entityTypes,
		commandRequestMetaClass,
		commandResponseMetaClass,
		this::createDefaultResponseMeta
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
				check(descriptors.size == 1) { "multiple entity types have the same ID class: ${idClass.qualifiedName}" }
			}
	}


	protected open fun createDefaultResponseMeta(): TCommandResponseMeta? = null
	abstract fun createRequestMetaForCommand(command: Command): TCommandRequestMeta


	companion object
}
