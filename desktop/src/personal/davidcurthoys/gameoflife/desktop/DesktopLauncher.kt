package personal.davidcurthoys.gameoflife.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import personal.davidcurthoys.gameoflife.LifeOfGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        with(config) {
            height = 750
            width = 750
            title = "Conway's Game of Life"
            fullscreen = false
            resizable = false
        }
        LwjglApplication(LifeOfGame(), config)
    }
}
