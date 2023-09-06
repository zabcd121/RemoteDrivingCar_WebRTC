package com.mobility.remotedrivingmobility_be.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Parser {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Optional<Long> parseId(String sid) {
        Long id = null;
        try {
            id = Long.valueOf(sid);
        } catch (Exception e) {
            logger.debug("An error occured: {}", e.getMessage());
        }

        return Optional.ofNullable(id);
    }
}
