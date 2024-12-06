import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureNanoTime

enum class GuardDirection {
    UP, RIGHT, DOWN, LEFT
}

data class GuardPos(
    val x: Int,
    val y: Int,
    val direction: GuardDirection,
)

data class Coords(
    val x: Int,
    val y: Int,
)

/*
    PART 01
 */

fun day06_part1(input: List<List<Char>>): Int {
    val height = input.size
    val width = input[0].size
    val mutableList = day06_convertListToMutableList(input)
    while (true) {
        val guardPos = day06_findGuardPos(mutableList) ?: return -1
        val newCoords = when(guardPos.direction) {
            GuardDirection.UP -> Coords(guardPos.x, guardPos.y-1)
            GuardDirection.RIGHT -> Coords(guardPos.x+1, guardPos.y)
            GuardDirection.DOWN -> Coords(guardPos.x, guardPos.y+1)
            GuardDirection.LEFT -> Coords(guardPos.x-1, guardPos.y)
        }
        if((newCoords.y < 0 || newCoords.y >= height) ||
            (newCoords.x < 0 || newCoords.x >= width)) {
            mutableList[guardPos.y][guardPos.x] = 'X'
            break
        }
        if(mutableList[newCoords.y][newCoords.x] == '#')
            mutableList[guardPos.y][guardPos.x] = when(guardPos.direction) {
                GuardDirection.UP -> '>'
                GuardDirection.RIGHT -> 'v'
                GuardDirection.DOWN -> '<'
                GuardDirection.LEFT -> '^'
            }
        else {
            mutableList[guardPos.y][guardPos.x] = 'X'
            mutableList[newCoords.y][newCoords.x] = when (guardPos.direction) {
                GuardDirection.UP -> '^'
                GuardDirection.RIGHT -> '>'
                GuardDirection.DOWN -> 'v'
                GuardDirection.LEFT -> '<'
            }
        }
    }
    var steps = 0
    for(line in mutableList) {
        for(char in line)
            if (char == 'X')
                steps++
    }
    return steps
}

fun day06_findGuardPos(input: List<List<Char>>): GuardPos? {
    for(y in input.indices)
        for(x in input[y].indices)
            if (input[y][x] == '^')
                return GuardPos(x, y, GuardDirection.UP)
            else if (input[y][x] == '>')
                return GuardPos(x, y, GuardDirection.RIGHT)
            else if (input[y][x] == '<')
                return GuardPos(x, y, GuardDirection.LEFT)
            else if (input[y][x] == 'v')
                return GuardPos(x, y, GuardDirection.DOWN)
    return null
}

/*
    PART 02
 */

data class GuardRun(
    val run: MutableList<MutableList<Char>>,
    val success: Boolean = false
)

fun day06_part2(input: List<List<Char>>): Int {
    val height = input.size
    val width = input[0].size
    val maxTries = (height*width)
    val successes = AtomicInteger()
    val firstRun = day06_part2_guardRun(input, maxTries) ?: return -1
    val coordsToIntercept = day06_part2_getIndexes(firstRun.run)
    runBlocking {
        withContext(Dispatchers.Default) {
            day06_part2_coroutine(coordsToIntercept) { coord ->
                val newList = day06_convertListToMutableList(input)
                newList[coord.y][coord.x] = 'O'
                val run = day06_part2_guardRunSkipX(newList, maxTries)
                if (run != null && !run.success)
                    successes.incrementAndGet()
            }
        }
    }

    return successes.toInt()
}

fun day06_part2_guardRunSkipX(input: List<List<Char>>, maxTries: Int): GuardRun? {
    val height = input.size
    val width = input[0].size
    var success = true
    var guardPos = day06_findGuardPos(input) ?: return null

    var obstacleHit = 0
    val maxObstacleHit = 2
    var tries = 0
    while (true) {
        val newCoords = when (guardPos.direction) {
            GuardDirection.UP -> Coords(guardPos.x, guardPos.y - 1)
            GuardDirection.RIGHT -> Coords(guardPos.x + 1, guardPos.y)
            GuardDirection.DOWN -> Coords(guardPos.x, guardPos.y + 1)
            GuardDirection.LEFT -> Coords(guardPos.x - 1, guardPos.y)
        }
        if ((newCoords.y < 0 || newCoords.y >= height) ||
            (newCoords.x < 0 || newCoords.x >= width)
        ) {
            break
        }
        if(input[newCoords.y][newCoords.x] == 'O' && obstacleHit > maxObstacleHit) {
            success = false
            break
        }
        if (input[newCoords.y][newCoords.x] == '#' || input[newCoords.y][newCoords.x] == 'O') {
            if(input[newCoords.y][newCoords.x] == 'O') obstacleHit++
            guardPos = when (guardPos.direction) {
                GuardDirection.UP -> GuardPos(guardPos.x, guardPos.y, GuardDirection.RIGHT)
                GuardDirection.RIGHT -> GuardPos(guardPos.x, guardPos.y, GuardDirection.DOWN)
                GuardDirection.DOWN -> GuardPos(guardPos.x, guardPos.y, GuardDirection.LEFT)
                GuardDirection.LEFT -> GuardPos(guardPos.x, guardPos.y, GuardDirection.UP)
            }
        } else {
            guardPos = GuardPos(newCoords.x, newCoords.y, guardPos.direction)
        }
        tries++
        if(tries >= maxTries) {
            success = false
            break
        }
    }
    return GuardRun(mutableListOf(), success)
}

