/*
 * Copyright (c) 2019. Trevor Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trevjonez.agrp

import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidGithubReleasePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.pluginManager.withPlugin("com.android.library") {
      project.pluginManager.apply(AgrpLibraryPlugin::class.java)
    }

    project.pluginManager.withPlugin("com.android.application") {
      project.pluginManager.apply(AgrpApplicationPlugin::class.java)
    }

    project.afterEvaluate {
      project.extensions.findByName("AndroidGithubRelease")
          ?: project.tasks.find { it is AgrpTask }
          ?: project.missingPlugin<AndroidBasePlugin>(genericExceptionMessage)
    }
  }

  private val genericExceptionMessage =
      """If you believe this is an issue or missing feature, please consider opening an issue on github.
      |https://github.com/trevjonez/AndroidGithubReleasePlugin
      |""".trimMargin()

  private inline fun <reified T : Plugin<Project>> Project.missingPlugin(msg: String = ""): Nothing {
    throw MissingPluginException(
      """Failed to configure ${AndroidGithubReleasePlugin::class.java.name} plugin on project: $path
          |  Expected plugin: `${T::class.java.name}` was not applied.
          |  $msg""".trimMargin()
    )
  }

  class MissingPluginException(message: String) : GradleException(message)
}
