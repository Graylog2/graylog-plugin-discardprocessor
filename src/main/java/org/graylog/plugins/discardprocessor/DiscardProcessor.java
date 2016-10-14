package org.graylog.plugins.discardprocessor;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.graylog2.plugin.IOState;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.Messages;
import org.graylog2.plugin.inputs.MessageInput;
import org.graylog2.plugin.messageprocessors.MessageProcessor;
import org.graylog2.shared.inputs.InputRegistry;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import static com.codahale.metrics.MetricRegistry.name;

public class DiscardProcessor implements MessageProcessor {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DiscardProcessor.class);

    private final String fieldName;
    private final long maxLength;
    private final Meter discarded;
    private final InputRegistry inputRegistry;

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
                            @Named("discardprocessor_max_length") long maxLength,
                            MetricRegistry metricRegistry,
                            InputRegistry inputRegistry) {
        this.fieldName = fieldName;
        this.maxLength = maxLength;
        discarded = metricRegistry.meter(name(DiscardProcessor.class, "discarded"));
        this.inputRegistry = inputRegistry;
    }

    @Override
    public Messages process(Messages messages) {
        if (maxLength == Long.MAX_VALUE) {
            return messages;
        }
        for (Message message : messages) {
            final Object content = message.getField(fieldName);
            if (content != null && content instanceof String) {
                final int length = ((String) content).length();
                if (length > maxLength) {
                    if (LOG.isInfoEnabled()) {
                        final IOState<MessageInput> inputState = inputRegistry.getInputState(message.getSourceInputId());
                        final String inputName = inputState != null ? inputState.getStoppable().getName() : "unknown";
                        final String inputId = inputState != null ? inputState.getStoppable().getId() : "unknown";
                        LOG.info(
                                "Discarding message <{}> from <{}> (received from address <{}> on input \"{}\" <{}>): Field {} exceeds maximum length: {} (maximum length {})",
                                message.getId(),
                                message.getSource(),
                                (message.getIsSourceInetAddress() ? message.getInetAddress() : "unknown"),
                                inputName,
                                inputId,
                                fieldName,
                                length,
                                maxLength);
                    }
                    discarded.mark();
                    message.setFilterOut(true);
                }
            }
        }
        return messages;
    }
}
