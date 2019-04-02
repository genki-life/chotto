package team.genki.chotto.server

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.Payload


interface Authenticator<in Transaction : ChottoTransaction> {

	val jwtRealm: String
	val jwtVerifier: JWTVerifier

	suspend fun Transaction.authenticate(jwtPayload: Payload): Boolean
}
