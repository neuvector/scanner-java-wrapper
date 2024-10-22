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
        cmdList.add("--rm");
    }

    public DockerRunCommandBuilder(String runtime) {
        cmdList = new ArrayList<>();
        cmdList.add(runtime);
        cmdList.add("run");
        cmdList.add("--rm");
    }

    DockerRunCommandBuilder withEntrypoint(String command) {
        cmdList.add("--entrypoint");
        cmdList.add(command);
        return this;
    }

    DockerRunCommandBuilder withVolume(String volumeMapping) {
        if ("".equals(volumeMapping)) {
            return this;
        }
        cmdList.add("-v");
        cmdList.add(volumeMapping);
        return this;
    }

    DockerRunCommandBuilder withEnvironment(String environmentVariable) {
        cmdList.add("-e");
        cmdList.add(environmentVariable);
        return this;
    }

    DockerRunCommandBuilder withImage(String image) {
        cmdList.add(image);
        return this;
    }

    DockerRunCommandBuilder withCommand(String command) {
        cmdList.add("-c");
        cmdList.add(command);
        return this;
    }

    public List<String> getCmdList() {
        return cmdList;
    }
}
