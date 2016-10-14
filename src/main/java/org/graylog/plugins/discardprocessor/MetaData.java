package org.graylog.plugins.discardprocessor;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class MetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "org.graylog.plugins.discard-large-messages/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return "org.graylog.plugins.discardprocessor.DiscardProcessorPlugin";
    }

    @Override
    public String getName() {
        return "DiscardProcessor";
    }

    @Override
    public String getAuthor() {
        return "Graylog, Inc <hello@graylog.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/Graylog2/graylog-plugin-discardprocessor");
    }

    @Override
    public Version getVersion() {
        return new Version(1, 1, 0, "SNAPSHOT");
    }

    @Override
    public String getDescription() {
        return "Drops messages larger than a configured size.";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(2, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
