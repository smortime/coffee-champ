[![CircleCI](https://circleci.com/gh/smortime/coffee-champ.svg?style=svg)](https://circleci.com/gh/smortime/coffee-champ)
# Coffee Champ
> Coffee recommendations are just a click away.

Coffee Champ is an app that recommends regions to try coffee from based off your tastes preferences. I mainly started this app so I would have an excuse to experiment with new tech stacks / tools and get hands on experience with end to end development and deployment of a web app.

## Dependencies
* [Scala 2.12.8](https://www.scala-lang.org/)
* [Akka](https://akka.io/docs/)
* [Akka HTTP](https://doc.akka.io/docs/akka-http/current/index.html)

## Developing on Local

OS X & Linux:

Generate a PKCS12 cert and place it in `src/main/resources/keys`. For local development a self-signed cert works fine.

To run the service:

```bash
sbt docker:publishLocal
docker run -e HOST=0.0.0.0 -e PORT=8080 -e CERT_PASSWORD=[UPDATE PASSWORD IF HAVE ONE] -p 8080:8080  coffee-champ:0.1
```

To view web page just navigate to location of `coffee-champ/web/index.htnl` in browser of choice.

To run tests:
```bash
sbt test
```


## Release History

* 0.2
    * Circle CI deployment setup
    * App hosted on DigitalOcean droplet
* 0.1
    * Work in progress

## Planned Development
* Implement collaborative filtering with Spark ML
* Upgrade UI...

## Learning Goals
* Become comfortable with actor model and Scala
* Practice TDD and build out CI/CD workflows
* Experiment with Spark ML library

## Project Owner

* Schuyler Mortimer – schuymortimer@gmail.com

## Special Thanks

* Kaolin Hart - UI/UX Consultant 
* Tho Ha - Coffee SME

