package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YAML {
    private static final YAMLFactory factory;

    private static final ObjectMapper mapper;

    static {
        factory = new YAMLFactory();
        mapper = new ObjectMapper(factory);
    }

    /**
     * Returns the default YAML factory.
     */
    public static YAMLFactory getFactory() {
        return factory;
    }

    /**
     * Returns the object mapper with the registered that uses the default YAML factory.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
