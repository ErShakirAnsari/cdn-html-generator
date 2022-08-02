package in.jaxer.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author Shakir
 * @date 26-07-2022
 * @since v1.0.0
 */
@Data
public class MetaDtoList
{
	@SerializedName(value = "resource-list")
	private List<MetaDto> metaDtoList;
}
