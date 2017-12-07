package com.miskevich.movieland.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.Test

class SMTPAppenderITest {
    private final Logger LOG = LoggerFactory.getLogger(getClass())

    @Test
    void testSendEmail() {
        for (int i = 0; i < 2; i++) {
            try {
                50 / 0
            } catch (ArithmeticException e) {
                def message = 'Division by zero is prohibited!'
                LOG.error(message, e)
            }
        }
    }
}
