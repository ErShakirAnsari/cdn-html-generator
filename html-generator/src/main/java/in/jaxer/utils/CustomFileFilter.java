package in.jaxer.utils;

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
	private IgnoreFolderFilter ignoreFolderFilter;

	@Override
	public boolean accept(File root)
	{
		if (root.isDirectory())
		{
			if (root.isHidden())
			{
				return false;
			}

			File[] childFiles = root.listFiles(ignoreFolderFilter);
			if (childFiles != null && childFiles.length > 0)
			{
				return false;
			}
		}

		return true;
	}
}
