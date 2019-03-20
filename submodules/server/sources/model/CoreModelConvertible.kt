package team.genki.chotto.server


interface CoreModelConvertible<out CoreValue : Any, in Transaction : ChottoTransaction> {

	fun Transaction.toCoreModel(): CoreValue
}
