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
		consoleLogger.info("version: [" + appPropreties.getVersion() + "]");
		consoleLogger.border();

		try (ConsoleInput consoleInput = new ConsoleInput())
		{
			consoleLogger.log("Do you want to continue? (y/n)");
			if (!"Y".equalsIgnoreCase(consoleInput.readString()))
			{
				System.exit(0);
			}
			consoleLogger.ln();

			File rootFile = new File(appPropreties.getRootPath());
			consoleLogger.info("canonicalPath: [" + rootFile.getCanonicalPath() + "]");
			consoleLogger.ln();

			basicValidation.doValidation(rootFile.getCanonicalFile());

			fileHandler.createHtmlFiles(rootFile.getCanonicalFile());
		} catch (Exception e)
		{
			JValidator.rethrow(e);
		}

	}
}
