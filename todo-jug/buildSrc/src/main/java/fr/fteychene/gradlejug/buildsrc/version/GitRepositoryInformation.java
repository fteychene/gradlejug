package fr.fteychene.gradlejug.buildsrc.version;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods to find build versions from a Git repo.
 */
public class GitRepositoryInformation {

    private final String gitDir;

    public GitRepositoryInformation(String gitDir) {
        this.gitDir = gitDir;
    }

    /**
     * Will search the local Git repo for tags and return a snapshot version or release version based on isRelease.
     *
     * @param releaseTagPattern  a regexp pattern that includes a group that extracts the version number from the tag.
     * @param matchGroup         the number of the group in the releaseTagPattern that matches the version number.
     * @param versionSplitter    the separator in the version number.  Must allow spitting the version number to integers.
     * @param snapShotQuantifier the quantifier for snapshots.
     * @param snapShotQuantifier a string to identify SNAPSHOTS to add to the end of the returned buildVersion.
     * @param isRelease          true for a release (no snapShotQuantifier), false to add the snapShotQuantifier
     * @return
     * @throws java.io.IOException
     * @throws GitAPIException
     */
    public String getBuildVersion(String releaseTagPattern, String matchGroup, String versionSplitter, String snapShotQuantifier, boolean isRelease, String defaultVersion)
            throws IOException, GitAPIException {

        String version = null;

        String latestRelease = getLatestReleaseTag(releaseTagPattern);
        System.out.println("Latest release tag found : "+latestRelease);
        if (latestRelease != null) {
            if (isRelease) {
                version = releaseVersion(
                        latestRelease,
                        releaseTagPattern,
                        matchGroup);
            } else {
                version = nextSnapShotVersion(
                        latestRelease,
                        releaseTagPattern,
                        matchGroup,
                        versionSplitter,
                        snapShotQuantifier);
            }
        } else {
            if (isRelease) {
                version = defaultVersion;
            } else {
                version = defaultVersion + snapShotQuantifier;
            }
        }


        return version;
    }

    public String getCurrentBranchName() throws IOException, GitAPIException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setWorkTree(new File(gitDir))
                .findGitDir()
                .build();
        return repository.getBranch();
    }

    /**
     * Searches a Git repository for annotated tags that match releaseTagPattern and returns the name
     * of the highest tag based on natural sort order.
     * <p>
     * For example given the pattern ^release-\d+\.\d+\.\d+$ and a repo with annotated tags including
     * revision-1.0.0 and revision-1.0.3 it will return revision-1.0.3
     *
     * @param releaseTagPattern a pattern to match the tag name.
     * @return
     * @throws java.io.IOException
     * @throws GitAPIException
     */
    public String getLatestReleaseTag(String releaseTagPattern) throws IOException, GitAPIException {

        Pattern tagPattern = Pattern.compile(releaseTagPattern);

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setWorkTree(new File(gitDir))
                .findGitDir()
                .build();



        RevWalk walk = new RevWalk(repository);
        walk.markStart(walk.parseCommit(repository.resolve("HEAD")));

        List<String> releaseTags = new ArrayList<String>();

        for (Ref ref : new Git(repository).tagList().call()) {
            walk.reset();

            ObjectId objectId = repository.resolve(ref.getName());

            // We only want annotated tags.  It's inefficient to shorten all the
            // tag names but it lets the releaseTagPattern contain end of line
            // anchors (and not have to deal with the preceding refs/tags).
            boolean isTag = false;

            try {
                if (walk.parseTag(objectId) instanceof RevTag) isTag = true;
            } catch (IncorrectObjectTypeException ignored) {
            }

            if (isTag) {
                String tagName = Repository.shortenRefName(ref.getName());
                Matcher releaseTagMatcher = tagPattern.matcher(tagName);
                if (releaseTagMatcher.matches()) {
                    releaseTags.add(tagName);
                }
            }
        }

        repository.close();

        Collections.sort(releaseTags);
        Collections.reverse(releaseTags);

        return releaseTags.size() > 0 ? releaseTags.get(0) : null;
    }

    /**
     * Calculates the next snapshot version.
     * <p>
     * For example, given the releaseTag 'release-0.0.0' the following code will return the next snapshot version
     * '0.0.1-SNAPSHOT':
     * <p>
     * <pre>
     * {@code
     * buildVersion.nextSnapShotVersion("release-0.0.0", "^release-(\\d+\\.\\d+\\.\\d)$", "$1", ".", "-SNAPSHOT"));
     * }
     * </pre>
     *
     * @param releaseTag         the release tag string to calculate the version from.
     * @param releaseTagPattern  a regexp pattern that includes a group that extracts the version number from the tag.
     * @param matchGroup         the number of the group in the releaseTagPattern that matches the version number.
     * @param versionSplitter    the separator in the version number.  Must allow spitting the version number to integers.
     * @param snapShotQuantifier the quantifier for snapshots.
     * @return
     */
    String nextSnapShotVersion(String releaseTag, String releaseTagPattern, String matchGroup, String versionSplitter, String snapShotQuantifier) {
        String version = releaseTag.replaceAll(releaseTagPattern, matchGroup);

        ArrayList<String> versionElement = Lists.newArrayList(Splitter.on(versionSplitter).split(version));

        Integer lastElement = Integer.parseInt(versionElement.remove(versionElement.size() - 1));
        versionElement.add(String.valueOf(lastElement + 1));

        return Joiner.on(versionSplitter).join(versionElement) + snapShotQuantifier;
    }

    /**
     * Calculates the current snapshot version.
     * <p>
     * For example, given the releaseTag 'release-0.0.0' the following code will return the next snapshot version
     * '0.0.1-SNAPSHOT':
     * <p>
     * <pre>
     * {@code
     * buildVersion.nextSnapShotVersion("release-0.0.0", "^release-(\\d+\\.\\d+\\.\\d)$", "$1", ".", "-SNAPSHOT"));
     * }
     * </pre>
     *
     * @param releaseTag         the release tag string to calculate the version from.
     * @param releaseTagPattern  a regexp pattern that includes a group that extracts the version number from the tag.
     * @param matchGroup         the number of the group in the releaseTagPattern that matches the version number.
     * @param versionSplitter    the separator in the version number.  Must allow spitting the version number to integers.
     * @param snapShotQuantifier the quantifier for snapshots.
     * @return
     */
    String currentSnapshotVersion(String releaseTag, String releaseTagPattern, String matchGroup, String versionSplitter, String snapShotQuantifier) {
        String version = releaseTag.replaceAll(releaseTagPattern, matchGroup);

        ArrayList<String> versionElement = Lists.newArrayList(Splitter.on(versionSplitter).split(version));

        return Joiner.on(versionSplitter).join(versionElement) + snapShotQuantifier;
    }


    public String releaseVersion(String releaseTag, String releaseTagPattern, String matchGroup) {
        return releaseTag.replaceAll(releaseTagPattern, matchGroup);
    }

    public String getGitDir() {
        return gitDir;
    }

}
