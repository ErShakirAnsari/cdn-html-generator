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
public class IgnoreResourceFilter implements FileFilter
{
	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private IgnoreFolderFilter ignoreFolderFilter;

	@Override
	public boolean accept(File resource)
	{
		if (resource.isFile())
		{
			String extension = Files.getExtensionWithDot(resource.getName().toLowerCase());
			for (String ignoreFile : appPropreties.getIgnoreFiles())
			{
				if (extension == null || extension.equals(ignoreFile))
				{
					return false;
				}
			}
		} else
		{
			if (resource.isHidden())
			{
				return false;
			}

			File[] childs = resource.listFiles(ignoreFolderFilter);
			if (childs != null && childs.length > 0)
			{
				return false;
			}
		}
		return true;
	}
}
