package Weather.WeatherSpring.weathermapper;

import java.util.List;
import java.util.Map;

import Weather.WeatherSpring.weatherdto.AreaRequestDTO;
import Weather.WeatherSpring.weatherdto.WeatherDTO;

public interface WeatherMapper {

	List<AreaRequestDTO> selectArea(Map<String, String> params);

	AreaRequestDTO selectCoordinate(String areacode);

	List<WeatherDTO> selectSameCoordinateWeatherList(AreaRequestDTO areaRequestDTO);

	void insertWeatherList(List<WeatherDTO> weatherList);

}
