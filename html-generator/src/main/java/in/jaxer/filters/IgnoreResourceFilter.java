package in.jaxer.filters;

import in.jaxer.core.utilities.Files;
import in.jaxer.utils.AppPropreties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
@Component
public class IgnoreResourceFilter implements FileFilter
{
	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private CheckIfDirectoryContainsIgnoreFileFilter checkIfDirectoryContainsIgnoreFileFilter;

	@Override
	public boolean accept(File resource)
	{
		if (resource.isFile())
		{
			String extension = Files.getExtensionWithDot(resource.getName().toLowerCase());
			for (String ignoreFile : appPropreties.getIgnoreResourceFiles())
			{
				if (extension == null || extension.equals(ignoreFile))
				{
					return false;
				}
			}
		} else
		{
			for (String ignoreFolder : appPropreties.getIgnoreFolders())
			{
				if (resource.getName().equals(ignoreFolder))
				{
					return false;
				}
			}

			File[] childs = resource.listFiles(checkIfDirectoryContainsIgnoreFileFilter);
			if (childs != null && childs.length > 0)
			{
				return false;
			}
		}
		return true;
	}
}