fun day06_part2_guardRun(input: List<List<Char>>, maxTries: Int): GuardRun? {
    val height = input.size
    val width = input[0].size
    val mutableList = mutableListOf<MutableList<Char>>()
    for (list in input) {
        mutableList.add(list.toMutableList())
    }
    var success = true
    var guardPos = day06_findGuardPos(mutableList) ?: return null
    var tries = 0
    while (true) {
        val newCoords = when (guardPos.direction) {
            GuardDirection.UP -> Coords(guardPos.x, guardPos.y - 1)
            GuardDirection.RIGHT -> Coords(guardPos.x + 1, guardPos.y)
            GuardDirection.DOWN -> Coords(guardPos.x, guardPos.y + 1)
            GuardDirection.LEFT -> Coords(guardPos.x - 1, guardPos.y)
        }
        if ((newCoords.y < 0 || newCoords.y >= height) ||
            (newCoords.x < 0 || newCoords.x >= width)
        ) {
            break
        }
        if (mutableList[newCoords.y][newCoords.x] == '#') {
            guardPos = when (guardPos.direction) {
                GuardDirection.UP -> GuardPos(guardPos.x, guardPos.y, GuardDirection.RIGHT)
                GuardDirection.RIGHT -> GuardPos(guardPos.x, guardPos.y, GuardDirection.DOWN)
                GuardDirection.DOWN -> GuardPos(guardPos.x, guardPos.y, GuardDirection.LEFT)
                GuardDirection.LEFT -> GuardPos(guardPos.x, guardPos.y, GuardDirection.UP)
            }
        } else {
            guardPos = GuardPos(newCoords.x, newCoords.y, guardPos.direction)
            if(mutableList[newCoords.y][newCoords.x] != '^')
                mutableList[newCoords.y][newCoords.x] = 'X'
        }
        tries++
        if(tries >= maxTries) {
            success = false
            break
        }
    }
    return GuardRun(mutableList, success)
}

fun day06_part2_getIndexes(input: List<List<Char>>): List<Coords> {
    val mutableList = mutableListOf<Coords>()
    for ((y, line) in input.withIndex()) {
        for ((x, char) in line.withIndex()) {
            if (char == 'X')
                mutableList.add(Coords(x, y))
        }
    }
    return mutableList
}

suspend fun day06_part2_coroutine(list: List<Coords>, action: suspend (coords: Coords) -> Unit) {
    coroutineScope {
        for(coord in list) {
            launch {
                action(coord)
            }
        }
    }
}


/* Misc functions */

fun day06_print(input: List<List<Char>>) {
    for (line in input) {
        for (char in line) {
            when (char) {
                '.' -> print(ANSI.GRAY)
                '^' -> print(ANSI.RED)
                'X', '|', '-', '+' -> print(ANSI.BLUE)
                'O' -> print(ANSI.YELLOW)
            }
            print("$char")

            print("${ANSI.RESET} ")
        }
        println()
    }
}

fun day06_convertInput(input: String): List<List<Char>> {
    val lines = input.lines()
    val outputList = mutableListOf<List<Char>>()
    for (line in lines) {
        if(line.isEmpty()) continue
        val mutableList: MutableList<Char> = mutableListOf()
        for (char in line) {
            mutableList.add(char)
        }
        outputList.add(mutableList)
    }
    return outputList
}

fun day06_convertListToMutableList(input: List<List<Char>>): MutableList<MutableList<Char>> {
    val mutableList = mutableListOf<MutableList<Char>>()
    for(line in input) {
        mutableList.add(line.toMutableList())
    }
    return mutableList.toMutableList()
}

fun main() {
    val input: String
    val part01: Int
    val part02: Int
    val convertedInput: List<List<Char>>
    val elapsedLoadFile = measureNanoTime {
        input = File("./src/06.input.txt")
            .readText(Charsets.UTF_8)
    }
    val elapsedConversionInput = measureNanoTime {
        convertedInput = day06_convertInput(input)
    }
    val elapsedPart01 = measureNanoTime {
        part01 = day06_part1(convertedInput)
    }
    val elapsedPart02 = measureNanoTime {
        part02 = day06_part2(convertedInput)
    }

    val title = "Advent Of Code 2024 - Day 06"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Loaded input in " + convertTime(elapsedLoadFile) + "\n" +
            "Converted input in " + convertTime(elapsedConversionInput) + "\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + convertTime(elapsedPart01) + ")\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + convertTime(elapsedPart02) + ")\n" +
            "Finished in " + convertTime(elapsedLoadFile + elapsedConversionInput + elapsedPart01 + elapsedPart02)
    )
}