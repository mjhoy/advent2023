// Creator: david
// Date: 2023-12-2
// Purpose: day 2 of advent of code 2023

package day_002

import (
	"david/src/utils"
	"fmt"
	"strconv"
	"strings"
)

func ParseLines(content string) map[int][]string {
	contentArray := utils.TxtFileReader(content)
	result := make(map[int][]string)
	for _, line := range contentArray {
		splitLine := strings.Split(line, ":")
		gameNumber, _ := strconv.Atoi(strings.Split(splitLine[0], " ")[1])
		result[gameNumber] = strings.Split(splitLine[1], ";")
	}
	return result
}

func ProblemOne(content string) int {
	RED := 12
	GREEN := 13
	BLUE := 14
	var result int = 0
	parsedLines := ParseLines(content)
	for gameKey := range parsedLines {

		var toAdd bool = true
		for _, game := range parsedLines[gameKey] {
			var red int = 0
			var green int = 0
			var blue int = 0
			splitLines := strings.Split(game, " ")
			for idx, word := range splitLines {
				if strings.Contains(word, "red") {
					redVal, _ := strconv.Atoi(splitLines[idx-1])
					red += redVal
				} else if strings.Contains(word, "green") {
					greenVal, _ := strconv.Atoi(splitLines[idx-1])
					green += greenVal
				} else if strings.Contains(word, "blue") {
					blueVal, _ := strconv.Atoi(splitLines[idx-1])
					blue += blueVal
				}
			}
			if red > RED || green > GREEN || blue > BLUE {
				toAdd = false
			}
		}
		if toAdd {
			result += gameKey
		}
	}
	return result
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func ProblemTwo(content string) int {
	var result int = 0
	parsedLines := ParseLines(content)
	for gameKey := range parsedLines {
		var red int = 0
		var green int = 0
		var blue int = 0
		for _, game := range parsedLines[gameKey] {
			var redGame int = 0
			var greenGame int = 0
			var blueGame int = 0
			splitLines := strings.Split(game, " ")
			for idx, word := range splitLines {
				if word == "red" || word == "red," {
					redVal, _ := strconv.Atoi(splitLines[idx-1])
					redGame += redVal
				} else if word == "green" || word == "green," {
					greenVal, _ := strconv.Atoi(splitLines[idx-1])
					greenGame += greenVal
				} else if word == "blue" || word == "blue," {
					blueVal, _ := strconv.Atoi(splitLines[idx-1])
					blueGame += blueVal
				}
			}
			red = max(red, redGame)
			green = max(green, greenGame)
			blue = max(blue, blueGame)
		}
		power := red * green * blue
		result += power

	}
	return result
}

func main() {
	fmt.Println(ProblemOne("sample.txt"))
	fmt.Println(ProblemTwo("sample.txt"))
}
