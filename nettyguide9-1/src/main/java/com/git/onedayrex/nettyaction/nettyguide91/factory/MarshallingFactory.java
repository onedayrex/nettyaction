package com.git.onedayrex.nettyaction.nettyguide91.factory;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingFactory {

    public static final MarshallingEncoder encode() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(0);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, marshallingConfiguration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }


    public static final MarshallingDecoder decode() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(0);
        DefaultUnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, marshallingConfiguration);
        MarshallingDecoder decoder = new MarshallingDecoder(provider,1024);
        return decoder;
    }
}
