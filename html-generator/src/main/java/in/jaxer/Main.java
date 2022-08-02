package in.jaxer;

import in.jaxer.core.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Shakir
 * @date 01-08-2022
 * @since v1.0.0
 */
public class Main
{
	public static void main(String[] args)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		Application application = context.getBean(Application.class);
		application.start();
	}
}
