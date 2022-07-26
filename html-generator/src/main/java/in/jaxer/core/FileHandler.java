package in.jaxer.core;

import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Strings;
import in.jaxer.utils.CustomFileFilter;
import in.jaxer.utils.AppPropreties;
import in.jaxer.utils.IgnoreFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
@Component
public class FileHandler
{
	@Autowired
	private HtmlManager htmlManager;

	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private CustomFileFilter customFileFilter;

	@Autowired
	private IgnoreFileFilter ignoreFileFilter;

	private String rootPath;

	public void createHtmlFiles(String rootPath)
	{
		this.rootPath = rootPath;

		basicValidation();

		pathWalker(new File(rootPath));
	}

	private void basicValidation()
	{
		File rootFile = new File(rootPath);

		if (!rootFile.isDirectory())
		{
			System.out.println("rootPath is not a directory [" + rootFile.getAbsolutePath() + "]");
			System.out.println("Please fix these issues");
			System.out.println("System will exit");
			System.exit(0);
		}

		boolean foundInvalidResource = validResourcePathWalker(rootFile);
		if (foundInvalidResource)
		{
			System.out.println("System will exit");
			System.exit(0);
		}
	}

	private void pathWalker(File root)
	{
		File[] childs = root.listFiles(customFileFilter);
		if (childs == null)
		{
			System.out.println("No file/folder found at root: " + root.getAbsolutePath());
			return;
		}

		createHtmlFile(root);

		for (File child : childs)
		{
			if (child.isDirectory())
			{
				pathWalker(child);
			}
		}
	}

	private void createHtmlFile(File folder)
	{
		File htmlFile = new File(folder, appPropreties.getAppHtmlPagename());
		try (Writer writer = new BufferedWriter(new FileWriter(htmlFile));)
		{
			writer.append(htmlManager.getHtmlHead(""));
			writer.append(htmlManager.getBodyHeader());

			writer.append(htmlManager.openContainer());

			//-- \css\emoji-css\@demo
			String remainingPath = Strings.removeStartsWith(folder.getAbsolutePath(), this.rootPath);

			writer.append(htmlManager.getBreadcrum(remainingPath));
			writer.append(htmlManager.getTable(folder.listFiles(ignoreFileFilter), remainingPath));

			writer.append(htmlManager.closeContainer());
			writer.append(htmlManager.getFooter());
			writer.append(htmlManager.endBody());
			System.out.println(" --- created a file at:\t[" + htmlFile.getAbsolutePath() + "]");
		} catch (Exception ex)
		{
			JValidator.rethrow(ex);
		}
	}

	private boolean validResourcePathWalker(File root)
	{
		boolean foundInvalidResource = false;

		File[] childs = root.listFiles();
		if (childs != null && childs.length > 0)
		{
			for (File f : childs)
			{
				if (f.getName().contains(" ") || f.getName().contains("@"))
				{
					foundInvalidResource = true;
					System.out.println("Invalid resource-name: [" + f.getAbsolutePath() + "]");
				}

				if (f.isDirectory() && !f.isHidden())
				{
					if (validResourcePathWalker(f))
					{
						foundInvalidResource = true;
					}
				}
			}
		}
		return foundInvalidResource;
	}
}
