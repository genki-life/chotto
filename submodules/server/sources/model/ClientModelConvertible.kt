package team.genki.chotto.server


interface ClientModelConvertible<out CoreValue : Any, in Transaction : ChottoTransaction> {

	fun toClientModel(transaction: Transaction): CoreValue
}
