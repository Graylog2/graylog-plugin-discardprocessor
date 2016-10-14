# DiscardProcessor Plugin for Graylog

[![Build Status](https://travis-ci.org/Graylog2/graylog-plugin-discardprocessor.svg?branch=master)](https://travis-ci.org/Graylog2/graylog-plugin-discardprocessor)

This plugin implements a Graylog `MessageProcessor` to discard messages with a large field. Both the field name and the maximum length are configurable.

**Required Graylog version:** 2.0 and later

Installation
------------

[Download the plugin](https://github.com/Graylog2/graylog-plugin-discardprocessor/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Usage
-----

Activate/Deactivate the plugin in the `System/Configuration` section of the web interface. Typically you want to run this processor as the first in the chain to prevent large messages ever reaching the more expensive parts of the processing, such as Grok patterns or regular expression extractors.

Configuration
-------------
In your `server.conf` file add the following two lines:

```
# By default the plugin looks at the message field, this name is case sensitive!
discardprocessor_field = message
# By default the plugin allows the length to by Long.MAX_VALUE (2^63 - 1). This is the character count, not the byte size.
discardprocessor_max_length = 10

```

After a server restart the values take effect.


Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.

Plugin Release
--------------

```
$ mvn versions:set
# edit src/main/java/org/graylog/plugins/discardprocessor/MetaData.java to reflect the new version
$ mvn clean test compile package
```

Then tag the release, push, create the release and upload the artifact from target/ to Github.