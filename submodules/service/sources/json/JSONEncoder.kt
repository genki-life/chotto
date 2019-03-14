package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


inline val <Transaction : ChottoTransaction> JSONEncoder<Transaction>.transaction
	get() = context
