package tests

import com.github.fluidsonic.fluid.stdlib.*
import kotlin.test.*


@Suppress("unused")
object DayOfWeekTest : JsonTest() {

	@Test
	fun testDisplayName() {
		assertEquals("Monday", DayOfWeek.monday.displayName(Locale.englishInUnitedStates))
		assertEquals("Tuesday", DayOfWeek.tuesday.displayName(Locale.englishInUnitedStates))
		assertEquals("Wednesday", DayOfWeek.wednesday.displayName(Locale.englishInUnitedStates))
		assertEquals("Thursday", DayOfWeek.thursday.displayName(Locale.englishInUnitedStates))
		assertEquals("Friday", DayOfWeek.friday.displayName(Locale.englishInUnitedStates))
		assertEquals("Saturday", DayOfWeek.saturday.displayName(Locale.englishInUnitedStates))
		assertEquals("Sunday", DayOfWeek.sunday.displayName(Locale.englishInUnitedStates))
	}


	@Test
	fun testJson() =
		mapOf(
			DayOfWeek.monday to "monday",
			DayOfWeek.tuesday to "tuesday",
			DayOfWeek.wednesday to "wednesday",
			DayOfWeek.thursday to "thursday",
			DayOfWeek.friday to "friday",
			DayOfWeek.saturday to "saturday",
			DayOfWeek.sunday to "sunday"
		).forEach { (value, string) ->
			testJson(
				value = value,
				json = """ "$string" """
			)
		}


	@Test
	fun testMinus() {
		assertEquals(DayOfWeek.monday, DayOfWeek.tuesday - 1)
		assertEquals(DayOfWeek.tuesday, DayOfWeek.wednesday - 1)
		assertEquals(DayOfWeek.wednesday, DayOfWeek.thursday - 1)
		assertEquals(DayOfWeek.thursday, DayOfWeek.friday - 1)
		assertEquals(DayOfWeek.friday, DayOfWeek.saturday - 1)
		assertEquals(DayOfWeek.saturday, DayOfWeek.sunday - 1)
		assertEquals(DayOfWeek.sunday, DayOfWeek.monday - 1)

		assertEquals(DayOfWeek.monday, DayOfWeek.tuesday - 8)
		assertEquals(DayOfWeek.tuesday, DayOfWeek.wednesday - 8)
		assertEquals(DayOfWeek.wednesday, DayOfWeek.thursday - 8)
		assertEquals(DayOfWeek.thursday, DayOfWeek.friday - 8)
		assertEquals(DayOfWeek.friday, DayOfWeek.saturday - 8)
		assertEquals(DayOfWeek.saturday, DayOfWeek.sunday - 8)
		assertEquals(DayOfWeek.sunday, DayOfWeek.monday - 8)
	}


	@Test
	fun testPlus() {
		assertEquals(DayOfWeek.monday, DayOfWeek.sunday + 1)
		assertEquals(DayOfWeek.tuesday, DayOfWeek.monday + 1)
		assertEquals(DayOfWeek.wednesday, DayOfWeek.tuesday + 1)
		assertEquals(DayOfWeek.thursday, DayOfWeek.wednesday + 1)
		assertEquals(DayOfWeek.friday, DayOfWeek.thursday + 1)
		assertEquals(DayOfWeek.saturday, DayOfWeek.friday + 1)
		assertEquals(DayOfWeek.sunday, DayOfWeek.saturday + 1)

		assertEquals(DayOfWeek.monday, DayOfWeek.sunday + 8)
		assertEquals(DayOfWeek.tuesday, DayOfWeek.monday + 8)
		assertEquals(DayOfWeek.wednesday, DayOfWeek.tuesday + 8)
		assertEquals(DayOfWeek.thursday, DayOfWeek.wednesday + 8)
		assertEquals(DayOfWeek.friday, DayOfWeek.thursday + 8)
		assertEquals(DayOfWeek.saturday, DayOfWeek.friday + 8)
		assertEquals(DayOfWeek.sunday, DayOfWeek.saturday + 8)
	}
}
