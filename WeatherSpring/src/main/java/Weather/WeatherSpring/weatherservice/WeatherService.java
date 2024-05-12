package Weather.WeatherSpring.weatherservice;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import Weather.WeatherSpring.weatherdto.AreaRequestDTO;
import Weather.WeatherSpring.weatherdto.WeatherApiResponseDTO;
import Weather.WeatherSpring.weatherdto.WeatherDTO;

@Service
@Component
public interface WeatherService {
	List<WeatherDTO> getWeather(AreaRequestDTO areaRequestDTO)
			throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException;

	List<AreaRequestDTO> getArea(Map<String, String> params);

	AreaRequestDTO getCoordinate(String areacode);

	ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO)
			throws UnsupportedEncodingException, URISyntaxException;
}
