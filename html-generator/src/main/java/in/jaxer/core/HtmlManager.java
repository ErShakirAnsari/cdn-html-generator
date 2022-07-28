package in.jaxer.core;

import in.jaxer.core.utilities.Files;
import in.jaxer.core.utilities.JValidator;
import in.jaxer.core.utilities.Strings;
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
				"<head>" +
				"<meta charset='UTF-8' />" +
				"<meta http-equiv='X-UA-Compatible' content='IE=edge' />" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
				"<link rel='stylesheet' href='" + appPropreties.getBootstrapCssIcon() + "' />" +
				"<link rel='stylesheet' href='" + appPropreties.getBootstrapCss() + "'/>" +
				"<script defer src='" + appPropreties.getBootstrapJs() + "'></script>" +
				"<link rel='stylesheet' href='" + appPropreties.getAppCssMain() + "' />" +
				"<script defer src='" + appPropreties.getApplicationJs() + "'></script>" +
				"<title>" + appPropreties.getAppTitle() + " - " + title + "</title>" +
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
		StringBuilder navbar = new StringBuilder("");

		navbar.append("\n<nav aria-label='breadcrumb'>")
				.append("<ol class='breadcrumb p-2 fs-6 text-cprimary fs-5'>");

		if (remainingPath.isEmpty())
		{
			navbar.append("\n<li class='breadcrumb-item active'><i class='bi bi-house fs-5'></i>&nbsp;Home</li>");
		} else
		{
			boolean homeFlag = true;
			String[] paths = remainingPath.split(File.separator + File.separator);
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
								.append("<a href='../" + relativeBackPath + appPropreties.getAppHtmlPagename() + "'>Home</a>")
								.append("</li>");
						homeFlag = false;
					}

					if (i + 1 == paths.length)
					{
						navbar.append("\n<li class='breadcrumb-item active' aria-current='page'>" + path + "</li>");
					} else
					{
						navbar.append("\n<li class='breadcrumb-item' aria-current='page'>")
								.append("<a href='" + relativeBackPath + appPropreties.getAppHtmlPagename() + "'>" + path + "</a>")
								.append("</li>");
					}
				}
			}
		}
		navbar.append("</ol></nav>");
		return navbar.toString();
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
				String uuid = Strings.getUUID();
				trFiles.append("\n<tr>");
				trFiles.append("<td>")
						.append(getIcon(child.getName().toLowerCase()))
//						.append("<a href='#' data-bs-toggle='modal' data-bs-target='#resourceModal'>" + child.getName() + "</a>")
						.append("<a href='javascript:void(0)' onclick='onResourceClick(\"" + uuid + "\")' id='" + uuid + "'>" + child.getName() + "</a>")
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

	public String getModelHtml()
	{
		StringBuilder modelHtml = new StringBuilder();

		modelHtml.append("\n<div class='modal modal-lg fade' id='resourceModal' tabindex='-1' aria-labelledby='resourceModalLabel' aria-hidden='true'>")
				.append("<div class='modal-dialog'>")
				.append("<div class='modal-content bg-cprimary'>")
				.append("<div class='modal-body'>")
				.append("<div class='p-4 pb-3'>")
				.append("<div class='row'>")
				.append("<div class='col-12'>")
				.append("<div>")
				.append("<div>CDN provider &mdash;</div>")
				.append("<div class='form-check form-check-inline'>")
				.append("<label class='form-check-label cursor-pointer'>")
				.append("<input class='form-check-input' type='radio' name='cdnProvider' value='github' onchange='onChangeCdnProvide()' />")
				.append("Github</label>")
				.append("</div>")
				.append("<div class='form-check form-check-inline'>")
				.append("<label class='form-check-label cursor-pointer'>")
				.append("<input class='form-check-input' type='radio' name='cdnProvider' value='cloudflare' onchange='onChangeCdnProvide()' />")
				.append("Cloudflare</label>")
				.append("</div>")
				.append("<div class='form-check form-check-inline'>")
				.append("<label class='form-check-label cursor-pointer'>")
				.append("<input class='form-check-input' type='radio' name='cdnProvider' value='jsdelivery' onchange='onChangeCdnProvide()' checked='checked' />")
				.append("jsDelivery</label>")
				.append("</div>")
				.append("</div>")
				.append("<div class='my-2'>")
				.append("<div class='mb-2'><div>Raw url</div><code id='idCodeRawUrl'></code></div>")
				.append("<div class='mb-0'><div>HTML embedded</div><code id='idCodeEmbeddedUrl'></code></div>")
				.append("</div>")
				.append("</div>")
				.append("<div class='col-12 mt-3'>")
				.append("<div class='row'>")
				.append("<div class='col-sm-12 col-lg-4 mb-1'>")
				.append("<button class='btn btn-light w-100' onclick='onClickCopyRawUrl()'><i class='bi bi-clipboard2'></i>&nbsp;Copy raw url</button>")
				.append("</div>")
				.append("<div class='col-sm-12 col-lg-4 mb-1'>")
				.append("<button class='btn btn-light w-100' onclick='onClickCopyEmbeddedUrl()'><i class='bi bi-clipboard2-data'></i>&nbsp;Copy html/embedded</button>")
				.append("</div>")
				.append("<div class='col-sm-12 col-lg-4 mb-1'>")
				.append("<button class='btn btn-light w-100' onclick='onClickCopyDownloadFile()'><i class='bi bi-download'></i> Download file</button>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>")
				.append("</div>");

		return modelHtml.toString();
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
