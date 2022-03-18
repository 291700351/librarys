# 个人自用Android框架


## How to use
```kotlin
    val coreVersion = "last.version"
    implementation("io.github.291700351:core-common:${coreVersion}")
    implementation("io.github.291700351:core-dao:${coreVersion}")
    implementation("io.github.291700351:core-data-store:${coreVersion}")
    implementation("io.github.291700351:core-view-model:${coreVersion}")
    implementation("io.github.291700351:core-databinding-ui:${coreVersion}") {
        exclude("io.github.291700351")
    }
```

## License

    Copyright 2022 291700351

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.