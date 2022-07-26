package in.jaxer.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
public class ValidationFileFilter implements FileFilter
{
	@Override
	public boolean accept(File pathname)
	{
		return pathname.getName().contains(" ");
	}
}
