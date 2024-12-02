import java.io.File
import kotlin.math.abs

fun getGoodData(input: String, problemDampener: Boolean = false): Int {
    val lines = input.lines()
    var safe = lines.count()
    for (line in lines) {
        val strings = line.split(' ')
        var remainingFaults = if(problemDampener) strings.count() else 1
        for(i in strings.indices) {
            val mutableList = strings.toMutableList()
            if (problemDampener) mutableList.removeAt(i)
            val newStr = mutableList.toList()
            var previous = -1
            var descending: Boolean? = null
            for (str in newStr) {
                val number = str.toInt()
                // first number, skip check
                if (previous == -1) { previous = number; continue }
                // otherwise
                if((number == previous) || (abs(number - previous) > 3) ||
                    (descending == true && number > previous) || (descending == false && number < previous)
                ) {
                    remainingFaults--
                    break
                } else if (descending == null) {
                    descending = number <= previous
                }
                previous = number
            } // for (str in newStr)
            if(!problemDampener) break
        } // for (i in strings.indices)
        if(remainingFaults <= 0) safe--
    } // for (line in lines)
    return safe
}

fun main() {
    val title = "Advent Of Code 2024 - Day 02"
    println(title)
    println("-".repeat(title.length))
    val input = File("./src/02.input.txt").readText(Charsets.UTF_8)
    val part01 = getGoodData(input)
    val part02 = getGoodData(input, true)
    println("First Part:  $part01")
    println("Second Part: $part02")
}