package in.jaxer.validator;

import in.jaxer.core.ConsoleLogger;
import in.jaxer.core.utilities.Files;
import in.jaxer.filters.IgnoreResourceFilter;
import in.jaxer.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shakir
 * @date 31-07-2022
 * @since v1.0.0
 */
@Component
public class BasicValidation
{
	@Autowired
	private ConsoleLogger consoleLogger;

	@Autowired
	private IgnoreResourceFilter ignoreResourceFilter;

	public void doValidation(File root)
	{
		basicValidation(root);
	}

	private void basicValidation(File rootFile)
	{
		boolean validationFailed = false;
		if (!rootFile.isDirectory())
		{
			consoleLogger.error("rootPath is not a directory [" + rootFile.getAbsolutePath() + "]");
			validationFailed = true;
		}

		boolean foundInvalidResource = resourcePathWalker(rootFile);
		if (foundInvalidResource)
		{
			validationFailed = true;
		}

		consoleLogger.ln();

		if (validationFailed)
		{
			consoleLogger.error("Please fix these issues.. System will exit with exit-code 0");
			System.exit(0);
		} else
		{
			consoleLogger.info("Validation got complete..");
		}
	}

	private boolean resourcePathWalker(File root)
	{
		consoleLogger.debug("resourcePathWalker: [" + root.getAbsolutePath() + "]");
		boolean foundInvalidResource = false;

		File[] childs = root.listFiles(ignoreResourceFilter);
		if (childs != null && childs.length > 0)
		{
			for (File f : childs)
			{
				if (isInvalidResource(f))
				{
					foundInvalidResource = true;
				} else if (f.isDirectory() && !f.isHidden() && resourcePathWalker(f))
				{
					foundInvalidResource = true;
				}
			}
		}
		return foundInvalidResource;
	}

	private boolean isInvalidResource(File resource)
	{
		if (resource.isHidden())
		{
			consoleLogger.error("Hidden resource found: [" + resource.getAbsolutePath() + "]");
			return true;
		}

		if (!resource.getName().toLowerCase().equals(resource.getName()))
		{
			consoleLogger.error("Uppercase not allowed in resource-name: [" + resource.getAbsolutePath() + "]");
			return true;
		}

		if (resource.getName().contains("@"))
		{
			consoleLogger.error("Special characters are not resource-name: [" + resource.getAbsolutePath() + "]");
			return true;
		}

		if (resource.getName().contains(" "))
		{
			consoleLogger.error("White-space not allowed in resource-name: [" + resource.getAbsolutePath() + "]");
			return true;
		}

		if (resource.isFile())
		{
			if (Files.getExtensionWithoutDot(resource.getName()) == null)
			{
				consoleLogger.error("Without extension resource found: [" + resource.getAbsolutePath() + "]");
				return true;
			}

			Pattern compile = Pattern.compile(AppConstants.REGEX_VERSION);
			Matcher matcher = compile.matcher(resource.getName());
			if (!matcher.find())
			{
				consoleLogger.error("Invalid versioned resource found: [" + resource.getAbsolutePath() + "]");
				return true;
			}

			if (!resource.getName().matches(AppConstants.REGEX_VERSION))
			{
			}
		}

		return false;
	}
}
