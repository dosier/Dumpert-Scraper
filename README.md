# Dumpert-Scraper
This repository contains a scraper library that can be used to scraper Dumpert comment sections.

### Example scraper
```kotlin
// create a new Scraper
val scraper = Scraper<Comments>()

// create a list of page parsers
val parsers = listOf(CommentsPageParser(pageId))

// scrape the listed pages and fetch the results
val results = scraper.scrape(COMMENTS_BASE_URL, parsers)
```

### Example result sorting
```kotlin
// sorts results by descending kudos
results.forEach { it.sortByKudos() } 
```

### Example serialization
```kotlin
// serializes the results and saves it to a file
Serializer.serialize(pageId.replace("/", "_"), results) 
```

### Example result interpreting (post scraping)
```kotlin
val pageIds = ArrayList<String>()

// load all dumps from pages 1 to 10 (exclusive)
Dumps.loadRange(1, 10).forEach { pageIds.addAll(it.getPageIds()) }

val comments = ArrayList<Comments.Comment>()

// load all comments from all dumps
Comments.load(*pageIds.toTypedArray()).forEach { comments.addAll(it.comments) }

// print users with the most cumulative kudos spanning all comments
printTopUsers(comments)
```

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Kotlin](https://kotlinlang.org/) - Programming language used
* [Jsoup](https://jsoup.org/) - Used to parse web pages

## Authors

* **Stan van der Bend** - *Initial work* - [dosier](https://github.com/dosier)
