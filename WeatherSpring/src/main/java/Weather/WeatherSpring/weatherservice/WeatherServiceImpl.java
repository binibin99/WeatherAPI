package Weather.WeatherSpring.weatherservice;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Weather.WeatherSpring.weatherdto.AreaRequestDTO;
import Weather.WeatherSpring.weatherdto.WeatherApiResponseDTO;
import Weather.WeatherSpring.weatherdto.WeatherDTO;
import Weather.WeatherSpring.weatherdto.WeatherItemDTO;
import Weather.WeatherSpring.weathermapper.WeatherMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Repository
public class WeatherServiceImpl implements WeatherService {

	@Autowired
	private WeatherMapper weatherMapper;

	@Override
	public List<WeatherDTO> getWeather(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException {
		List<WeatherDTO> weatherList = this.weatherMapper.selectSameCoordinateWeatherList(areaRequestDTO); 
		// 날짜, 시간, nx, ny가 requestDTO의 값과 일치하는 데이터가 있는지 검색
		if (weatherList.isEmpty()) {
			ResponseEntity<WeatherApiResponseDTO> response = requestWeatherApi(areaRequestDTO); // 데이터가 하나도 없는 경우 새로 생성
			ObjectMapper objectMapper = new ObjectMapper();
			List<WeatherItemDTO> weatherItemList = response.getBody().getResponse().getBody().getItems().getItem();
			for (WeatherItemDTO item : weatherItemList) {
				weatherList.add(objectMapper.readValue(objectMapper.writeValueAsString(item), WeatherDTO.class));
			}
			this.weatherMapper.insertWeatherList(weatherList); // API요청 후 결과값을 DB에 저장

			return weatherList; // 로그를 찍지 않으려면 삭제해도 OK
		}
		return weatherList; // DB에 기존 저장되어있는 값에서 가져온 List
	}

	@Override
	public List<AreaRequestDTO> getArea(Map<String, String> params) {
		return this.weatherMapper.selectArea(params);
	}

	@Override
	public AreaRequestDTO getCoordinate(String areacode) {
		return this.weatherMapper.selectCoordinate(areacode);
	}

	@Override
	public ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO)
			throws UnsupportedEncodingException, URISyntaxException {
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
		String serviceKey = "디코딩된 서비스 키를 입력해주세요";
		String encodedServiceKey = URLEncoder.encode(serviceKey, "UTF-8");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "JSON", Charset.forName("UTF-8")));

		StringBuilder builder = new StringBuilder(url);
		builder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + encodedServiceKey);
		builder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
		builder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
		builder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
		builder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
				+ URLEncoder.encode(areaRequestDTO.getBaseDate(), "UTF-8"));
		builder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
				+ URLEncoder.encode(areaRequestDTO.getBaseTime(), "UTF-8"));
		builder.append(
				"&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNx(), "UTF-8"));
		builder.append(
				"&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNy(), "UTF-8"));
		URI uri = new URI(builder.toString());

		ResponseEntity<WeatherApiResponseDTO> response = restTemplate.exchange(uri, HttpMethod.GET,
				new HttpEntity<String>(headers), WeatherApiResponseDTO.class);
		return response;
	}
}
