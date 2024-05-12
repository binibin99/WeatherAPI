function getWeather() {
    var nullCheck = true;
    $('.emptyCheck').each(function (){
        if ('' == $(this).val()){
            alert($(this).attr('title') + "을(를) 확인바람");
            nullCheck = false;
            return false;	// 빈 값에서 멈춤
        }
    });

    if (nullCheck) {
        var time = $('#time').val() + '00';
        if ($('#time').val() < 10) {
            time = "0" + time;
        }
        var areacode = "";
        var cityCode = $('#step1 option:selected').val();
        var countyCode = $('#step2 option:selected').val();
        var townCode = $('#step3 option:selected').val();

        if (townCode == '' && countyCode == '') {
            areacode = cityCode;
        } else if (townCode == '' && countyCode != '') {
            areacode = countyCode;
        } else if (townCode != '') {
            areacode = townCode;
        }

        var date = $('#datepicker').val();
        var data = {"areacode": areacode, "baseDate": date, "baseTime": time};

        $.ajax({
            url: "/board/getWeather.do",
            data: data,
            dataType: "JSON",
            method: "POST",
            success: function (res) {
                console.log(res);
                if (res[0].resultCode != null) {
                    alert(res[0].resultMsg);
                } else {
                    displayWeatherData(res); // 서버 응답 데이터를 표시하는 함수 호출
                }
            },
           error: function (xhr) {
        if (xhr.status === 500) {
            alert("서버에서 데이터를 불러오지 못했습니다.\n 나중에 다시 시도해주세요.");
        } else {
            alert("알 수 없는 오류가 발생했습니다.");
        }
    }
        });
    }
}

function displayWeatherData(weatherData) {
    var html = "<div class='weather-container'><div class='weather-info'><div class='coordinates'>nx: " + weatherData[0].nx + ", ny: " + weatherData[0].ny + "</div>";
    html += "<table class='weather-table'><thead><tr><th>항목</th><th>값</th></tr></thead><tbody>";

    $.each(weatherData, function (index, item) {
        html += "<tr><td>" + getCategoryName(item.category) + "</td><td>" + item.obsrValue + "</td></tr>";
    });

    html += "</tbody></table></div></div>";
    $("#resultWeather").empty().append(html);
}

function getCategoryName(category) {

    switch (category) {
        case 'PTY':
            return '강수형태';
        case 'REH':
            return '습도';
        case 'RN1':
            return '1시간 강수량';
        case 'T1H':
            return '기온';
        case 'UUU':
            return '동서바람성분';
        case 'VEC':
            return '풍향';
        case 'VVV':
            return '남북바람성분';
        case 'WSD':
            return '풍속';
        default:
            return category;
    }
}
