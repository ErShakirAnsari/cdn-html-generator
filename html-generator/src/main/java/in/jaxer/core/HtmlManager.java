package in.jaxer.core;

import in.jaxer.core.utilities.Files;
import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Time;
import in.jaxer.dto.MetaDto;
import in.jaxer.dto.MetaDtoList;
import in.jaxer.utils.AppPropreties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Shakir
 * @date 23-07-2022
 * @since v1.0.0
 */
@Component
public class HtmlManager
{
	@Autowired
	private AppPropreties appPropreties;

	public String getHtmlHead(String title)
	{
		return "" +
				"<head>\n" +
				"<meta charset='UTF-8' />\n" +
				"<meta http-equiv='X-UA-Compatible' content='IE=edge' />\n" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />\n" +
				"<link rel='stylesheet' href='" + appPropreties.getBootstrapCssIcon() + "' />\n" +
				"<link rel='stylesheet' href='" + appPropreties.getBootstrapCss() + "'/>\n" +
				"<script defer src='" + appPropreties.getBootstrapJs() + "'></script>\n" +
				"<link rel='stylesheet' href='" + appPropreties.getAppCssMain() + "' />\n" +
				"<title>" + appPropreties.getAppTitle() + " - " + title + "</title>\n" +
				"</head>";
	}

	public String getBodyHeader()
	{
		return "" +
				"\n<body>" +
				"\n<header class='text-cprimary ps-md-5 ps-2'>" +
				"<h1 class='text-md-start text-center'>CDN</h1>" +
				"<p class='text-md-start text-center'>Private content delivery network by " + appPropreties.getAppTitle() + "&trade; for personal use only.</p>" +
				"</header>";
	}

	public String getBreadcrum(String remainingPath)
	{
		String navbar = "\n<nav aria-label='breadcrumb'><ol class='breadcrumb p-2 fs-6 text-cprimary fs-5'>";
		if (remainingPath.isEmpty())
		{
			navbar += "\n<li class='breadcrumb-item'><i class='bi bi-house fs-5'></i>&nbsp;Home</li>";
		} else
		{
			navbar += "\n<li class='breadcrumb-item'><i class='bi bi-house fs-5'></i>&nbsp;<a href='/'>Home</a></li>";

			String[] paths = remainingPath.split(File.separator + File.separator);
			for (int i = 0; i < paths.length; i++)
			{
				String path = paths[i];
				if (path.isEmpty())
				{
					continue;
				}

				String relativeBackPath = "";
				for (int j = paths.length - i - 1; j > 0; j--)
				{
					relativeBackPath += "../";
				}

				if (i + 1 == paths.length)
				{
					navbar += "\n<li class='breadcrumb-item active' aria-current='page'>" + path + "</li>";
				} else
				{
					navbar += "\n<li class='breadcrumb-item' aria-current='page'>" +
							"<a href='" + relativeBackPath + appPropreties.getAppHtmlPagename() + "'>" + path + "</a>" +
							"</li>";
				}
			}
		}
		navbar += "" +
				"</ol>\n" +
				"</nav>";
		return navbar;
	}

	public String getTable(File[] files, String remainingPath)
	{
		if (files == null || files.length == 0)
		{
			return "\n<div class='text-center pt-5'><h1>404</h1><p>No resource found</p></div>";
		}

		StringBuilder table = new StringBuilder("");
		table.append("\n<div>")
				.append("\n<table class='table border-0 text-cprimary'>");

		table.append("\n<thead>")
				.append("<tr>")
				.append("<th>Resource name</th>")
				.append("<th class='col-2'><i class='bi bi-clock'></i>&nbsp;Available since</th>")
				.append("<th class='col-1'>Size</th>")
				.append("</tr>")
				.append("</thead>");

		table.append("\n<tbody>");
		if (!remainingPath.isEmpty())
		{
			table.append("\n<tr><td><a href='../" + appPropreties.getAppHtmlPagename() + "'><i class='bi bi-arrow-left'></i> back</a></td><td></td><td></td></tr>");
		}

		StringBuilder trFolders = new StringBuilder("");
		StringBuilder trFiles = new StringBuilder("");

		MetaDtoList metaDtoList = getMetaDtoList(files[0]);

		for (File child : files)
		{
			if (child.isDirectory())
			{
				trFolders.append("\n<tr>");

				trFolders.append("<td>")
						.append("<i class='bi bi-folder-fill fs-5'></i>&nbsp;")
						.append("<a href='./" + child.getName() + "/" + appPropreties.getAppHtmlPagename() + "'>" + child.getName() + "</a>")
						.append("</td>");
				trFolders.append("<td>" + getResourceDate(metaDtoList, child) + "</td>");
				trFolders.append("<td></td>");
				trFolders.append("</tr>");
			} else
			{
				trFiles.append("\n<tr>");
				trFiles.append("<td>")
						.append(getIcon(child.getName().toLowerCase()) + "<a href='#' data-bs-toggle='modal' data-bs-target='#resourceModal'>" + child.getName() + "</a>")
						.append("</td>");
				trFiles.append("<td>" + getResourceDate(metaDtoList, child) + "</td>");
				trFiles.append("<td>" + Files.getFileSize(child.length()) + "</td>");
				trFiles.append("</tr>");
			}
		}

		table.append(trFolders)
				.append(trFiles).append("\n</tbody>")
				.append("\n</table>")
				.append("\n</div>");

		return table.toString();
	}

