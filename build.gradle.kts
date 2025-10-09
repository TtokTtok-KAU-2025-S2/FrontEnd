// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    // ✅ 플러그인 classpath에도 javapoet 1.13.0을 올려서 선점
    dependencies {
        classpath("com.squareup:javapoet:1.13.0")
    }
    // ✅ 혹시 다른 플러그인이 낮은 버전을 끌어와도 덮어쓰기
    configurations.classpath {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
            eachDependency {
                if (requested.group == "com.squareup" && requested.name == "javapoet") {
                    useVersion("1.13.0")
                }
            }
        }
    }
}

// (권장) 모든 모듈/구성에도 같은 고정 적용
allprojects {
    configurations.configureEach {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
            eachDependency {
                if (requested.group == "com.squareup" && requested.name == "javapoet") {
                    useVersion("1.13.0")
                }
            }
        }
    }
}