import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.forEachLine
import kotlin.io.path.name
import kotlin.system.measureNanoTime

data class Size(
    val height: Int,
    val width: Int,
)

fun day08_getInput(input: Path): Pair<MutableMap<Char, MutableList<Coords>>,Size> {
    val mutableMap = mutableMapOf<Char, MutableList<Coords>>()
    var y = 0
    var width = 0
    input.forEachLine { line ->
        if(line.isNotEmpty()) {
            width = line.length
            for ((x, char) in line.withIndex())
                if (char != '.' && char != '#' || char.isDigit()) {
                    if (mutableMap[char] == null)
                        mutableMap[char] = mutableListOf()
                    mutableMap[char]!!.add(Coords(x, y))
                }
            y++
        }
    }
    return Pair(mutableMap, Size(y, width))
}

fun day08_part01(input: MutableMap<Char, MutableList<Coords>>, size: Size): Int {
    val height = size.height
    val width = size.width
    val mutableSet = mutableSetOf<Coords>()
    for((node, coords) in input) {
        for((index, coord) in coords.withIndex()) {
            for(subcoord in coords) {
                if (subcoord == coord) continue
                val vector = Coords(subcoord.x - coord.x, subcoord.y - coord.y)
                val newCoords = Coords(subcoord.x + vector.x, subcoord.y + vector.y)
                if((newCoords.x in 0..<width) &&
                    (newCoords.y in 0..<height))
                    mutableSet.add(newCoords)
            }
        }
    }
    return mutableSet.count()
}

fun day08_part02(input: MutableMap<Char, MutableList<Coords>>, size: Size): Int {
    val height = size.height
    val width = size.width
    val mutableSet = mutableSetOf<Coords>()
    for((node, coords) in input) {
        for((index, coord) in coords.withIndex()) {
            mutableSet.add(coord)
            for(subcoord in coords) {
                if (subcoord == coord) continue
                val vector = subcoord.minus(coord.x, coord.y)
                var newCoords = subcoord.plus(vector.x, vector.y)
                while((newCoords.x in 0..<width) &&
                    (newCoords.y in 0..<height)) {
                    mutableSet.add(newCoords)
                    newCoords = newCoords.plus(vector.x, vector.y)
                }
            }
        }
    }
    return mutableSet.count()
}

fun main() {
    val path : Path
    val part01: Int
    val part02: Int
    val input: MutableMap<Char, MutableList<Coords>>
    val size: Size
    val elapsedLoadFile = measureNanoTime {
        path = Path("./src/08.input.txt")
        val pair = day08_getInput(path)
        input = pair.first
        size = pair.second
    }
    val elapsedPart01 = measureNanoTime {
        part01 = day08_part01(input, size)
        //part01 = 0
    }
    val elapsedPart02 = measureNanoTime {
        part02 = day08_part02(input, size)
    }

    val title = "Advent Of Code 2024 - Day 08"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            "Input: " + ANSI.BLUE + path.name + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Got the list in " + convertTime(elapsedLoadFile) + "\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + convertTime(elapsedPart01) + ")\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + convertTime(elapsedPart02) + ")\n" +
            "Finished in " + convertTime(elapsedLoadFile + elapsedPart01 + elapsedPart02)
    )
}