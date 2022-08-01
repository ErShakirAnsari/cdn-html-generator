package in.jaxer.validator;

import in.jaxer.core.ConsoleLogger;
import in.jaxer.core.utilities.Files;
import in.jaxer.filters.IgnoreResourceFilter;
import in.jaxer.filters.OnlyFileFilter;
import in.jaxer.utils.AppConstants;
import in.jaxer.utils.AppPropreties;
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

	@Autowired
	private OnlyFileFilter onlyFileFilter;

	@Autowired
	private AppPropreties appPropreties;

	public void doValidation(File root)
	{
		basicValidation(root);
		consoleLogger.ln();
	}

	private void basicValidation(File rootFile)
	{
		if (!isCdnDirectory(rootFile))
		{
			System.exit(0);
		}

		boolean validationFailed = false;
		validationFailed = resourcePathWalker(rootFile, validationFailed);

		if (validationFailed)
		{
			consoleLogger.error("Please fix these issues.. System will exit with exit-code 0");
			System.exit(0);
		} else
		{
			consoleLogger.info("Validation got complete..");
		}
	}

	private boolean isCdnDirectory(File root)
	{
		if (!root.isDirectory())
		{
			consoleLogger.error("ROOT MUST BE A FOLDER [" + root.getAbsolutePath() + "]");
			return false;
		}

		File[] files = root.listFiles(onlyFileFilter);

		for (File f : files)
		{
			if (f.getName().equals(appPropreties.getFileCdn()))
			{
				return true;
			}
		}

		consoleLogger.error("THIS IS NOT A CDN FOLDER");
		return false;
	}

	private boolean resourcePathWalker(File root, boolean foundInvalidResource)
	{
		consoleLogger.debug("foundInvalidResource: [" + foundInvalidResource + "]");
		consoleLogger.debug("resourcePathWalker: [" + root.getAbsolutePath() + "]");

		File[] childs = root.listFiles(ignoreResourceFilter);
		if (childs != null && childs.length > 0)
		{
			for (File f : childs)
			{
				if (isInvalidResource(f))
				{
					foundInvalidResource = true;
				}

				if (f.isDirectory() && resourcePathWalker(f, foundInvalidResource))
				{
					foundInvalidResource = true;
				}
			}
		}
		return foundInvalidResource;
	}

	private boolean isInvalidResource(File resource)
	{
		boolean isInvalidResource = false;
		if (resource.isHidden())
		{
			consoleLogger.error("Hidden resource found: [" + resource.getAbsolutePath() + "]");
			isInvalidResource = true;
		}

		if (!resource.getName().toLowerCase().equals(resource.getName()))
		{
			consoleLogger.error("Uppercase not allowed in resource-name: [" + resource.getAbsolutePath() + "]");
			isInvalidResource = true;
		}

		if (resource.getName().contains("@"))
		{
			consoleLogger.error("Special characters are not resource-name: [" + resource.getAbsolutePath() + "]");
			isInvalidResource = true;
		}

		if (resource.getName().contains(" "))
		{
			consoleLogger.error("White-space not allowed in resource-name: [" + resource.getAbsolutePath() + "]");
			isInvalidResource = true;
		}

		if (resource.isFile())
		{
			if (Files.getExtensionWithoutDot(resource.getName()) == null)
			{
				consoleLogger.error("Without extension resource found: [" + resource.getAbsolutePath() + "]");
				isInvalidResource = true;
			}

			Pattern compile = Pattern.compile(AppConstants.REGEX_VERSION);
			Matcher matcher = compile.matcher(resource.getName());
			if (!matcher.find())
			{
				consoleLogger.error("Invalid versioned resource found: [" + resource.getAbsolutePath() + "]");
				isInvalidResource = true;
			}

			if (!resource.getName().matches(AppConstants.REGEX_VERSION))
			{
			}
		}

		return isInvalidResource;
	}
}
