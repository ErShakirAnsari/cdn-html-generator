package in.jaxer;

import in.jaxer.core.FileHandler;
import in.jaxer.validator.BasicValidation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * @author Shakir
 * @date 2022-07-21
 * @since v0.0.0
 */
public class Application
{
	public static void main(String[] args)
	{
		start();
	}

	private static void start()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		BasicValidation basicValidation = context.getBean(BasicValidation.class);

		String root = "C:\\Users\\Shakir\\Documents\\git\\personal\\jaxer-in\\forked\\cdn";
		basicValidation.doValidation(new File(root));

//		FileHandler fileHandler = context.getBean(FileHandler.class);
//		fileHandler.createHtmlFiles(root);
	}
}
