package in.jaxer.core;

import in.jaxer.core.utilities.Files;
import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Strings;
import in.jaxer.dto.MetaDto;
import in.jaxer.dto.MetaDtoList;
import in.jaxer.filters.IgnoreResourceFilter;
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
	private ConsoleLogger consoleLogger;

	@Autowired
	private AppPropreties appPropreties;

	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private IgnoreResourceFilter ignoreResourceFilter;

	public String getHtmlHead(String title, String depth)
	{
		StringBuilder headBuilder = new StringBuilder();

		headBuilder.append("<head>")
				.append("<meta charset='UTF-8' />")
				.append("<meta http-equiv='X-UA-Compatible' content='IE=edge' />")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0' />")
				.append("<link rel='stylesheet' href='" + appPropreties.getBootstrapCssIcon() + "' />")
				.append("<link rel='stylesheet' href='" + appPropreties.getBootstrapCss() + "'/>")
				.append("<script defer src='" + appPropreties.getBootstrapJs() + "'></script>")
				.append("<link rel='stylesheet' href='" + appPropreties.getAppCssMain() + "' />")
				.append("<script defer src='" + appPropreties.getApplicationJs() + "'></script>")
				.append("\n<title>" + appPropreties.getAppTitle() + " - " + title + "</title>")
				.append("\n<script>")
				.append("const depth = '" + depth + "';")
				.append("const version = '" + appPropreties.getVersion() + "';")
				.append("</script>")
				.append("</head>");

		return headBuilder.toString();
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

	public String getBreadcrumb(String depth)
	{
		StringBuilder navbar = new StringBuilder("");

		navbar.append("\n<nav aria-label='breadcrumb'>")
				.append("<ol class='breadcrumb p-2 fs-6 text-cprimary fs-5'>");

		if (depth.isEmpty())
		{
			navbar.append("\n<li class='breadcrumb-item active'><i class='bi bi-house fs-5'></i>&nbsp;Home</li>");
		} else
		{
			boolean homeFlag = true;
			String[] paths = depth.split("/");
			for (int i = 0; i < paths.length; i++)
			{
				String path = paths[i];
				if (!path.isEmpty())
				{
					String relativeBackPath = "";
					for (int j = paths.length - i - 1; j > 0; j--)
					{
						relativeBackPath += "../";
					}

					if (homeFlag)
					{
						navbar.append("\n<li class='breadcrumb-item'>")
								.append("<i class='bi bi-house fs-5'></i>&nbsp;")
								.append("<a href='../" + relativeBackPath + appPropreties.getAppPagenameIndex() + "'>Home</a>")
								.append("</li>");
						homeFlag = false;
					}

					if (i + 1 == paths.length)
					{
						navbar.append("\n<li class='breadcrumb-item active' aria-current='page'>" + path + "</li>");
					} else
					{
						navbar.append("\n<li class='breadcrumb-item' aria-current='page'>")
								.append("<a href='" + relativeBackPath + appPropreties.getAppPagenameIndex() + "'>" + path + "</a>")
								.append("</li>");
					}
				}
			}
		}
		navbar.append("</ol></nav>");
		return navbar.toString();
	}

	public String getTable(File folder, String depth)
	{
		File[] files = folder.listFiles(ignoreResourceFilter);
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
//				.append("<th class='col-2'><i class='bi bi-clock'></i>&nbsp;Available since</th>")
//				.append("<th class='col-2'>Available since</th>")
				.append("<th class='col-1 text-end'>Size</th>")
				.append("</tr>")
				.append("</thead>");

		table.append("\n<tbody>");

		if (!depth.isEmpty())
		{
			// do not show back button on home page
			table.append("\n<tr><td><a href='../" + appPropreties.getAppPagenameIndex() + "'><i class='bi bi-arrow-left'></i> back</a></td><td></td><td></td></tr>");
		}

		StringBuilder trFolders = new StringBuilder("");
		StringBuilder trFiles = new StringBuilder("");

//		MetaDtoList metaDtoList = getMetaDtoList(files[0]);

		for (File child : files)
		{
			if (child.isDirectory())
			{
				trFolders.append("\n<tr>");

				trFolders.append("<td>")
						.append("<i class='bi bi-folder-fill fs-5'></i>&nbsp;")
						.append("<a href='./" + child.getName() + "/" + appPropreties.getAppPagenameIndex() + "'>" + child.getName() + "</a>")
						.append("</td>");
//				trFolders.append("<td>" + getResourceDate(metaDtoList, child) + "</td>");
				trFolders.append("<td></td>");
				trFolders.append("</tr>");
			} else
			{
				String uuid = Strings.getUUID();
				trFiles.append("\n<tr>");
				trFiles.append("<td>")
						.append(getIcon(child.getName().toLowerCase()))
//						.append("<a href='#' data-bs-toggle='modal' data-bs-target='#resourceModal'>" + child.getName() + "</a>")
//						.append("<a href='javascript:void(0)' onclick=\"onResourceClick('" + child.getName() + "')\" >" + child.getName() + "</a>")
						.append("<a href='javascript:void(0)' onclick='onResourceClick(this)' >" + child.getName() + "</a>")
						.append("</td>");
//				trFiles.append("<td>" + getResourceDate(metaDtoList, child) + "</td>");
				trFiles.append("<td class='text-end'>" + Files.getFileSize(child.length()) + "</td>");
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
//			consoleLogger.error("meta.json not found");
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

	public String getModelHtml()
	{
		return "\n" + fileHandler.fileReader("html/model.html", true);
	}

	public String getFooter()
	{
		return "" +
				"\n<footer class='bg-cprimary mt-5 border-top'>" +
				"<div class='p-5 text-md-end text-center'>" +
				"<div class='align-bottom text-cprimary'>" +
				"<img src='" + appPropreties.getAppImageLogo() + "' class='mb-1' alt='logo' width='48' />" +
				"<p class='fs-5 m-0'>" + appPropreties.getAppBrand() + "&trade;</p>" +
				"<p class='fs-6 m-0'>" + appPropreties.getVersion() + "</p>" +
				"</div>" +
				"</div>" +
				"</footer>";
	}

	public String endBody()
	{
		return "\n</body>\n<html>";
	}
}
