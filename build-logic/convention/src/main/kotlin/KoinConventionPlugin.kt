/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.gradle.api.AndroidBasePlugin
import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            /** Add support for Android modules, based on [AndroidBasePlugin] */
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    "implementation"(platform(libs.findLibrary("koin.bom").get()))
                    "implementation"(libs.findLibrary("koin.android").get())
                    "implementation"(libs.findLibrary("koin.android.compat").get())
                    "implementation"(libs.findLibrary("koin.core.coroutines").get())
                    "implementation"(libs.findLibrary("koin.compose").get())
                    "implementation"(libs.findLibrary("koin.compose.viewmodel").get())
                    "implementation"(libs.findLibrary("koin.compose.viewmodel.navigation").get())
                    "implementation"(libs.findLibrary("koin.androidx.compose").get())
                    "implementation"(libs.findLibrary("koin.androidx.compose.navigation").get())
                    "implementation"(libs.findLibrary("koin.androidx.workmanager").get())
                }
            }
        }
    }
}
