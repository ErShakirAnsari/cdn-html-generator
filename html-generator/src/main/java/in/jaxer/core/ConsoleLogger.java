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
		System.out.println(msg);

		if (throwable != null)
		{
			throwable.printStackTrace();
		}
	}

	public void log(ConsoleLoggerType type, String msg)
	{
		log("[" + type.name() + "]\t- " + msg);
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
