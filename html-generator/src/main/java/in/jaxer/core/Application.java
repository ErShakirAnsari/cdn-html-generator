package in.jaxer.core;

import in.jaxer.core.utilities.ConsoleInput;
import in.jaxer.core.utilities.JValidator;
import in.jaxer.utils.AppPropreties;
import in.jaxer.validator.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Shakir
 * @date 2022-07-21
 * @since v1.0.0
 */
@Component
public class Application
{
	@Autowired
	private ConsoleLogger consoleLogger;

	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private BasicValidation basicValidation;

	@Autowired
	private FileHandler fileHandler;

	public void start()
	{
		consoleLogger.border();
		consoleLogger.info("Active profile: [" + appPropreties.getAppProfile() + "]");
		consoleLogger.info("Root path: [" + appPropreties.getRootPath() + "]");
		consoleLogger.border();

		try (ConsoleInput consoleInput = new ConsoleInput())
		{
			consoleLogger.log("Do you want to continue? (y/n)");
			if (!"Y".equalsIgnoreCase(consoleInput.readString()))
			{
				System.exit(0);
			}

//			consoleLogger.log("Please enter a version i.e: vYYMMDD - ");
//			String version = consoleInput.readString();

			File rootFile = new File(appPropreties.getRootPath());
			String canonicalPath = rootFile.getCanonicalPath();
			consoleLogger.info("canonicalPath: [" + canonicalPath + "]");

			basicValidation.doValidation(rootFile.getCanonicalFile());

//			fileHandler.setVersion(version);
			fileHandler.createHtmlFiles(rootFile.getCanonicalFile());
		} catch (Exception e)
		{
			JValidator.rethrow(e);
		}

	}
}
