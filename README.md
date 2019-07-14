## Article Parser
A simple system for downloading and parsing Japanese news articles to aid in learning Japanese. 

Article Parser retrieves articles from a list of sources, parsing their content into a searchable database, allowing users to find real-word, example
sentences of words they are studying. For example:

```
Word:     登る
Results:  1.  山梨県から登る道は１日にオープンしましたが、崩れた石で３４５０ｍより上に登ることができませんでした。
          2.  夏さんは１４日、世界でいちばん高いエベレストの頂上に登ることに成功しました。
                 etc.  
                    .
                    .
```

## Motivation

I created this project as I needed to get back up to scratch with Java and related frameworks, and have long been wanting a system for finding 
sample Japanese sentences. 

As a student of Japanese, I use the Anki flashcard application to input new words, and I always use example sentences
on the flashcard. Instead of making my own sentences (which, if done everyday, can be time-consuming), I'd like to find example sentences
from various sources that match my interests, rather than the often mind-numbingly dull dictionary examples. For example, I'd like to
search NHK EasyNews for new words, such as 登る (noboru - to climb. See above.), or perhaps the full-blow NHK site or Asahi Shinbun for grammar points,
looking to see how a grammar point is used in various contexts, such as ○○ように (~youni), which can be used in a variety of ways.

## Tech / Framework
Built with:
- Maven
- Spring Boot

Tested with:
- JUnit
- Mockito
