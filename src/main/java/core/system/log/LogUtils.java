package core.system.log;

import java.util.Arrays;

/**
 * Класс длявсяких утилитарных методов, связанных с логированием
 */
public class LogUtils {
  public static String exceptionToString(Throwable th){
    StringBuilder textBuilder = new StringBuilder(20000);
    textBuilder.append(th.getMessage() == null ? "Null pointer exception" : th.getLocalizedMessage())
            .append("\r\n");
    Arrays.stream(th.getStackTrace()).forEach(stackTraceElement -> textBuilder.append(stackTraceElement.toString()).append("\r\n"));
    return textBuilder.toString();
  }
}
