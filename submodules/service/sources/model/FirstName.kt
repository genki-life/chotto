package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


@JSON
inline class FirstName(val value: String) {

	override fun toString() = value
}
