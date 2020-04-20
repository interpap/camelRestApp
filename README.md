# CAMEL REST Microservice (Java+Camel+Spring+Maven)

This is a REST Microservice, used for listing the languages used by the 100 trending public repos on GitHub.
For every language listed👇:

	-- Number of repos using this language
	-- The list of repos using the language

## Get 100 Trending Repos from Github using Github API v3

Fetching trending repositories simply translates to fetching the most starred repos created in the last 30 days ( from now ). To do that, you'll need to call the following endpoint:

```
https://api.github.com/search/repositories?q=created:>{date}&sort=stars&order=desc&per_page=100
```

The JSON data from Github will be paginated (you'll receive around 100 repos per JSON page). You can ignore the subsequent pages since you only need the first 100 repositories.

## How do I run this example?

This project can be built with

```
mvn clean install
```

##Running this example on your machine

You can also run this quickstart as a standalone project directly

Obtain the project and enter the project's directory then build the project:

```
$ mvn clean package
```

And run the following Maven goal:

```
$ mvn spring-boot:run 
```

Or just run as java application .


## Endpoint

 Get repositories by languages:

```
localhost:8080/getReposByLanguages

```

## Built With

* [Spring Boot/Security](http://www.dropwizard.io/1.0.2/docs/) - The web framework used for inversion of control container for the Java platform
* [Maven](https://maven.apache.org/) - Dependency Management
* [CAMEL](https://camel.apache.org/manual/latest/index.html) - Camel empowers you to define routing and mediation rules in a variety of domain-specific languages


## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/interpap/camelRestApp/tags). 

## Authors

* **Achraf KADDOURI** - *Initial work* - [Interpap](https://github.com/interpap)



