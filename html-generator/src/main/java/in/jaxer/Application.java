package in.jaxer;

import in.jaxer.core.FileHandler;
import in.jaxer.utils.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * @author Shakir
 * @date 2022-07-21
 * @since 0.0.0
 */
public class Application
{
	public static void main(String[] args)
	{
		if (args == null || args.length == 0)
		{
			throw new IllegalArgumentException("Please provide build mode [" + Constants.MODE_DEV + " OR " + Constants.MODE_PROD + "]");
		}

		String buildMode = args[0];
		System.out.println("buildMode: " + buildMode);

		spring();
	}

	private static void spring()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		FileHandler fileHandler = context.getBean(FileHandler.class);
		fileHandler.createHtmlFiles("C:\\Users\\Shakir\\Documents\\git\\personal\\jaxer-in\\forked\\cdn");
	}
}
