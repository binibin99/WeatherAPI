window.onload = function() {
    $('#datepicker').datepicker({
        showOn: 'button',
        buttonImage: 'http://jqueryui.com/resources/demos/datepicker/images/calendar.gif',
        buttonImageOnly: true,
        showButtonPanel: true,
        dateFormat: 'yymmdd',
        minDate: "-1D",
        maxDate: 0,
        closeText: "닫기",
        currentText: "오늘",
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        weekHeader: "주",
        yearSuffix: '년',
        showMonthAfterYear: true
    });

    // 지역 정보 로드
    loadArea('city');
};

// 시/도 변경 시
$('#step1').on("change", function() {
    loadArea('county', $(this));
});

// 시/군/구 변경 시
$('#step2').on("change", function() {
    loadArea('town', $(this));
});

// 지역 정보 로드 함수
function loadArea(type, element) {
    var value = $(element).find('option:selected').text();
    var data = { type: type, keyword: value };

    console.log(data);
    $.ajax({
        url: "/board/weatherStep.do",
        data: data,
        dataType: "JSON",
        method: "POST",
        success: function(res) {
            if (type == 'city') {
                res.forEach(function(city) {
                    $('#step1').append('<option value="' + city.areacode + '">' + city.step1 + '</option>');
                });
            } else if (type == 'county') {
                $('#county').siblings().remove();
                $('#town').siblings().remove();
                res.forEach(function(county) {
                    $('#step2').append('<option value="' + county.areacode + '">' + county.step2 + '</option>');
                });
            } else {
                $('#town').siblings().remove();
                res.forEach(function(town) {
                    $('#step3').append('<option value="' + town.areacode + '">' + town.step3 + '</option>');
                });
            }
        },
        error: function(xhr) {
            alert(xhr.responseText);
        }
    });
}

// 날짜 선택 시
$('#datepicker').on("change", function() {
    $('#time option:eq(0)').prop("selected", true);
    var now = new Date();
    var currentHour = now.getHours();
    var month = (now.getMonth() + 1) < 10 ? "0" + (now.getMonth() + 1) : (now.getMonth() + 1);
    var nowDate = now.getDate() < 10 ? "0" + now.getDate() : now.getDate();
    var today = now.getFullYear() + "" + month + "" + nowDate;
    var datepickerValue = $(this).val();

    /* 오늘 날짜라면 */
    $('#time').children('option:not(:first)').remove();
    var html = '';
    if (datepickerValue == today) {
        for (var i = 0; i <= currentHour; i++) {
            html += '<option value="' + i + '">' + i + '시</option>';
        }
    }
    /* 어제였다면 */
    else {
        if (currentHour != 23) {
            for (var i = currentHour + 1; i < 24; i++) {
                html += '<option value="' + i + '">' + i + '시</option>';
            }
        } else {
            alert(datepickerValue + " 날짜의 제공 가능한 날씨 데이터가 없습니다.");
            $(this).val('');
        }
    }
    $('#time').append(html);
});
