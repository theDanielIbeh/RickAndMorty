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

import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Core Koin dependencies are added to every Android module (application or library).
            // "com.android.base" is the common ancestor of both com.android.application and
            // com.android.library, so this block fires for any Android module that applies this plugin.
            // The BOM pins all Koin artifact versions from a single entry in libs.versions.toml.
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    "implementation"(platform(libs.findLibrary("koin.bom").get()))
                    "implementation"(libs.findLibrary("koin.android").get())
                    "implementation"(libs.findLibrary("koin.android.compat").get())
                    "implementation"(libs.findLibrary("koin.core.coroutines").get())
                }
            }

            // Compose-specific Koin dependencies are only added when the Kotlin Compose compiler
            // plugin is present. This plugin is applied exclusively by modules that have a UI layer
            // (e.g. :app), so data/network modules never receive these unnecessary dependencies.
            // Detection via pluginManager.withPlugin() is lazy — it fires only if the plugin
            // is actually applied, and is safe to call even if it never fires.
            pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
                dependencies {
                    "implementation"(libs.findLibrary("koin.compose").get())
                    "implementation"(libs.findLibrary("koin.compose.viewmodel").get())
                    "implementation"(libs.findLibrary("koin.compose.viewmodel.navigation").get())
                    "implementation"(libs.findLibrary("koin.androidx.compose").get())
                    "implementation"(libs.findLibrary("koin.androidx.compose.navigation").get())
                }
            }
        }
    }
}
