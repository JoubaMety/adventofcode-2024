import java.io.File

fun convertInputToList(input: String): List<Int> {
    val mutableList : MutableList<Int> = mutableListOf()
    var tempString = ""
    for (char in input) {
        if (char.isDigit()) {
            tempString += char
            continue
        } else if (char == '\n' || char == ' ') {
            if (tempString != "") {
                mutableList.add(tempString.toInt())
                tempString = ""
            }
            continue
        }
    }
    if(tempString != "") mutableList.add(tempString.toInt())
    return mutableList.toList()
}

fun convertListTo2DList(input: List<Int>): List<List<Int>> {
    val firstMutableList : MutableList<Int> = mutableListOf()
    val secondMutableList : MutableList<Int> = mutableListOf()
    for (i in input.indices) {
        if ((i + 1) % 2 == 0) {
            secondMutableList.add(input[i])
        } else {
            firstMutableList.add(input[i])
        }
    }
    return listOf(firstMutableList.toList(), secondMutableList.toList())
}

fun convertInputTo2DList(input: String): List<List<Int>> {
    return convertListTo2DList(convertInputToList(input))
}

fun Sort2DList(input: List<List<Int>>): List<List<Int>> {
    return listOf(input[0].sorted(), input[1].sorted())
}

fun GetAllDistancesFrom2DList(input: List<List<Int>>): Int {
    val sortedList = Sort2DList(input)
    var distance = 0
    for (i in sortedList[0].indices) {
        val left = sortedList[0][i]
        val right = sortedList[1][i]
        distance += if (left < right) {
            right - left
        } else if (left > right) {
            left - right
        } else {
            0
        }
    }
    return distance
}

fun GetUniqueNumbersFrom2DList(input: List<List<Int>>): Map<Int, Int> {
    val mutableMap : MutableMap<Int, Int> = mutableMapOf()

    val leftList = input[0]
    val rightList = input[1]
    // init
    for (number in leftList) {
        mutableMap[number] = 0
    }
    // count
    for (number in leftList) {
        val value = mutableMap[number]!! // I just init'd it, lmfao
        mutableMap[number] = value + rightList.count { it == number }
    }
    return mutableMap.toMap()
}

fun MultiplyUniqueNumbers(input: Map<Int, Int>): Int {
    val mutableList : MutableList<Int> = mutableListOf()
    input.forEach { (key, value) ->
        mutableList.add(key * value)
    }
    var score = 0
    mutableList.forEach { number ->
        score += number
    }
    return score
}

fun getAllDistances(input: String): Int {
    val list = convertInputTo2DList(input)
    val distances = GetAllDistancesFrom2DList(list)
    return distances
}

fun getSimilarityScore(input: String): Int {
    val list = convertInputTo2DList(input)
    val map = GetUniqueNumbersFrom2DList(list)
    val similarityScore = MultiplyUniqueNumbers(map)
    return similarityScore
}

fun main() {
    val title = "Advent Of Code 2024 - Day 01"
    println(title)
    println("-".repeat(title.length))
    val input = File("./src/01.input.txt").readText(Charsets.UTF_8)
    println("First Part: ${getAllDistances(input)}")
    println("Second Part: ${getSimilarityScore(input)}")
}