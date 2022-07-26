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
public class IgnoreFolderFilter implements FileFilter
{
	@Autowired
	private AppPropreties appPropreties;

	/**
	 * @return true if folder contains .ignore or .demo file
	 */
	@Override
	public boolean accept(File pathname)
	{
		if (pathname.isFile())
		{
			String extension = Files.getExtensionWithDot(pathname.getName().toLowerCase());
			for (String ignoreFolder : appPropreties.getIgnoreFolders())
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
