package org.graylog.plugins.discardprocessor;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.Messages;
import org.graylog2.plugin.messageprocessors.MessageProcessor;

import javax.inject.Inject;
import javax.inject.Named;

public class DiscardProcessor implements MessageProcessor {

    private final String fieldName;
    private final long maxLength;

    public static class Descriptor implements MessageProcessor.Descriptor {
        @Override
        public String name() {
            return "Discard large messages";
        }

        @Override
        public String className() {
            return DiscardProcessor.class.getCanonicalName();
        }
    }

    @Inject
    public DiscardProcessor(@Named("discardprocessor_field") String fieldName,
                            @Named("discardprocessor_max_length") long maxLength) {
        this.fieldName = fieldName;
        this.maxLength = maxLength;
    }

    @Override
    public Messages process(Messages messages) {
        if (maxLength == Long.MAX_VALUE) {
            return messages;
        }
        for (Message message : messages) {
            final Object content = message.getField(fieldName);
            if (content != null && content instanceof String) {
                if (((String) content).length() > maxLength) {
                    message.setFilterOut(true);
                }
            }
        }
        return messages;
    }
}
