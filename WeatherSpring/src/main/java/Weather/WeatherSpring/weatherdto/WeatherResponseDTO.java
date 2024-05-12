package Weather.WeatherSpring.weatherdto;

import lombok.Data;

@Data
public class WeatherResponseDTO {
	private WeatherHeaderDTO header;

	private WeatherBodyDTO body;
}
