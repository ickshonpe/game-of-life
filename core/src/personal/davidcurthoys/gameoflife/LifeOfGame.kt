package personal.davidcurthoys.gameoflife

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

fun countNeighbours(map: Array<out Array<out Boolean>>, x: Int, y: Int): Int {
    return listOf(
            Pair(-1, 0),
            Pair(-1, -1),
            Pair(-1, 1),
            Pair(1, 0),
            Pair(1, -1),
            Pair(1, 1),
            Pair(0, -1),
            Pair(0, 1))
            .mapNotNull { map.getOrNull(x + it.first)?.getOrNull(y + it.second) }
            .count { it }
}

fun updateCell(isAlive: Boolean, neighbourCount: Int) =
        isAlive && neighbourCount in 2..3
                || neighbourCount == 3

fun updateMap(world: Array<out Array<out Boolean>>) =
        Array(world.size) { x ->
            Array(world[x].size) { y ->
                updateCell(world[x][y], countNeighbours(world, x, y))
            }
        }

class LifeOfGame : ApplicationAdapter() {
    private lateinit var shapeRenderer: ShapeRenderer
    private val rows = 100
    private val columns = 100
    private val updateFrequency = 1.0f
    var updateTimer = 0f
    private var world = Array(columns) { Array(rows) { false } }
    var paused = false

    override fun create() {
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val spacing = 7.5f
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        for (x in 0 until world.size) {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f)
            shapeRenderer.line(x * spacing, 0f, x * spacing, world[x].size * spacing)
        }
        for (y in 0 until world[0].size) {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f)
            shapeRenderer.line(0f, y * spacing, world.size * spacing, y * spacing)
        }
        shapeRenderer.end()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 1f)
        world.forEachIndexed { x, r ->
            r.forEachIndexed { y, isAlive ->
                    if (isAlive) { shapeRenderer.rect(x * spacing, y * spacing, spacing, spacing) }
            }
        }
        shapeRenderer.end()
        if (!paused) {
            updateTimer += Gdx.graphics.deltaTime
            if (updateFrequency < updateTimer) {
                updateTimer = 0f
                world = updateMap(world)
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { Gdx.app.exit() }
        paused = if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) !paused else paused
        val pointerX = (Gdx.input.x / spacing).toInt()
        val pointerY = ((Gdx.graphics.height - Gdx.input.y - 1) / spacing).toInt()
        if (pointerX in 0 until world.size && pointerY in 0 until world[pointerX].size) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { world[pointerX][pointerY] = true }
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) { world[pointerX][pointerY] = false }
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}


