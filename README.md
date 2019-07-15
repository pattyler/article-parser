# Article Parser - Japanese Article Downloader and Parser
*__Important__: This project is a work in progress, and in the early stages of development. Many things, especially the front-end, have not been designed or mapped out yet. The first step here is setting up a reliable, extendable back-end system, after which the front-end can be designed.*
## About
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

This project came from wanting a system to find sample Japanese sentences, mixed with need to get back up to scratch with Java and related frameworks, and have long been 

As a student of Japanese, many people (myself included) use the [Anki flashcard application](https://apps.ankiweb.net/) to aid vocabulary study. Like many others, I always use example sentences on the flashcard rather than single words. Instead of making my own sentences (which, if done everyday, can be time-consuming), I thought wouldn't it be nice to find real-word example sentences online matching my interests, rather than the often mind-numbingly dull dictionary examples. 

## Use Cases
### Use Case 1 (Beginner student)

- Search NHK EasyNews for a word I wish to remember, such as 登る (noboru - to climb. See above.).

NHK EasyNews is a simplified news site aimed at a younger audience, and as such contains a simpler sentence structure, which is great for beginner learners looking for example sentences. Not every news article is interesting to everybody, so reults could be further refined based on other semantics. (E.g. limit articles to business articles, or articles about hiking.)

### Use Case 2 (Any level)

- Search an online blog for a word I wish to remember, or see in use.

Similar to the use case above, but rather than a news article I'd like to see more casual example sentences. Using the example of 登る, as above, I'd like to search a few mountain-climbing blogs to find example sentences written informally.

### Use Case 3 (Intermediate - Advanced student)

- Search Asahi Shinbun, NHK (full-site), and other newspaper sites for example grammar points, seeing how it is used in various contexts.

In-depth news articles often contain a variety of grammar points, including some complex but important points. I would like to see examples of grammar points used in various contexts, such as ○○ように (~youni, which can be used in a variety of ways.), in order to aid my grammar study.

## Technologies Used

Built with:
- Maven
- Spring Boot
- SQLite

Tested with:
- JUnit
- Mockito
