import java.io.File
import kotlin.math.abs

fun getGoodDataV2(input: String, problemDampener: Boolean = false): Int {
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
                if (previous == -1) {
                    previous = number
                    continue
                }
                // otherwise
                if((number == previous) ||
                    (abs(number - previous) > 3) ||
                    (descending == true && number > previous) ||
                    (descending == false && number < previous)
                ) {
                    remainingFaults--
                    break
                } else if (descending == null) {
                    descending = number <= previous
                }
                previous = number
            }
            if(!problemDampener) break
        }
        if(remainingFaults <= 0) safe--
    }
    return safe
}

fun main() {
    val input = File("./src/02.input.txt").readText(Charsets.UTF_8)
    val part01 = getGoodDataV2(input)
    val part02 = getGoodDataV2(input, true)
    println("Part 1: $part01 ${if(part01 == 279) "✅CORRECT✅" else ""}")
    println("Part 2: $part02 ${if (part02 <= 323) "‼\uFE0FTOO LOW‼\uFE0F" else if (part02 >= 412) "‼\uFE0Ftoo high‼\uFE0F" else "❔check❔"}")
}