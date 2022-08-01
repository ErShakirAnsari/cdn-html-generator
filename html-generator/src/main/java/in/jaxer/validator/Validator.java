package in.jaxer.validator;

import java.io.File;

/**
 * @author Shakir
 * @date 28-07-2022
 * @since v1.0.0
 */
public interface Validator
{
	boolean isValid(File root);
}
