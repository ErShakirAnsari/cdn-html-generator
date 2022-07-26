package in.jaxer.utils;

import in.jaxer.core.utilities.Files;
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
public class IgnoreFileFilter implements FileFilter
{
	@Autowired
	private AppPropreties appPropreties;

	@Override
	public boolean accept(File pathname)
	{
		if (pathname.isFile())
		{
			String extension = Files.getExtensionWithDot(pathname.getName().toLowerCase());
			for (String ignoreFile : appPropreties.getIgnoreFiles())
			{
				if (extension == null || extension.equals(ignoreFile))
				{
					return false;
				}
			}

		}
		return true;
	}
}
