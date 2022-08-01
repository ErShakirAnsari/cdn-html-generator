package in.jaxer.filters;

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
public class CustomFileFilter implements FileFilter
{
	@Autowired
	private CheckIfDirectoryContainsIgnoreFileFilter checkIfDirectoryContainsIgnoreFileFilter;

	@Override
	public boolean accept(File root)
	{
		if (root.isDirectory())
		{
			File[] childFiles = root.listFiles(checkIfDirectoryContainsIgnoreFileFilter);
			if (childFiles != null && childFiles.length > 0)
			{
				return false;
			}
		}

		return true;
	}
}
