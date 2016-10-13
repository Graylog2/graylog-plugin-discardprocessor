package org.graylog.plugins.discardprocessor;

import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;

/**
 * Extend the PluginModule abstract class here to add you plugin to the system.
 */
public class Module extends PluginModule {
    /**
     * Returns all configuration beans required by this plugin.
     *
     * Implementing this method is optional. The default method returns an empty {@link Set}.
     */
    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.singleton(new Config());
    }

    @Override
    protected void configure() {

        addMessageProcessor(DiscardProcessor.class, DiscardProcessor.Descriptor.class);
        addConfigBeans();
    }
}
