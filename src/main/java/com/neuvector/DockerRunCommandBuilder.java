package com.neuvector;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for the docker run command arguments.
 */
public class DockerRunCommandBuilder
{
    private final List<String> cmdList;

    public DockerRunCommandBuilder() {
        cmdList = new ArrayList<>();
        cmdList.add("docker");
        cmdList.add("run");
        cmdList.add("--rm");
    }

    DockerRunCommandBuilder withUserAndGroup(String userIdGroupId) {
        if (userIdGroupId != null && userIdGroupId.length() > 0) {
            cmdList.add("-u");
            cmdList.add(userIdGroupId);
        }
        return this;
    }

    DockerRunCommandBuilder withName(String name) {
        cmdList.add("--name");
        cmdList.add(name);
        return this;
    }

    DockerRunCommandBuilder withVolume(String volumeMapping) {
        cmdList.add("-v");
        cmdList.add(volumeMapping);
        return this;
    }

    DockerRunCommandBuilder withEnvironment(String environmentVariable) {
        cmdList.add("-e");
        cmdList.add(environmentVariable);
        return this;
    }

    String[] buildForImage(String image) {
        cmdList.add(image);
        return cmdList.toArray(new String[0]);
    }
}
