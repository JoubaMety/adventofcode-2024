package main

import (
	"log"
	"os"
	"sort"
	"strconv"
	"strings"
	"unicode"
)

type List2D struct {
	left []int
	right []int
}

func convertInputToList2D(input []byte) List2D {
	f := func(c rune) bool {
		return !unicode.IsNumber(c)
	}
	split := strings.FieldsFunc(string(input), f)
	list := List2D{}
	for i := range split {
		if i % 2 == 0 {
			num, err := strconv.Atoi(split[i])
			if err != nil {
				log.Panicf("Failed to convert string to int: %v", err)
			}
			list.left = append(list.left, num)
		} else {
			num, err := strconv.Atoi(split[i])
			if err != nil {
				log.Panicf("Failed to convert string to int: %v", err)
			}
			list.right = append(list.right, num)
		}
	}
	return list
}
type List2DBySize []int

func (a List2DBySize) Len() int           { return len(a) }
func (a List2DBySize) Swap(i, j int)      { a[i], a[j] = a[j], a[i] }
func (a List2DBySize) Less(i, j int) bool { return a[i] < a[j] }

func sort2DList(input List2D) {
	sort.Sort(List2DBySize(input.left))
	sort.Sort(List2DBySize(input.right))
}

func getAllDistancesFromInput(input []byte) int {
	list := convertInputToList2D(input)
	sort2DList(list)
	left := list.left
	right := list.right
	distance := 0
	for i := range left {
		l := left[i]
		r := right[i]
		if l < r {
			distance += r - l
		} else if l > r {
			distance += l - r
		} else {
			distance += 0
		}
	}
	return distance
}

func getOccurencesOfUniqueNumbersInList(input List2D) map[int]int {
	m := make(map[int]int)
	left := input.left
	right := input.right
	// init
	for i := range left {
		l := left[i]
		m[l] = 0
	}
	// count
	for i := range left {
		l := left[i]
		for j := range right {
			r := right[j]
			if l == r {
				m[l] += 1
			}
		}
	}
	return m
}

func multiplyAndAddOccurences(input map[int]int) int {
	result := 0
	for left := range input {
		right := input[left]
		result += left * right
	}
	return result
}

func getSimilarityScoreFromInput(input []byte) int {
	list := convertInputToList2D(input)
	m := getOccurencesOfUniqueNumbersInList(list)
	return multiplyAndAddOccurences(m)
}

func main() {
	title := "Advent of Code 2024 - Day 01"
	log.Println(title)
	log.Println(strings.Repeat("-", len(title)))
	input, err := os.ReadFile("./01.input.txt")
	if err != nil {
		log.Panicf("Failed to open file: %v", err);
	}
	log.Printf("First part:  %d", getAllDistancesFromInput(input))
	log.Printf("Second part: %d", getSimilarityScoreFromInput(input))

}