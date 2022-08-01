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
public class CheckIfDirectoryContainsIgnoreFileFilter implements FileFilter
{
	@Autowired
	private AppPropreties appPropreties;

	/**
	 * @return true if folder contains .ignore or .demo file
	 */
	@Override
	public boolean accept(File resource)
	{
		if (resource.isFile())
		{
			String extension = Files.getExtensionWithDot(resource.getName().toLowerCase());
			if (extension == null)
			{
				return false;
			}

			for (String ignoreFolder : appPropreties.getIgnoreResourceFolders())
			{
				if (extension == null || extension.equals(ignoreFolder))
				{
					return true;
				}
			}
		}
		return false;
	}
}
