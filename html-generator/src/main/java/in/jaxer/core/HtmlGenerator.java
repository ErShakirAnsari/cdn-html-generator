package in.jaxer.core;

import in.jaxer.core.utilities.JValidator;
import in.jaxer.dto.NavbarItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Shakir
 * @date 23-07-2022
 * @since v1.0.0
 */
@Component
public class HtmlGenerator
{
	@Value("${app.brand}")
	private String appTitle;

	@Value("${app.brand}")
	private String appBrand;

	private String getApplicationLogoLink()
	{
		return "https://jaxer-in.github.io/cdn/cdn-resources/images/logo.svg";
	}

	private String getApplicationCssLink()
	{
		return "https://jaxer-in.github.io/cdn/cdn-resources/css/main.css";
	}

	private String getBootstrapCssLink()
	{
		return "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css";
	}

	private String getBootstrapIconCssLink()
	{
		return "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css";
	}

	private String getBootstrapJsLink()
	{
		return "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
	}

	public String getHtmlHead(String title)
	{
		return "" +
				"<head>" +
				"<meta charset='UTF-8' />\n" +
				"<meta http-equiv='X-UA-Compatible' content='IE=edge' />\n" +
				"<meta name='viewport' content='width=device-width, initial-scale=1.0' />\n" +
				"<link rel='stylesheet' href='" + getBootstrapIconCssLink() + "' />\n" +
				"<link rel='stylesheet' href='" + getBootstrapCssLink() + "'/>\n" +
				"<script defer src='" + getBootstrapJsLink() + "'></script>\n" +
				"<link rel='stylesheet' href='" + getApplicationCssLink() + "' />\n" +
				"<title>" + appTitle + " - " + title + "</title>\n" +
				"</head>";
	}

	public String getBodyHeader()
	{
		return "" +
				"<header class='text-cprimary ps-md-5 ps-2'>\n" +
				"<h1 class='text-md-start text-center'>CDN</h1>\n" +
				"<p class='text-md-start text-center'>Private content delivery network by " + appTitle + "&trade; for personal use only.</p>\n" +
				"</header>";
	}

	public String getBreadcrum(List<NavbarItemDto> navbarItemDtoList)
	{
		String navbar = ""
				+ "<nav aria-label='breadcrumb'>\n"
				+ "<ol class='breadcrumb fs-6 p-2 text-cprimary fs-5'>\n"
				+ "<li class='breadcrumb-item'><i class='bi bi-house'></i><a href='#'>Home</a></li>\n";

		if (JValidator.isNotNullAndNotEmpty(navbarItemDtoList))
		{
			int size = navbarItemDtoList.size();
			for (int i = 0; i < size; i++)
			{
				NavbarItemDto itemDto = navbarItemDtoList.get(i);

				if (i + 1 == size)
				{
					navbar += "<li class='breadcrumb-item active' aria-current='page'>" + itemDto.getTitle() + "</li>\n";
				} else
				{
					navbar += "<li class='breadcrumb-item' aria-current='page'>" +
							"<a href='" + itemDto.getLink() + "'>" + itemDto.getTitle() + "</a>" +
							"</li>\n";
				}
			}
		}
		navbar += "" +
				"</ol>\n" +
				"</nav>";
		return navbar;
	}

	private String getFooter()
	{
		return "" +
				"<footer class='bg-cprimary mt-5 border-top'>\n" +
				"<div class='p-5 text-md-end text-center'>\n" +
				"<div class='align-bottom text-cprimary'>\n" +
				"<img src='" + getApplicationLogoLink() + "' class='mb-1' alt='logo' width='48' />\n" +
				"<p class='fs-5 m-0'>" + appBrand + "&trade;</p>\n" +
				"<p class='fs-6 m-0'>v2022-07-23</p>\n" +
				"</div>\n" +
				"</div>\n" +
				"</footer>";
	}
}
