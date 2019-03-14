package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


inline val <Transaction : ChottoTransaction> JSONDecoder<Transaction>.transaction
	get() = context
