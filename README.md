# quarkus-github-user-repos

This project uses Quarkus with Kotlin to create a service, where you can query a list of repositories of a user on
GitHub.

## Structure

`/user/{username}`
Provided a valid username, returns a list of repositories and branches of that repositories

Schema:

```json
[
  {
    "name": "name of the repo",
    "ownerLogin": "username of the owner",
    "branches": [
      {
        "name": "branch name",
        "sha": "sha of the latest commit"
      }
    ]
  }
]
```

Returns `404 Not found` if the specified user is not found.

Only `Accept: application/json` is valid, anything else returns error `406 Not acceptable`.

## Running the application

Please specify a `GITHUB_TOKEN` env variable for increased rate limits, for example with a `.env` file in the root the
project. If not specified, GitHub applies strict rate limits.

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.
