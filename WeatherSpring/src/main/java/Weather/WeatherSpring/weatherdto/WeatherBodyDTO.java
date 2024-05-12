package Weather.WeatherSpring.weatherdto;

import lombok.Data;

@Data
public class WeatherBodyDTO {
	
	private String dataType;

	private WeatherItemsDTO items;
}
