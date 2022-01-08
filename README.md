[![Visits Badge](https://badges.pufler.dev/visits/torrescalazans/github-client-android)](https://badges.pufler.dev)

![GitHub](https://img.shields.io/github/license/torrescalazans/github-client-android)
![GitHub language count](https://img.shields.io/github/languages/count/torrescalazans/github-client-android)
![GitHub top language](https://img.shields.io/github/languages/top/torrescalazans/github-client-android)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/torrescalazans/github-client-android)
![GitHub repo size](https://img.shields.io/github/repo-size/torrescalazans/github-client-android)
![GitHub all releases](https://img.shields.io/github/downloads/torrescalazans/github-client-android/total)

# Android - MVVM with Clean Architecture: GitHub client sample

GitHub client sample project encompasses best practices and recommended architecture for building robust, high-quality Android mobile apps. It's following [MVVM](https://en.wikipedia.org/wiki/Model_View_ViewModel) as architectural pattern. Moreover, adds the [Clean Architecture](http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) concepts to separate domain logic.

The code for this project was mainly based on [Android Paging](https://developer.android.com/codelabs/android-paging) Google's codelab that demonstrates the usage of Android Jetpack Paging library. Moreover, adds some improvements like Dependecy Injection and Clean Architecture concepts.

MVVM with Clean Architecture overview:
![image info](https://uploads.toptal.io/blog/image/127608/toptal-blog-image-1543413671794-80993a19fea97477524763c908b50a7a.png)
Source: https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture

GitHub client features:
* GitHub repositories list based on searching criteria
* Repository details (web-based content)
* Infinite scrolling
* Network error handling

Expected flows:
<p float="left">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_searching-01.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_searching-02.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/repository-details.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_no-results-found.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_confirm-exit.png" width="180">
</p>

Exception flows:
<p float="left">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_network-error-handling-01.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/home-repositories-list_network-error-handling-02.png" width="180">
<img src="https://torrescalazans.github.io/projects/github-client-android/docs/assets/repository-details_network-error-handling.png" width="180">
</p>

Setup
-------

Step 1: Clone the repository in order to create your own forks:
```shell
$ git clone https://github.com/torrescalazans/github-client-android.git
```

Step 2: Put the `GITHUB_BASE_URL` into `local.properties` file:
```groovy
# GitHub base url
GITHUB_BASE_URL="https://api.github.com/"
```
Note:

It's a simple approach for storing secret keys and keep them not checked into source controls. You can extend this project and add your own secret keys based on your api authentication needs.

It's worth mentioning that add your secret keys into the code is not a proper secure way to store and protect them. Any sensitive information must be kept and maintained as part of backend security services.

Step 3: Build project on Linux or Mac (for Windowns use _gradlew build_):
```shell
$ ./gradlew build
```
Note:

Lint checks must be addressed in order to avoid build failure once warningsAsErrors, abortOnError and checkReleaseBuilds are set as true.

Libraries and components
-------

### Kotlin libraries

* [Kotlin coroutines](https://github.com/Kotlin/kotlinx.coroutines)

    kotlinx.coroutines is a rich library for coroutines developed by JetBrains. It contains a number of high-level coroutine-enabled primitives that this guide covers, including launch, async and others.

* [Kotlin coroutines with lifecycle-aware components - KTX dependencies](https://developer.android.com/jetpack)

    Kotlin coroutines provide an API that enables you to write asynchronous code. With Kotlin coroutines, you can define a CoroutineScope, which helps you to manage when your coroutines should run. Each asynchronous operation runs within a particular scope.

* [Kotlin coroutines flow](https://developer.android.com/kotlin/flow)

    In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value. For example, you can use a flow to receive live updates from a database or network.

### Android Jetpack

* [Android Jetpack](https://developer.android.com/jetpack)

    Jetpack is a suite of libraries to help developers follow best practices, reduce boilerplate code, and write code that works consistently across Android versions and devices so that developers can focus on the code they care about.

    * [View Binding](https://developer.android.com/topic/libraries/view-binding)

        View binding is a feature that allows you to more easily write code that interacts with views. Once view binding is enabled in a module, it generates a binding class for each XML layout file present in that module. An instance of a binding class contains direct references to all views that have an ID in the corresponding layout..

    * [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)

        Lifecycle-aware components perform actions in response to a change in the lifecycle status of another component, such as activities and fragments. These components help you produce better-organized, and often lighter-weight code, that is easier to maintain.

    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

        The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way. The ViewModel class allows data to survive configuration changes such as screen rotations.

    * [Saved State module for ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate)

        When using this module, ViewModel objects receive a SavedStateHandle object through its constructor. This object is a key-value map that lets you write and retrieve objects to and from the saved state. These values persist after the process is killed by the system and remain available through the same object.

    * [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

        The Paging library helps you load and display pages of data from a larger dataset from local storage or over network. This approach allows your app to use both network bandwidth and system resources more efficiently. The components of the Paging library are designed to fit into the recommended Android app architecture, integrate cleanly with other Jetpack components, and provide first-class Kotlin support.

    * [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)

        Navigation is a framework for navigating between 'destinations' within an Android application that provides a consistent API whether destinations are implemented as Fragments, Activities, or other components.

### Networking

* [Retrofit](https://github.com/square/retrofit)

    A type-safe HTTP client for Android and Java.

* [OkHttp](https://github.com/square/okhttp)

    Using OkHttp is easy. Its request/response API is designed with fluent builders and immutability. It supports both synchronous blocking calls and async calls with callbacks.

### Serialization

* [Moshi](https://github.com/square/moshi)

    Moshi is a modern JSON library for Android, Java and Kotlin. It makes it easy to parse JSON into Java and Kotlin classes.

### Dependecy injection

* [Dagger-hilt: Dependency injection with Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

    Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.

### Imaging

* [Glide](https://github.com/bumptech/glide)

    Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.

### Testing

* [JUnit4](https://junit.org/junit4/)

    JUnit is a simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.

* [Truth - Fluent assertions for Java and Android](https://truth.dev/)

    Truth is owned and maintained by the Guava team. It is used in the majority of the tests in Google’s own codebase.

* [kotlinx-coroutines-test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)

    This package provides utilities for efficiently testing coroutines.

* [Espresso](https://developer.android.com/jetpack/androidx/releases/test#espresso)

    Use Espresso to write concise, beautiful, and reliable Android UI tests.

* [Robolectric](http://robolectric.org/)

    Robolectric is a framework that brings fast and reliable unit tests to Android. Tests run inside the JVM on your workstation in seconds.

* [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)

    A scriptable web server for testing HTTP clients.

### Logging

* [Timber](https://github.com/JakeWharton/timber)

    This is a logger with a small, extensible API which provides utility on top of Android's normal Log class.

* [Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)

    An OkHttp interceptor which logs HTTP request and response data.

Android Studio plugin
-------
* [JSON To Kotlin Class ​(JsonToKotlinClass)](https://plugins.jetbrains.com/plugin/9960-json-to-kotlin-class-jsontokotlinclass-)

    Plugin for Kotlin to convert Json String into Kotlin data class code quickly.

Developed by
-------

José Torres Calazans Júnior

[![LinkedIn Badge](https://img.shields.io/badge/LinkedIn-Profile-informational?style=flat&logo=linkedin&logoColor=white&color=0D76A8)](https://www.linkedin.com/in/josetorrescalazansjunior/)

License
-------

    Copyright 2021 José Torres Calazans Júnior

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
