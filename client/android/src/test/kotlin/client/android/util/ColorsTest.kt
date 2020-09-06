package client.android.util

import androidx.compose.ui.graphics.Color
import assert4k.*
import util.percent
import kotlin.test.*

class ColorsTest {

    @Test
    fun `blend with 0 percent returns receiver Color`() {
        assert that Red.blend(Color.Blue, 0.percent) equals Red
    }

    @Test
    fun `blend with 0 percent returns other Color`() {
        assert that Red.blend(Color.Blue, 100.percent) equals Blue
    }

    @Test
    fun `blend between White and Black at 50 percent returns Grey`() {
        val r = White.blend(Black, 50.percent)

        assert that r *{
            +red() between 0.5f .. 0.502f
            +green() between 0.5f .. 0.502f
            +blue() between 0.5f .. 0.502f
            +alpha() equals 1
        }
    }

    @Test
    fun `blend between White and Green at 20 percent returns very light Green`() {
        val r = White.blend(Green, 20.percent)

        assert that r *{
            +red() equals 0.8
            +green() equals 1
            +blue() equals 0.8
        }
    }

    @Test
    fun `blend between White and Green at 50 percent returns light Green`() {
        val r = White.blend(Green, 50.percent)

        assert that r *{
            +red() between 0.5f .. 0.502f
            +green() equals 1
            +blue() between 0.5f .. 0.502f
        }
    }

    @Test
    fun `blend between White and Green at 80 percent returns almost Green`() {
        val r = White.blend(Green, 80.percent)

        assert that r *{
            +red() equals 0.2
            +green() equals 1
            +blue() equals 0.2
        }
    }

    @Test
    fun `blend between Black and Green at 20 percent returns very dark Green`() {
        val r = Black.blend(Green, 20.percent)

        assert that r *{
            +red() equals 0
            +green() equals 0.2
            +blue() equals 0
        }
    }

    @Test
    fun `blend between Black and Green at 50 percent returns dark Green`() {
        val r = Black.blend(Green, 50.percent)

        assert that r *{
            +red() equals 0
            +green() between 0.5f .. 0.502f
            +blue() equals 0
        }
    }

    @Test
    fun `blend between Black and Green at 80 percent returns a little dark Green`() {
        val r = Black.blend(Green, 80.percent)

        assert that r *{
            +red() equals 0
            +green() equals 0.8
            +blue() equals 0
        }
    }

    @Test
    fun `blend between Red and Blue at 50 percent returns Violet`() {
        val r = Blue.blend(Red, 50.percent)

        assert that r *{
            +red() between 0.5f .. 0.502f
            +green() equals 0
            +blue() between 0.5f .. 0.502f
        }
    }

    private val Blue = Color.Blue
    private val Black = Color.Black
    private val Green = Color.Green
    private val Red = Color.Red
    private val White = Color.White
}
