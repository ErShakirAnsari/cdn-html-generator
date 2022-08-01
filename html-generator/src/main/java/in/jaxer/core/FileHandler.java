package in.jaxer.core;

import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Strings;
import in.jaxer.filters.CustomFileFilter;
import in.jaxer.utils.AppPropreties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

	private String rootPath;

	public void createHtmlFiles(String rootPath)
	{
		System.out.println("profile: [" + appPropreties.getAppProfile() + "]");
		this.rootPath = rootPath;

//		basicValidation();
//
//		pathWalker(new File(rootPath));
	}

	private void pathWalker(File root)
	{
		String depth = Strings.removeStartsWith(root.getAbsolutePath(), this.rootPath);
		depth = depth.replaceAll("\\\\", "/");
		System.out.println("depth: [" + depth + "]");

		File[] childs = root.listFiles(customFileFilter);
		if (childs == null)
		{
			System.out.println("No file/folder found at root: " + root.getAbsolutePath());
			return;
		}

		createHtmlFile(root, depth);

		for (File child : childs)
		{
			if (child.isDirectory())
			{
				pathWalker(child);
			}
		}
	}

	private void createHtmlFile(File folder, String depth)
	{
		File htmlFile = new File(folder, appPropreties.getAppPagenameIndex());
		try (Writer writer = new BufferedWriter(new FileWriter(htmlFile));)
		{
			writer.append(htmlManager.getHtmlHead(""));
			writer.append(htmlManager.getBodyHeader());

			writer.append(htmlManager.openContainer());

			//-- \css\emoji-css\@demo
			String remainingPath = Strings.removeStartsWith(folder.getAbsolutePath(), this.rootPath);

			writer.append(htmlManager.getBreadcrumb(remainingPath));
			writer.append(htmlManager.getTable(folder, remainingPath, depth));

			writer.append(htmlManager.closeContainer());
			writer.append(htmlManager.getFooter());
			writer.append(htmlManager.getModelHtml());
			writer.append(htmlManager.endBody());
			System.out.println(" --- created a file at:\t[" + htmlFile.getAbsolutePath() + "]");
		} catch (Exception ex)
		{
			JValidator.rethrow(ex);
		}
	}

	public String fileReader(String filename, boolean trim)
	{
		StringBuilder builder = new StringBuilder();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
			 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);)
		{
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				builder.append(trim ? line.trim() : line);
				builder.append(trim ? "" : System.lineSeparator());
			}
		} catch (Exception ex)
		{
			JValidator.rethrow(ex);
		}

		return builder.toString();
	}
}
