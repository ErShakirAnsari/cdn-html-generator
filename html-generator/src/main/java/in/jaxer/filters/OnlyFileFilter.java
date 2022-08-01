package in.jaxer.filters;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Shakir
 * @date 01-08-2022
 * @since v1.0.0
 */
@Component
public class OnlyFileFilter implements FileFilter
{
	@Override
	public boolean accept(File resource)
	{
		return resource.isFile() && resource.exists();
	}
}
