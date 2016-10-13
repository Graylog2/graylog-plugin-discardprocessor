package org.graylog.plugins.discardprocessor;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageCollection;
import org.graylog2.plugin.Messages;
import org.graylog2.plugin.SingletonMessages;
import org.graylog2.plugin.Tools;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscardProcessorTest {

    private DiscardProcessor limitMessageFieldTo100 = new DiscardProcessor("message", 100);
    private DiscardProcessor limitSomethingFieldTo100 = new DiscardProcessor("something", 100);

    @Test
    public void shortMessageNotFiltered() {
        Message shortMessage = new Message(Strings.padEnd("", 50, '*'), "source", Tools.nowUTC());
        final Messages shortOut = limitMessageFieldTo100.process(new SingletonMessages(shortMessage));

        assertThat(shortOut).hasOnlyOneElementSatisfying((message) -> {
            assertThat(message.getFilterOut()).isFalse();
        });

    }

    @Test
    public void longMessageFiltered() {
        Message longMessage = new Message(Strings.padEnd("", 1000, '*'), "source", Tools.nowUTC());
        final Messages longOut = limitMessageFieldTo100.process(new SingletonMessages(longMessage));

        assertThat(longOut).hasOnlyOneElementSatisfying((message) -> {
            assertThat(message.getFilterOut()).isTrue();
        });
    }

    @Test
    public void multipleMessages() {

        final Message msg10 = new Message(Strings.padEnd("", 10, '*'), "source", Tools.nowUTC());
        final Message msg101 = new Message(Strings.padEnd("", 101, '*'), "source", Tools.nowUTC());
        final Message msg100 = new Message(Strings.padEnd("", 100, '*'), "source", Tools.nowUTC());
        final Message msg99 = new Message(Strings.padEnd("", 99, '*'), "source", Tools.nowUTC());
        final Message msg1000 = new Message(Strings.padEnd("", 1000, '*'), "source", Tools.nowUTC());
        final MessageCollection inMessages = new MessageCollection(ImmutableList.of(
                msg10,
                msg100,
                msg101,
                msg99,
                msg1000
        ));

        final Messages outMessages = limitMessageFieldTo100.process(inMessages);


        assertThat(outMessages)
                .containsExactly(msg10, msg100, msg99);
    }

    @Test
    public void otherField() {
        // must be two different objects because DiscardProcessor mutates the message object!
        final Message notFiltered = new Message("ignored", "source", Tools.nowUTC());
        notFiltered.addField("something", Strings.padEnd("", 101, '*'));

        final Message filtered = new Message("ignored", "source", Tools.nowUTC());
        filtered.addField("something", Strings.padEnd("", 101, '*'));

        final Messages wrongField = limitMessageFieldTo100.process(notFiltered);
        final Messages correctField = limitSomethingFieldTo100.process(filtered);

        assertThat(wrongField).containsExactly(notFiltered);
        assertThat(correctField).isEmpty();
    }

    @Test
    public void nonStringField() {
        final Message notFiltered = new Message("ignored", "source", Tools.nowUTC());
        notFiltered.addField("something", 10L);

        final Messages wrongType = limitSomethingFieldTo100.process(notFiltered);

        assertThat(wrongType).containsExactly(notFiltered);
    }
}