	private MetaDtoList getMetaDtoList(File file)
	{
		try
		{
			File metaFile = new File(file.getParent(), appPropreties.getMetaJsonFileName());
			return Files.readJsonObject(MetaDtoList.class, metaFile);
		} catch (IOException e)
		{
			System.out.println("[ERROR] meta.json not found");
			return null;
		}
	}

	public String getResourceDate(MetaDtoList metaDtoList, File file)
	{
		String date = "---";
		if (metaDtoList != null && JValidator.isNotNullAndNotEmpty(metaDtoList.getMetaDtoList()))
		{
			Optional<MetaDto> optional = metaDtoList.getMetaDtoList()
					.stream()
					.filter(d -> d.isFolder() == file.isDirectory())
					.filter(d -> d.getName().equalsIgnoreCase(file.getName()))
					.findFirst();
			if (optional.isPresent())
			{
				date = optional.get().getDate();
			}
		}
		return date;
	}

	public String openContainer()
	{
		return "\n<main class='container-fluid p-4'>";
	}

	public String closeContainer()
	{
		return "\n</main>";
	}

	private String getIcon(String filename)
	{
		String defaultIcon = "<i class='bi bi-file-earmark fs-5'></i>&nbsp;";
		if (JValidator.isNullOrEmpty(filename))
		{
			return defaultIcon;
		}

		String extension = Files.getExtensionWithoutDot(filename);
		if (JValidator.isNullOrEmpty(extension))
		{
			return defaultIcon;
		}

		if (extension.endsWith("css")
				|| extension.endsWith("csv")
				|| extension.endsWith("doc")
				|| extension.endsWith("docx")
				|| extension.endsWith("exe")
				|| extension.endsWith("gif")
				|| extension.endsWith("html")
				|| extension.endsWith("java")
				|| extension.endsWith("jpg")
				|| extension.endsWith("json")
				|| extension.equals("js")
				|| extension.equals("md")
				|| extension.equals("mp3")
				|| extension.equals("pdf")
				|| extension.equals("php")
				|| extension.equals("png")
				|| extension.equals("sass")
				|| extension.equals("scss")
				|| extension.equals("sql")
				|| extension.equals("svg")
				|| extension.equals("txt")
				|| extension.equals("woff")
				|| extension.equals("xls")
				|| extension.equals("xlsx")
				|| extension.equals("xml")
				|| extension.equals("yml")
		)
		{
			return "<i class='bi bi-filetype-" + extension + "'></i>&nbsp;";
		}

		return defaultIcon;
	}

	public String getFooter()
	{
		return "" +
				"\n<footer class='bg-cprimary mt-5 border-top'>" +
				"<div class='p-5 text-md-end text-center'>" +
				"<div class='align-bottom text-cprimary'>" +
				"<img src='" + appPropreties.getAppImageLogo() + "' class='mb-1' alt='logo' width='48' />" +
				"<p class='fs-5 m-0'>" + appPropreties.getAppBrand() + "&trade;</p>" +
				"<p class='fs-6 m-0'>" + appPropreties.getAppPublishedDate() + "</p>" +
				"</div>" +
				"</div>" +
				"</footer>";
	}

	public String endBody()
	{
		return "\n</body>\n<html>";
	}
}
