# Recipe App

[![Build Status](https://travis-ci.org/CMPUT301W13T06/classproject.png)](https://travis-ci.org/CMPUT301W13T06/classproject)

This repository contains the source code for the [RecipeBot](https://github.com/CMPUT301W13T06/classproject)

Please see the [issues](https://github.com/CMPUT301W13T06/classproject/issues) section
to report any bugs or feature requests and to see the list of known issues.

## License
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2013 Adam Saturna
Copyright 2013 Brian Trinh
Copyright 2013 Ethan Mykytiuk
Copyright 2013 Prateek Srivastava (@f2prateek)

Copyright 2012 Google Inc.
Copyright 2012 Donn Felker
Copyright 2012 GitHub Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Building

The build requires [Maven](http://maven.apache.org/download.html) v3.0.3+ and the [Android SDK](http://developer.android.com/sdk/index.html) to be installed in your development environment. In addition you'll need to set the `ANDROID_HOME` environment variable to the location of your SDK:

`export ANDROID_HOME=~/tools/android-sdk`

After satisfying those requirements, the build is pretty simple:
(a connected device is required)

* Run `mvn clean package` from the `app` directory to build the APK only
* Run `mvn clean install` from the root directory to build the app and also run the integration tests, this requires a connected Android device or running emulator
* Run `mvn clean verify` from the root directory to build the app, run the integration tests, and run checkstyle
* Run ./test-cli.sh, to build the app, run integration tests and view the output in a web browser
* Run ./test-cli-windows.sh (on Windows), to build the app, run integration tests and view the output in a web browser

## Acknowledgements

  * [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock) for a consistent, great looking header across all Android platforms.
  * [ViewPagerIndicator](https://github.com/JakeWharton/Android-ViewPagerIndicator) for swiping between fragments.
  * [NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids) for view animations.
  * [RoboGuice](http://code.google.com/p/roboguice/) for dependency-injection.
  * [Robotium](http://code.google.com/p/robotium/) for driving our app during integration tests.
  * [android-maven-plugin](https://github.com/jayway/maven-android-plugin) for automating our build and producing release-ready APKs.
  * [Otto](https://github.com/square/otto) - An event bus for publish subscribe style communication between application components
  * [fest-android](https://github.com/square/fest-android) - For fluent assertions
  * [AndroidBootstrap](http://www.androidbootstrap.com/) - for generating the template of our source code.
  * [Travis-CI](https://travis-ci.org/f2prateek/FoodBot) for continuous integration
  * [http-request](https://github.com/kevinsawicki/http-request) A simple convenience library for using a HttpURLConnection.
  * [Spoon](http://square.github.com/spoon/) - for making sense of our instrumentation tests

## Contributing
Please fork this repository and contribute back using [pull requests](https://github.com/CMPUT301W13T06/classproject/pulls).
