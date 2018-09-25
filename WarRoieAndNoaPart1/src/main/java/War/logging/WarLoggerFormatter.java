package War.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class WarLoggerFormatter extends Formatter{
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentTime());
        sb.append(' ');
        sb.append('[');
        sb.append(record.getLevel());
        sb.append("]: ");
        sb.append(record.getMessage());
        sb.append('\n');
        return sb.toString();
    }

    private String currentTime(){
        return LocalDateTime.now().format(dateFormatter);
    }
}
