package personal.davidcurthoys.gameoflife

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class LifeOfGame : ApplicationAdapter() {

    private lateinit var shapeRenderer: ShapeRenderer
    private val rows = 100
    private val columns = 100
    private val updateFrequency = 1.0f
    var updateTimer = 0f
    private var map = Array(columns) { Array(rows) { false } }
    var paused = false

    override fun create() {
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val spacing = 7.5f
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        for (x in 0 until columns) {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f)
            shapeRenderer.line(x * spacing, 0f, x * spacing, rows * spacing)
        }
        for (y in 0 until rows) {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f)
            shapeRenderer.line(0f, y * spacing, columns * spacing, y * spacing)
        }
        shapeRenderer.end()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 1f)
        for (x in 0 until columns) {
            for (y in 0 until rows) {
                if (map[x][y]) {
                    shapeRenderer.rect(x * spacing, y * spacing, 1f * spacing, 1f * spacing)
                }
            }
        }

        shapeRenderer.end()
        if (!paused) {
            updateTimer += Gdx.graphics.deltaTime
            if (updateTimer > updateFrequency) {
                updateTimer = 0f
                map = updateMap(map)
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            paused = !paused
        }


        val pointerX = (Gdx.input.x / spacing).toInt()
        val pointerY = ((Gdx.graphics.height - Gdx.input.y - 1) / spacing).toInt()
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (pointerX in 0 until map.size && pointerY in 0 until map[pointerX].size) {
                map[pointerX][pointerY] = true
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if (pointerX in 0 until map.size && pointerY in 0 until map[pointerX].size) {
                map[pointerX][pointerY] = false
            }
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}

fun countNeighbours(map: Array<out Array<out Boolean>>, x: Int, y: Int): Int {
    return listOf(
            Pair(x - 1, y),
            Pair(x - 1, y - 1),
            Pair(x - 1, y + 1),
            Pair(x + 1, y),
            Pair(x + 1, y - 1),
            Pair(x + 1, y + 1),
            Pair(x, y - 1),
            Pair(x, y + 1))
            .mapNotNull { (x, y) -> map.getOrNull(x)?.getOrNull(y) }
            .count { it }
}

fun updateCell(isAlive: Boolean, neighbourCount: Int) =
        isAlive && neighbourCount in 2..3
        || neighbourCount == 3

fun updateMap(map: Array<out Array<out Boolean>>) =
        Array(map.size) { x ->
            Array(map[x].size) { y ->
                updateCell(map[x][y], countNeighbours(map, x, y))
            }
        }




