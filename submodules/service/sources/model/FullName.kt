package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


@JSON
inline class FullName(val value: String) {

	constructor(firstName: FirstName, lastName: LastName) :
		this("$firstName $lastName")


	override fun toString() = value
}
