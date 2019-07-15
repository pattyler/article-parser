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
- ### Use Case 1 (Beginner student)

__Search NHK EasyNews for a word I wish to remember, such as 登る (noboru - to climb. See above.).__

[NHK EasyNews](https://www3.nhk.or.jp/news/easy/) is a simplified news site aimed at a younger audience, and as such contains a simpler sentence structure, which is great for beginner learners looking for example sentences. Not every news article is interesting to everybody, so reults could be further refined based on other semantics. (E.g. limit articles to business articles, or articles about hiking.)

- ### Use Case 2 (Any level)

__Search an online blog for a word I wish to remember, or see in use.__

Similar to the use case above, but rather than a news article I'd like to see more casual example sentences. Using the example of 登る, as above, I'd like to search a few mountain-climbing blogs to find example sentences written informally.

- ### Use Case 3 (Intermediate - Advanced student)

__Search Asahi Shinbun, NHK (full-site), and other newspaper sites for example grammar points, seeing how it is used in various contexts.__

In-depth news articles often contain a variety of grammar points, including some complex but important points. I would like to see examples of grammar points used in various contexts, such as ○○ように (~youni, which can be used in a variety of ways.), in order to aid my grammar study.

## Information for Developers
### Technologies Used

Built with:
- [Maven](https://maven.apache.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [SQLite](https://sqlite.org/index.html)

Tested with:
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

*For a full list of technologies, please consult the project POM files.*

### Project Structure
*__Important__: This is the current project structure, as it currently stands during development of the back-end system. This will be expanded in the future to include other modules or services, such as the database-parsing service to find semantic information within the persisted articles, and the frontend-viewer for use by clients.*
#### Overview
A multi-module Maven project, headed by the artifact `article-parser`:
```
|-- article-parser
|   |-- articler-parser-backend
|   |-- article-parser-grabber
|   |-- article-parser-viewer
```

#### Module Description
##### article-parser
The parent project, containing dependencies and other information common across all modules.

##### article-parser-backend
Contains the backend, database related code common to or shared among other modules. Contains the SQL scripts for setting up the database.

##### article-praser-grabber
Services that retrieve data from websites, parsing them into models provided by `article-parser-backend` in order to be stored in the database. This service should be called periodically, such as from a cron job.

This module does not take care of parsing semantic meaning. It is only concerned with ensuring articles from the websites are downloaded and handed to `article-parser-backend` to be persisted in the database. 

##### article-parser-viewer
Not for production. For use during development for checking the database or retrieving any information you wish programatically.
