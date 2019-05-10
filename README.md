# Dumpert-Scraper
Dumpert scraper API in Kotlin

This repository contains a scraper library that can be used to parse comment pages from Dumpert.

### Example scraper
```
val scraper = Scraper<Comments>() // create a new scraper
val requests = listOf(CommentsPageParser(pageId)) // parse comments from the page with the specified pageId
val results = scraper.scrape(COMMENTS_BASE_URL, requests) // load the scraping results
```

### Example result sorting
```
results.forEach { it.sortByKudos() } // sorts results by descending kudos
```

### Example serialization
```
Serializer.serialize(pageId.replace("/", "_"), results) // serializes the results and saves it to a file
```
## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Kotlin](https://kotlinlang.org/) - Programming language used
* [Jsoup](https://jsoup.org/) - Used to parse web pages

## Authors

* **Stan van der Bend** - *Initial work* - [dosier](https://github.com/dosier)
