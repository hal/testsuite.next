package org.jboss.hal.testsuite.test.deployment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.shrinkwrap.api.Archive; //import org.jboss.shrinkwrap.api.Assignable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class Deployment {

    private final Archive archive;

    private Deployment(Builder builder) {
        this.archive = (Archive) ShrinkWrap.create(builder.type.archiveType, builder.deploymentName);
        builder.assetMap.forEach((path, asset) -> archive.add(asset, path));
    }

    public File getDeploymentFile() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File deploymentFile = new File(tmpDirPath, archive.getName());
        deploymentFile.deleteOnExit();
        archive.as(ZipExporter.class).exportTo(deploymentFile, true);
        return deploymentFile;
    }

    public String getName() {
        return this.archive.getName();
    }

    public Address getAddress() {
        return Address.deployment(getName());
    }

    /**
     * @param serverGroups to be deployed to, this parameter being null in domain mode means to deploy to all server groups
     * @throws IllegalArgumentException in case non null serverGroups parameter in standalone mode
     */
    public Deploy deployEnabledCommand(String... serverGroups) {
        Deploy.Builder builder = new Deploy.Builder(archive.as(ZipExporter.class).exportAsInputStream(), archive.getName(), true);
        if (serverGroups.length > 0) {
            if (!ConfigUtils.isDomain()) {
                throw new IllegalArgumentException("'serverGroups' parameter may be used only for domain scenarios.");
            }
            builder.toServerGroups(serverGroups);
        }
        return builder.build();
    }

    public Undeploy disableCommand() {
        return new Undeploy.Builder(archive.getName()).keepContent().build();
    }

    public static enum Type {
        WAR(WebArchive.class);
        <T extends Archive> Type(Class<T> archiveType) {
            this.archiveType = archiveType;
        }
        private Class archiveType;
    }

    public static class Builder {

        private String deploymentName;
        private Type type = Type.WAR;
        private Map<String, Asset> assetMap = new HashMap<>();

        public Builder(String deploymentName) {
            this.deploymentName = deploymentName;
        }

        public Builder textFile(String path, String content) {
            if (assetMap.containsKey(path)) {
                throw new IllegalArgumentException(path + " file already added.");
            }
            assetMap.put(path, new StringAsset(content));
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Deployment build() {
            return new Deployment(this);
        }
    }
}
