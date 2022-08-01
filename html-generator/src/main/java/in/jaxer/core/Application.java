package in.jaxer.core;

import in.jaxer.utils.AppPropreties;
import in.jaxer.validator.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Shakir
 * @date 2022-07-21
 * @since v0.0.0
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
		consoleLogger.ln();
		consoleLogger.info("Active profile: [" + appPropreties.getAppProfile() + "]");
		consoleLogger.info("Root path: [" + appPropreties.getRootPath() + "]");
		consoleLogger.ln();

		basicValidation.doValidation(appPropreties.getRootPath());

		fileHandler.createHtmlFiles(appPropreties.getRootPath());
	}
}
