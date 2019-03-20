package team.genki.chotto.core


expect class Timezone {

	val id: String


	companion object {

		val utc: Timezone


		fun byId(id: String): Timezone?
	}
}
