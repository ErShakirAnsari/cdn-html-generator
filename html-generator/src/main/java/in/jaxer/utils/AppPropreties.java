package in.jaxer.utils;

import in.jaxer.core.ConsoleLogger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
@Configuration
@PropertySource("classpath:app.properties")
@PropertySource(value = "file:${user.home}/html-cdn-generator.properties", ignoreResourceNotFound = true)
@Getter
public class AppPropreties
{
	@Value("${app.profile}")
	private String appProfile;

	@Value("${app.title}")
	private String appTitle;

	@Value("${app.brand}")
	private String appBrand;

	@Value("${app.ignore.folders}")
	private String[] ignoreFolders;

	@Value("${app.ignore.resource.folders}")
	private String[] ignoreResourceFolders;

	@Value("${app.ignore.resource.files}")
	private String[] ignoreResourceFiles;

	@Value("${app.file.cdn}")
	private String fileCdn;

	@Value("${app.file.meta.json}")
	private String metaJsonFileName;

	@Value("${app.file.root.path}")
	private String rootPath;

	@Value("${app.logger.type:debug}")
	private ConsoleLogger.ConsoleLoggerType loggerType;

	@Value("${app.pagename.index}")
	private String appPagenameIndex;

	@Value("${app.pagename.demo}")
	private String appPagenameDemo;

//	@Value("${app.js.main}")
//	private String applicationJs;
//
//	@Value("${app.css.main}")
//	private String appCssMain;
//
//	@Value("${app.image.logo}")
//	private String appImageLogo;

	@Value("${3p.bootstrap.css}")
	private String bootstrapCss;

	@Value("${3p.bootstrap.css.icon}")
	private String bootstrapCssIcon;

	@Value("${3p.bootstrap.js}")
	private String bootstrapJs;

	public String getVersion()
	{
		return "v" + new SimpleDateFormat("yyMMdd").format(new Date());
	}

	public String getAppCssMain()
	{
		return "https://cdn.jsdelivr.net/gh/jaxer-in/cdn@v" + getVersion() + "/.resources/css/main.min.css";
	}

	public String getApplicationJs()
	{
		return "https://cdn.jsdelivr.net/gh/jaxer-in/cdn@v" + getVersion() + "/.resources/js/script.min.js";
	}

	public String getAppImageLogo()
	{
		return "https://cdn.jsdelivr.net/gh/jaxer-in/cdn@" + getVersion() + "/.resources/images/logo.min.svg";
	}
}
