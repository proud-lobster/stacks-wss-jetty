package com.proudlobster.stacks.wss.jetty;

import java.util.Optional;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.structure.Couple;

public enum WssComponent implements Component {
    WSS_SESSION(DataType.NONE),
    WSS_NEW(DataType.NONE),
    WSS_MESSAGE_IN(DataType.STRING),
    WSS_MESSAGE_OUT(DataType.STRING),
    WSS_FROM_SESSION(DataType.REFERENCE),
    WSS_TO_SESSION(DataType.REFERENCE);

    Couple<String> couple;

    WssComponent(final DataType type) {
        this.couple = Couple.of(name(), type.name());
    }

    @Override
    public Optional<String> get(long i) {
        return couple.get(i);
    }

}
