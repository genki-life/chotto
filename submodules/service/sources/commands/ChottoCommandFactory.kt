package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


abstract class ChottoCommandFactory<in Transaction : ChottoTransaction, Command : ChottoCommand, Result : Any>(
	name: String
) {

	val name = ChottoCommandName(name)


	abstract fun JSONDecoder<Transaction>.decodeCommand(): Command


	open fun JSONEncoder<Transaction>.encodeResult(result: Result) {
		writeIntoMap {}
	}


	abstract class Empty<in Transaction : ChottoTransaction, Command : ChottoCommand, Result : Any>(
		name: String
	) : ChottoCommandFactory<Transaction, Command, Result>(name = name) {

		abstract fun createCommand(): Command


		final override fun JSONDecoder<Transaction>.decodeCommand() =
			createCommand().also { skipValue() }
	}
}


fun <Transaction : ChottoTransaction, Result : Any> ChottoCommandFactory<Transaction, *, Result>.encodeResult(result: Result, encoder: JSONEncoder<Transaction>) =
	encoder.encodeResult(result)

