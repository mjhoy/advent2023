package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"strconv"
	"strings"
)

func txtFileReader(txtFile string) []string {
	content, error := ioutil.ReadFile(txtFile)
	if error != nil {
		log.Fatal(error)
	}
	contentArray := strings.Split(string(content[:]), "\n")
	return contentArray
}

func parseLines(content string) map[int][]string {
	contentArray := txtFileReader(content)
	result := make(map[int][]string)
	for _, line := range contentArray {
		splitLine := strings.Split(line, ":")
		gameNumber, _ := strconv.Atoi(strings.Split(splitLine[0], " ")[1])
		result[gameNumber] = strings.Split(splitLine[1], ";")
	}
	return result
}

func problemOne(content string) int {
	RED := 12
	GREEN := 13
	BLUE := 14
	var result int = 0
	parsedLines := parseLines(content)
	for gameKey := range parsedLines {

		var toAdd bool = true
		for _, game := range parsedLines[gameKey] {
			var red int = 0
			var green int = 0
			var blue int = 0
			splitLines := strings.Split(game, " ")
			for idx, word := range splitLines {
				if word == "red" || word == "red," {
					redVal, _ := strconv.Atoi(splitLines[idx-1])
					red += redVal
				} else if word == "green" || word == "green," {
					greenVal, _ := strconv.Atoi(splitLines[idx-1])
					green += greenVal
				} else if word == "blue" || word == "blue," {
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

func problemTwo(content string) int {
	var result int = 0
	parsedLines := parseLines(content)
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
			if blueGame > blue {
				blue = blueGame
			}
			if greenGame > green {
				green = greenGame
			}
			if redGame > red {
				red = redGame
			}
		}
		power := red * green * blue
		result += power

	}
	return result
}

func main() {
	fmt.Println(problemTwo("input.txt"))
}
