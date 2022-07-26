package in.jaxer.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
@Configuration
@PropertySource("classpath:app.properties")
//@PropertySource(value = "classpath:root.properties", ignoreResourceNotFound = true)
@Getter
public class AppPropreties
{
	@Value("${app.title}")
	private String appTitle;

	@Value("${app.brand}")
	private String appBrand;

	@Value("${app.file.ignoreFolders}")
	private String[] ignoreFolders;

	@Value("${app.file.ignoreFiles}")
	private String[] ignoreFiles;

	@Value("${app.file.meta.json}")
	private String metaJsonFileName;

	@Value("${app.html.pagename}")
	private String appHtmlPagename;

	@Value("${app.css.main}")
	private String appCssMain;

	@Value("${app.image.logo}")
	private String appImageLogo;

	@Value("${app.published.date}")
	private String appPublishedDate;

	@Value("${3p.bootstrap.css}")
	private String bootstrapCss;

	@Value("${3p.bootstrap.css.icon}")
	private String bootstrapCssIcon;

	@Value("${3p.bootstrap.js}")
	private String bootstrapJs;
}
