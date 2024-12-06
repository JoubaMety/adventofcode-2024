import java.io.File
import kotlin.math.pow
import kotlin.math.round
import kotlin.system.measureNanoTime

fun convertTime(nanoseconds: Long): String {
    val remainderDigits = 10.0.pow(1)
    return if (nanoseconds >= 1_000_000_000) {
        ANSI.YELLOW + "${round(nanoseconds / 1_000_000_000.0 * remainderDigits) / remainderDigits}" + ANSI.RESET + " seconds"
    } else if (nanoseconds >= 1_000_000) {
        ANSI.YELLOW + "${round(nanoseconds / 1_000_000.0 * remainderDigits) / remainderDigits}" + ANSI.RESET + " milliseconds"
    } else if (nanoseconds >= 1_000) {
        ANSI.YELLOW + "${round(nanoseconds / 1_000.0 * remainderDigits) / remainderDigits}" + ANSI.RESET + " microseconds"
    }
    else {
        ANSI.YELLOW + "$nanoseconds" + ANSI.RESET + " nanoseconds (ns)"
    }
}
/*
    Returns a list of ordering rules and a list of updates
 */
fun day05_convertInput(input: String): Pair<List<List<Int>>, List<List<Int>>> {
    val split = input.split("\n\n")
    val lines = split[0].lines().map { it.split("|").map{ it1 -> it1.toInt()} }
    val updateLines = split[1].lines()
    val updateList = mutableListOf<List<Int>>()
    for (line in updateLines) {
        if(line == "") { continue }
        updateList.add(line.split(",").map { it.toInt() })
    }
    return lines to updateList
}

fun getCorrectUpdates(inputOrdering: List<List<Int>>, inputUpdates: List<List<Int>>): List<Int> {
    val correctUpdates = mutableListOf<Int>()
    for ((updateIndex, update) in inputUpdates.withIndex()) {
        var correct = true
        for ((numberIndex, number) in update.withIndex()) {
            for(lookAheadIndex in numberIndex+1..<update.size) {
                if(inputOrdering.any { it[0] == update[lookAheadIndex] && it[1] == number }) {
                    correct = false
                    break
                }
            }
        }
        if(correct) correctUpdates.add(updateIndex)
    }
    return correctUpdates
}

fun getIncorrectUpdates(inputOrdering: List<List<Int>>, inputUpdates: List<List<Int>>): List<Int> {
    val correctUpdates = getCorrectUpdates(inputOrdering, inputUpdates)
    val mutableList = mutableListOf<Int>()
    for (index in inputUpdates.indices) {
        if (!correctUpdates.contains(index))
            mutableList.add(index)
    }
    return mutableList
}

fun checkIfUpdateCorrect(inputOrdering: List<List<Int>>, inputUpdate: List<Int>): Boolean {
    var correct = true
    for ((numberIndex, number) in inputUpdate.withIndex()) {
        for(lookAheadIndex in numberIndex+1..<inputUpdate.size) {
            if(inputOrdering.any { it[0] == inputUpdate[lookAheadIndex] && it[1] == number }) {
                    correct = false
                    break
                }
            }
        }
    return correct
}

fun part01_05(inputOrdering: List<List<Int>>, inputUpdates: List<List<Int>>): Int {
    val correctUpdates = getCorrectUpdates(inputOrdering, inputUpdates)
    var sum = 0
    for (updateIndex in correctUpdates) {
        val update = inputUpdates[updateIndex]
        sum += update[(update.size/2)]
    }
    return sum
}

fun reorderUpdate(inputOrdering: List<List<Int>>, inputUpdate: List<Int>): List<Int> {
    val mutableList = inputUpdate.toMutableList()
    var correct = false
    while (!correct) {
        if (checkIfUpdateCorrect(inputOrdering, mutableList)) correct = true
        for((l, r) in inputOrdering) {
            val uLI = mutableList.indexOf(l)
            val uRI = mutableList.indexOf(r)
            if(uLI != -1 && uRI != -1) {
                if (uLI > uRI) {
                    mutableList[uRI] = l
                    mutableList[uLI] = r
                }
            }
        }
    }
    return mutableList
}

fun part02_05(inputOrdering: List<List<Int>>, inputUpdates: List<List<Int>>): Int {
    val incorrectUpdates = getIncorrectUpdates(inputOrdering, inputUpdates)
    val mutableList = mutableListOf<List<Int>>()
    for(updateIndex in incorrectUpdates) {
        mutableList.add(
            reorderUpdate(inputOrdering,inputUpdates[updateIndex])
        )
    }
    var sum = 0
    for (update in mutableList) {
        if (update.isEmpty()) break
        sum += update[(update.size/2)]
    }
    return sum
}

fun main() {
    val input: String
    val part01: Int
    val part02: Int
    val inputUpdates: List<List<Int>>
    val inputOrdering: List<List<Int>>
    val elapsedLoadFile = measureNanoTime {
        input = File("./src/05.input.txt")
            .readText(Charsets.UTF_8)
    }
    val elapsedConversionInput = measureNanoTime {
        val pair = day05_convertInput(input)
        inputOrdering = pair.first
        inputUpdates = pair.second
    }
    val elapsedPart01 = measureNanoTime {
        part01 = part01_05(inputOrdering, inputUpdates)
    }
    val elapsedPart02 = measureNanoTime {
        part02 = part02_05(inputOrdering, inputUpdates)
    }

    val title = "Advent Of Code 2024 - Day 05"
    println(ANSI.GREEN + title + ANSI.RESET + "\n" +
            (ANSI.RED + "-" + ANSI.RESET).repeat(title.length))
    println("Loaded input in " + convertTime(elapsedLoadFile) + "\n" +
            "Converted input in " + convertTime(elapsedConversionInput) + "\n" +
            "First Part:  " + ANSI.BLUE + part01 + ANSI.RESET + " (" + convertTime(elapsedPart01) + ")\n" +
            "Second Part: " + ANSI.BLUE + part02 + ANSI.RESET + " (" + convertTime(elapsedPart02) + ")\n" +
            "Finished in " + convertTime(elapsedLoadFile + elapsedConversionInput + elapsedPart01 + elapsedPart02)
    )
}