package util

import cinescout.database.Database
import cinescout.di.CineScoutModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import kotlin.test.BeforeTest

abstract class BaseAppTest : AutoCloseKoinTest() {

	protected open val extraModule = module {  }

	@BeforeTest
	fun setup() {
		startKoin {
			allowOverride(true)
			modules(listOf(CineScoutModule, extraModule))
		}
		Database.Schema.create(get())
	}
}
