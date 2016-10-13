package org.graylog.plugins.discardprocessor;

import com.github.joschi.jadconfig.Parameter;
import com.github.joschi.jadconfig.validators.PositiveLongValidator;
import com.github.joschi.jadconfig.validators.StringNotBlankValidator;
import org.graylog2.plugin.PluginConfigBean;

public class Config implements PluginConfigBean {
    private static final String PREFIX = "discardprocessor_";

    @Parameter(value = PREFIX + "field", validator = StringNotBlankValidator.class)
    private String fieldName = "message";

    @Parameter(value = PREFIX + "max_length", validator = PositiveLongValidator.class)
    private long maxLength = Long.MAX_VALUE;

    public String getFieldName() {
        return fieldName;
    }

    public long getMaxLength() {
        return maxLength;
    }
}
