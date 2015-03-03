package fr.fteychene.gradlejug.buildsrc.version

/**
 * Created by fteychen on 23/02/2015.
 */
class ProjectVersion {

    String version
    String buildNumber

    ProjectVersion(String aVersion, String aBuildNumber) {
        version = aVersion
        buildNumber = aBuildNumber
    }

    String getProjectVersion() {
        String fullVersion = "$version"

        if (buildNumber) {
            fullVersion += ".$buildNumber"
        }

        fullVersion
    }

}
