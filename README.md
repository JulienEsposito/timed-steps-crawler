# timed-steps-crawler


A small Java app that:

* Starts from a URL and crawls links up to a max number of steps
* Extracts title/headings/body text and assigns top-K keywords per page
* Streams each visited page + keywords while running
* Prints Top keywords per step and a final crawl summary


## Requirements

* Java 17+
* Maven

## Build

```bash
mvn clean package -DskipTests
```

## Run

You can use positional args or named flags. Defaults are shown in brackets.

### Positional

```bash
java -jar target/timed-steps-crawler-0.1.0.jar ["https://en.wikipedia.org/wiki/Open-source_intelligence"] [depth=2] [minutes=5] [keywords=3]
```

### Named flags

```bash
java -jar target/word-hops-0.1.0.jar \
  --url "<start url>" \
  --depth <max steps> \
  --minutes <time limit in minutes> \
  --keywords <top K per page>
```

### Examples

```bash
java -jar target\timed-steps-crawler-0.1.0.jar
```


```bash
java -jar target\timed-steps-crawler-0.1.0.jar --url "https://github.com/JulienEsposito" --step 4 --minutes 1 --keywords 2
```