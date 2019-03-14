package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


@JSON
inline class PostalCode(val value: String) {

	override fun toString() = value
}
