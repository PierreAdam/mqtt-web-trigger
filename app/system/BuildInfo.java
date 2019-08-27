/*
 * Copyright (c) 2019 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package system;

/**
 * BuildInfo. Provides abstraction to access Scala
 * auto-generated object from Java.
 *
 * @author Pierre Adam
 * @since 19.08.26
 */
public final class BuildInfo {

    /**
     * Get the UTC date when the project was built.
     *
     * @return The UTC build date
     */
    public static String getBuildDate() {
        return BuildInfo.getBuildDate(true, true);
    }

    /**
     * Get the UTC date when the project was built.
     *
     * @return The UTC build date
     */
    public static String getBuildDate(final Boolean withSeconds, final Boolean jsonFormat) {
        String buildDate;
        if (withSeconds) {
            buildDate = sbtbuildinfo.BuildInfo.builtAtString()
                .substring(0, sbtbuildinfo.BuildInfo.builtAtString()
                    .indexOf("."));
        } else {
            buildDate = sbtbuildinfo.BuildInfo.builtAtString()
                .substring(0, sbtbuildinfo.BuildInfo.builtAtString()
                    .indexOf(".") - 3);
        }
        if (jsonFormat) {
            buildDate = buildDate.replace(' ', 'T');
        }
        return buildDate;
    }

    /**
     * Get the project version.
     *
     * @return The project version
     */
    public static String getProjectVersion() {
        return sbtbuildinfo.BuildInfo.version();
    }

    /**
     * Get the project name.
     *
     * @return The project name
     */
    public static String getProjectName() {
        return sbtbuildinfo.BuildInfo.name();
    }

    /**
     * Get the version of Java used to compile project.
     *
     * @return Java version
     */
    public static String getJavaVersion() {
        return sbtbuildinfo.BuildInfo.javaVersion();
    }

    /**
     * Get the version of Scala used to compile project.
     *
     * @return Scala version
     */
    public static String getScalaVersion() {
        return sbtbuildinfo.BuildInfo.scalaVersion();
    }

    /**
     * Get the version of SBT used to compile project.
     *
     * @return SBT version
     */
    public static String getSbtVersion() {
        return sbtbuildinfo.BuildInfo.sbtVersion();
    }
}
