package in.jaxer;

import in.jaxer.utils.Constants;

/**
 * @author Shakir
 * @date 2022-07-21
 * @since 0.0.0
 */
public class Application
{
	public static void main(String[] args)
	{
		if (args == null || args.length == 0)
		{
			throw new IllegalArgumentException("Please provide build mode [" + Constants.MODE_DEV + " OR " + Constants.MODE_PROD + "]");
		}

		String buildMode = args[0];
		System.out.println("buildMode: " + buildMode);
	}
}
