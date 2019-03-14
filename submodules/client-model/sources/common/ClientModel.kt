package team.genki.chotto.client.model

import kotlin.reflect.KClass


abstract class ClientModel<CommandRequestMeta : CommandRequest.Meta, CommandResponseMeta : CommandResponse.Meta>(
	commandDescriptors: Collection<Command.Descriptor<*, *>>,
	commandRequestMetaClass: KClass<CommandRequestMeta>,
	commandResponseMetaClass: KClass<CommandResponseMeta>,
	entityTypes: Collection<EntityType<*, *>>,
	jsonConfiguration: JsonConfiguration
) {

	val commandDescriptors: Collection<Command.Descriptor<*, *>> = commandDescriptors.toList()
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


	protected open fun createDefaultResponseMeta(): CommandResponseMeta? = null
	abstract fun createRequestMetaForCommand(command: Command<*>): CommandRequestMeta
}
