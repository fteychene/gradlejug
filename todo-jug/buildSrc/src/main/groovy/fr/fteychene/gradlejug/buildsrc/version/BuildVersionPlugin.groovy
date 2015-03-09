package fr.fteychene.gradlejug.buildsrc.version

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildVersionPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.configure(project) {
            BuildVersionExtension extension = extensions.create("buildVersion", BuildVersionExtension)
            extension.setGitVersion(new GitRepositoryInformation(project.getRootProject().projectDir.absolutePath))
        }

        if (System.getProperty("isRelease") && "true".equals(System.getProperty("isRelease"))) {
            project.allprojects {
                buildVersion {
                    isRelease = true
                }
            }
        }
    }
}
