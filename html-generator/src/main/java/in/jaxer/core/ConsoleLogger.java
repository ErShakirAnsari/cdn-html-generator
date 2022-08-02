package in.jaxer.core;

import in.jaxer.utils.AppPropreties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Shakir
 * @date 01-08-2022
 * @since v1.0.0
 */
@Component
public class ConsoleLogger
{
	@Autowired
	private AppPropreties appPropreties;

	public void log(String msg)
	{
		log(msg, null);
	}

	public void log(String msg, Throwable throwable)
	{
//		System.out.println(AnsiBackgroundColor.RED + AnsiFontColor.BLACK + msg + AnsiFontColor.RESET);
		System.out.println(msg);

		if (throwable != null)
		{
			throwable.printStackTrace();
		}
	}

	public void log(ConsoleLoggerType type, String msg)
	{
		log(getColor(type) + "[" + type.name() + "]\t- " + msg + AnsiFontColor.RESET);
	}

	public void ln()
	{
		log("");
	}

	public void border()
	{
		log("----- ----- ----- -----");
	}

	public void debug(String msg)
	{
		if (ConsoleLoggerType.debug.value >= appPropreties.getLoggerType().value)
		{
			log(ConsoleLoggerType.debug, msg);
		}
	}

	public void info(String msg)
	{
		if (ConsoleLoggerType.info.value >= appPropreties.getLoggerType().value)
		{
			log(ConsoleLoggerType.info, msg);
		}
	}

	public void warning(String msg)
	{
		if (ConsoleLoggerType.warning.value >= appPropreties.getLoggerType().value)
		{
			log(ConsoleLoggerType.warning, msg);
		}
	}

	public void error(String msg)
	{
		if (ConsoleLoggerType.warning.value >= appPropreties.getLoggerType().value)
		{
			log(ConsoleLoggerType.error, msg);
		}
	}

	public void error(String msg, Throwable throwable)
	{
		if (ConsoleLoggerType.warning.value >= appPropreties.getLoggerType().value)
		{
			log(ConsoleLoggerType.error, msg);
		}
	}

	private String getColor(ConsoleLoggerType loggerType)
	{
		if (loggerType == ConsoleLoggerType.info)
		{
//			return AnsiFontColor.BLUE;
		}
		if (loggerType == ConsoleLoggerType.warning)
		{
			return AnsiFontColor.YELLOW;
		}
		if (loggerType == ConsoleLoggerType.error)
		{
			return AnsiFontColor.RED;
		}

		return "";
	}

	private class AnsiFontColor
	{
		public static final String BLACK = "\u001B[30m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";
		public static final String YELLOW = "\u001B[33m";
		public static final String BLUE = "\u001B[34m";
		public static final String PURPLE = "\u001B[35m";
		public static final String CYAN = "\u001B[36m";
		public static final String WHITE = "\u001B[37m";
		public static final String RESET = "\u001B[0m";
	}

	private class AnsiBackgroundColor
	{
		public static final String BLACK = "\u001B[40m";
		public static final String RED = "\u001B[41m";
		public static final String GREEN = "\u001B[42m";
		public static final String YELLOW = "\u001B[43m";
		public static final String BLUE = "\u001B[44m";
		public static final String PURPLE = "\u001B[45m";
		public static final String CYAN = "\u001B[46m";
		public static final String WHITE = "\u001B[47m";
	}

	public enum ConsoleLoggerType
	{
		debug(1000),
		info(2000),
		warning(3000),
		error(4000);

		private final int value;

		ConsoleLoggerType(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}
	}
}
