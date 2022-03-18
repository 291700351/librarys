buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
}

rootProject.group = "io.github.291700351"
rootProject.version = "1.0.1"
rootProject.description = "预发布1.0.1版本"

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

subprojects {
    beforeEvaluate {
        ext.set("compileSdk", 32)
        ext.set("minSdk", 21)
        ext.set("targetSdk", 32)
    }
    afterEvaluate {
        if (!pluginManager.hasPlugin("com.android.library")) {
            println("Project $name is not an android library project, Skip...")
            return@afterEvaluate
        }
        //配置android相关属性和上传maven配置
        configAndroidLibrary(this) {

        }
        //库基础依赖
        dependencies {
            add(
                "implementation",
                "androidx.core:core-ktx:1.7.0"
            )
            add(
                "implementation",
                "androidx.appcompat:appcompat:1.3.1"
            )
            add(
                "implementation",
                "com.google.android.material:material:1.5.0"
            )
            add(
                "testImplementation",
                "junit:junit:4.13.2"
            )
            add(
                "androidTestImplementation",
                "androidx.test.ext:junit:1.1.3"
            )
            add(
                "androidTestImplementation",
                "androidx.test.espresso:espresso-core:3.4.0"
            )
        }
    }
}

inline fun <reified T : com.android.build.gradle.BaseExtension> configAndroidBase(
    androidProject: Project,
    crossinline block: T.() -> Unit = {}
) {
    androidProject.extensions.configure<T>("android") {
        setCompileSdkVersion(32)
        defaultConfig {
            minSdk = 21
            targetSdk = 32
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        block()
    }
}

fun configAndroidLibrary(
    libraryProject: Project,
    buildConfig: Boolean = false,
    block: com.android.build.gradle.LibraryExtension.() -> Unit = {}
) {
    configAndroidBase<com.android.build.gradle.LibraryExtension>(libraryProject) {
        libraryVariants.all {
            generateBuildConfigProvider?.configure { enabled = buildConfig }
        }
        buildFeatures {
            dataBinding = true
        }
        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
                consumerProguardFiles("proguard-rules.pro")
                isJniDebuggable = true
                isRenderscriptDebuggable = true
            }
            getByName("release") {
                isMinifyEnabled = false
                consumerProguardFiles("proguard-rules.pro")
                isJniDebuggable = false
                isRenderscriptDebuggable = false
            }
        }
        block()
    }

    libraryProject.afterEvaluate {
        val srcDirs = extensions.getByType(com.android.build.gradle.LibraryExtension::class.java)
            .sourceSets
            .getByName("main")
            .java.srcDirs
        loadSelfPropertiesToProperty(this)

        //添加上传maven插件
        pluginManager.apply("maven-publish")
        pluginManager.apply("signing")
        //定义一个生成源码jar包的task
        tasks.create<Jar>("androidSourcesJar") {
            archiveClassifier.set("sources")
            exclude("**/R.class", "**/BuildConfig.class")
            from(srcDirs)
        }

        //创建上传配置
        extensions.configure<PublishingExtension> {
            publications.create<MavenPublication>(
                "${replaceName(name)}Release"
            ) {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = rootProject.version.toString()
                artifact("$buildDir/outputs/aar/${project.name}-release.aar")
                artifact(tasks.getByName("androidSourcesJar"))

                pom {
                    name.set(project.name)
                    description.set(project.name)
                    url.set("https://github.com/291700351")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("291700351")
                            name.set("Ice Lee")
                            email.set("lb291700351@live.cn")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/291700351/librarys.git")
                        developerConnection.set("scm:git:ssh://github.com/291700351/librarys.git")
                        url.set("https://github.com/291700351/librarys/tree/master/${project.name}")
                    }
                    withXml {
                        val dependenciesNode = asNode().appendNode("dependencies")
                        project.configurations.getByName("implementation")
                            .allDependencies.forEach { dependency ->
                                val dependencyNode =
                                    dependenciesNode.appendNode("dependency")
                                dependencyNode.appendNode(
                                    "groupId",
                                    dependency.group
                                )
                                dependencyNode.appendNode(
                                    "artifactId",
                                    dependency.name
                                )
                                dependencyNode.appendNode(
                                    "version",
                                    dependency.version
                                )
                            }
                    }
                }

                repositories {
                    maven {
                        name = "MavenCentral"
                        val mavenUrl =
                            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"

                        this.url = uri(mavenUrl)
                        if (project.hasProperty("maven.username")
                            || project.hasProperty("maven.password")
                        ) {
                            credentials {
                                username = project.properties["maven.username"].toString()
                                password = project.properties["maven.password"].toString()
                            }
                        }
                    }
                }

                extensions.configure<SigningExtension>("signing") {
                    if (project.hasProperty("signing.keyId")
                        && project.hasProperty("signing.password")
                        && project.hasProperty("signing.secretKeyRingFile")
                    ) {
                        sign(extensions.getByType(PublishingExtension::class.java).publications)
                    }
                }

                //关联任务链
                tasks.getByName("publish${replaceName(project.name)}ReleasePublicationToMavenCentralRepository") {
                    dependsOn.add(tasks.getByName("assembleRelease"))
                }
                tasks.getByName("publish${replaceName(project.name)}ReleasePublicationToMavenLocal") {
                    dependsOn.add(tasks.getByName("assembleRelease"))
                }

            }

        }
    }


}


fun loadSelfPropertiesToProperty(project: Project) {
    var secretPropsFile = project.file("./local.properties")
    if (!secretPropsFile.exists() && !secretPropsFile.isFile) {
        secretPropsFile = project.rootProject.file("local.properties")
    }

    val p = java.util.Properties()
    if (secretPropsFile.exists() && secretPropsFile.isFile) {
        p.load(java.io.FileInputStream(secretPropsFile))
        p.forEach { name, value ->
            project.extensions.configure<ExtraPropertiesExtension> {
                this[name.toString()] = value
            }
        }
    }
}

fun replaceName(name: String): String {
    val sb = StringBuilder()
    name.split("-").forEach {
        sb.append(
            it.substring(0, 1).toUpperCase(java.util.Locale.ROOT)
                .plus(it.substring(1))
        )

    }
    return sb.toString()
}