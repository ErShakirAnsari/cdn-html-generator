package in.jaxer.core;

import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Strings;
import in.jaxer.filters.CustomFileFilter;
import in.jaxer.filters.IgnoreResourceFilter;
import in.jaxer.utils.AppPropreties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
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
	private ConsoleLogger consoleLogger;

	@Autowired
	private HtmlManager htmlManager;

	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private CustomFileFilter customFileFilter;

	@Autowired
	private IgnoreResourceFilter ignoreResourceFilter;

	public void createHtmlFiles(File rootFile)
	{
		String absolutePath = rootFile.getAbsolutePath();
		pathWalker(rootFile, absolutePath);
	}

	private void pathWalker(File root, String canonicalPath)
	{
		String depth = Strings.removeStartsWith(root.getAbsolutePath(), canonicalPath);
		depth = depth.replaceAll("\\\\", "/");
		consoleLogger.debug("depth: [" + depth + "]");

		File[] childs = root.listFiles(ignoreResourceFilter);
		if (childs == null)
		{
			consoleLogger.warning("No file/folder found at root: " + root.getAbsolutePath());
			return;
		}

		createHtmlFile(root, depth);

		for (File child : childs)
		{
			if (child.isDirectory())
			{
				pathWalker(child, canonicalPath);
			}
		}
	}

	private void createHtmlFile(File folder, String depth)
	{
		File htmlFile = new File(folder, appPropreties.getAppPagenameIndex());
		boolean exists = htmlFile.exists();

		try (Writer writer = new BufferedWriter(new FileWriter(htmlFile));)
		{
			writer.append(htmlManager.getHtmlHead(folder.getName(), depth));
			writer.append(htmlManager.getBodyHeader());

			writer.append(htmlManager.openContainer());
			writer.append(htmlManager.getBreadcrumb(depth));
			writer.append(htmlManager.getTable(folder, depth));
			writer.append(htmlManager.closeContainer());

			writer.append(htmlManager.getFooter());
			writer.append(htmlManager.getModelHtml());
			writer.append(htmlManager.endBody());

			consoleLogger.info((exists ? "Overwritten" : "Created") + " html file at: [" + htmlFile.getAbsolutePath() + "]");
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
