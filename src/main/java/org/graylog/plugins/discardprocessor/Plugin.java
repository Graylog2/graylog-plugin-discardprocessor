package org.graylog.plugins.discardprocessor;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

/**
 * Implement the Plugin interface here.
 */
public class Plugin implements org.graylog2.plugin.Plugin {
    @Override
    public PluginMetaData metadata() {
        return new MetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new Module());
    }
}
