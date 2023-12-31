// Creator: david
// Date: 2023-12-1
// Purpose: day 1 of advent of code 2023

package day_001

import (
	"david/src/utils"
	"fmt"
	"log"
	"regexp"
	"strconv"
	"strings"
)

var NUMBERS = map[string]int{
	"zero": 0, "one": 1, "two": 2, "three": 3, "four": 4, "five": 5,
	"six": 6, "seven": 7, "eight": 8, "nine": 9, "ten": 10,
	"eleven": 11, "twelve": 12, "thirteen": 13, "fourteen": 14, "fifteen": 15,
	"sixteen": 16, "seventeen": 17, "eighteen": 18, "nineteen": 19,
	"twenty": 20, "thirty": 30, "forty": 40, "fifty": 50,
	"sixty": 60, "seventy": 70, "eighty": 80, "ninety": 90,
}

// read txt file and return array of strings

// reverse a string
func reverse(s string) string {
	runes := []rune(s)
	for i, j := 0, len(runes)-1; i < j; i, j = i+1, j-1 {
		runes[i], runes[j] = runes[j], runes[i]
	}
	return string(runes)
}

// convenience function to get first or last int from a string
func GetSingleInt(intChars int, toReturn string) int {
	var idx int = 0
	if toReturn == "last" {
		idx = len(fmt.Sprint(intChars)) - 1
	} else if toReturn != "first" {
		log.Fatal("toReturn must be either 'first' or 'last'")
	}
	resByte := fmt.Sprint(intChars)[idx]
	result, _ := strconv.Atoi(string(resByte))
	return result
}

// sum first and last ints in each line of txt file
func SumFirstLastInts(content string) int {
	contentArray := utils.TxtFileReader(content)
	var res int
	for _, line := range contentArray {
		var temp string
		re := regexp.MustCompile(`\d+`) // find all ints in string
		strs := re.FindAllString(line, -1)
		first, _ := strconv.Atoi(fmt.Sprint(strs[0]))
		temp += fmt.Sprint(GetSingleInt(first, "first")) // get single integer from larger possible number
		last, _ := strconv.Atoi(strs[len(strs)-1])
		temp += fmt.Sprint(GetSingleInt(last, "last"))
		tempInt, _ := strconv.Atoi(temp)
		res += tempInt
	}
	return res
}

// sum first and last ints in each line of txt file, including ints in strings
func SumFirstLastIntsWithStr(content string) int {
	contentArray := utils.TxtFileReader(content)
	var res int
	for _, line := range contentArray {
		reversed := reverse(line)
		var temp string
		var firstInt int
		var lastInt int
		var firstIdx = 99999
		var lastIdx = 99999
		re := regexp.MustCompile(`\d+`) // find all ints in string
		strs := re.FindAllString(line, -1)
		if len(strs) != 0 {
			first, _ := strconv.Atoi(fmt.Sprint(strs[0]))
			firstInt = GetSingleInt(first, "first")
			last, _ := strconv.Atoi(strs[len(strs)-1])
			lastInt = GetSingleInt(last, "last")
			firstIdx = strings.Index(line, fmt.Sprint(firstInt))
			lastIdx = strings.Index(reversed, fmt.Sprint(lastInt))
		}
		// create an empty map with all numbers as keys
		for key := range NUMBERS {
			idx := strings.Index(line, key)
			if key == "zero" || idx == -1 {
				continue
			} else if idx < firstIdx {
				firstInt = GetSingleInt(NUMBERS[key], "first")
				firstIdx = idx
			}
		}
		for key := range NUMBERS {
			reverseIdx := strings.Index(reversed, reverse(key))
			if reverseIdx == -1 {
				continue
			} else if reverseIdx < lastIdx {
				lastInt = GetSingleInt(NUMBERS[key], "last")
				lastIdx = reverseIdx
			}
		}
		temp += fmt.Sprint(firstInt)
		temp += fmt.Sprint(lastInt)
		tempInt, _ := strconv.Atoi(temp)
		res += tempInt
	}
	return res
}

// read input txt file
func main() {
	//part 1
	fmt.Println(SumFirstLastInts("sample.txt"))
	fmt.Println(SumFirstLastIntsWithStr("sample2.txt"))
}
