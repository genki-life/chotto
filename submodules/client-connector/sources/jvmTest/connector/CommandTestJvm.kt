package tests

import com.github.fluidsonic.fluid.json.*


actual fun CommandTest.parseJson(json: String) =
	JSONParser.default.parseValueOrNull(json)
