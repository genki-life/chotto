package team.genki.chotto.server

import com.auth0.jwt.*
import com.auth0.jwt.interfaces.*


interface Authenticator<in Transaction : ChottoTransaction> {

	val jwtRealm: String
	val jwtVerifier: JWTVerifier

	suspend fun Transaction.authenticate(jwtPayload: Payload): Boolean
}
