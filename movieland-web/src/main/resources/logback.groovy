import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.util.FileSize

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO

def LOG_PATH = "movieland/log"
def LOG_ARCHIVE = "${LOG_PATH}/archive"

appender("STDOUT", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %X{nickname} %logger{36} - %msg%n"
    }
    filter(ThresholdFilter) {
        level = INFO
    }
}

appender("RollingFile-Appender", RollingFileAppender) {
    file = "${LOG_PATH}/movieland.log"

    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_ARCHIVE}/movieland.log%d{yyy-MM-dd}.log.%i"
        maxHistory = 20
        totalSizeCap = FileSize.valueOf("1GB")
        maxFileSize = FileSize.valueOf("5MB")
    }

    layout(PatternLayout) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %X{nickname} %logger{36} - %msg%n"
    }
}
logger("com.miskevich.movieland", DEBUG)
root(DEBUG, ["STDOUT", "RollingFile-Appender"])
