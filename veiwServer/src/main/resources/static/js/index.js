// EPSG:2097 (Bessel 중부원점TM) 정의
proj4.defs("EPSG:2097","+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +towgs84=-146.43,507.89,681.46 +units=m +no_defs");
// EPSG:4326 (WGS84) 정의
proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

var markers = [];
var infoWindows = [];
let memVet = {};
let nearVet = [];




const xhttp = new XMLHttpRequest();
xhttp.onload = function() {
    let data = JSON.parse(this.responseText);
    let hospitals = data.동물병원;

    // 네이버 지도 객체 생성
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.3595704, 127.105399),
        zoom: 15
    });
    

    // 현재 위치 가져오기
    navigator.geolocation.getCurrentPosition(function(position) {
        var currentPos = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
        map.setCenter(currentPos);
        
        var markerOptions = {
            position: new naver.maps.LatLng(currentPos),
            map: map,
            icon: {
                url:"/images/current.png",
                size: new naver.maps.Size(32, 32),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(25, 26)
            },
        };
        var marker = new naver.maps.Marker(markerOptions);
       //마커의 현재위치 클릭
        naver.maps.Event.addListener(marker, 'click', function() {
    	  map.panTo(currentPos);
	    });

		document.querySelector("#curBtn").addEventListener('click', function(e) {
		  e.preventDefault();	
    	  map.panTo(currentPos);
	    });

        // 반경 1km 이내의 병원 필터링
        var nearbyHospitals = hospitals.filter(function(hospital) {
            //json 내의 위치정보 위도 경도로 변경하기
            let x = parseFloat(hospital["좌표정보(x)"]);
            let y = parseFloat(hospital["좌표정보(y)"]);

            var wgs84 = proj4('EPSG:2097', 'EPSG:4326', [x, y]);

            let lat = wgs84[1];
            let lng = wgs84[0];
            var hospitalPos = new naver.maps.LatLng(lat, lng);

            //현재 내 위치와 거리가 1km미만인 병원 구하기
            const projection = map.getProjection();
            const distance = projection.getDistance(currentPos, hospitalPos);
            return distance <= 3000;
        });

        // 필터링된 병원 마커 추가 및 params 설정
        let params = new URLSearchParams();
        Promise.all(nearbyHospitals.map(hospital => {
            return new Promise((resolve, reject) => {
                let x = parseFloat(hospital["좌표정보(x)"]);
                let y = parseFloat(hospital["좌표정보(y)"]);

                var wgs84 = proj4('EPSG:2097', 'EPSG:4326', [x, y]);
                let lat = wgs84[1];
                let lng = wgs84[0];

                nearVet.push(hospital);

                // Reverse Geocode
                naver.maps.Service.reverseGeocode({
                    coords: new naver.maps.LatLng(lat, lng),
                }, function(status, response) {
                    if (status !== naver.maps.Service.Status.OK) {
                        return reject('Something went wrong!');
                    }

                    var result = response.v2; // 검색 결과의 컨테이너
                    var addrs = result.address.jibunAddress.split(" ");
                    var addr = addrs[0] + "//" + addrs[1];
                    params.append(hospital["사업장명"], addr);

                    resolve();
                });
            });
        })).then(() => {
            // All reverse geocodes are done
            getMemVerList(params, map, currentPos);
        }).catch(error => {
            console.error(error);
        });

        console.log(markers);
        console.log(nearVet);
    });
}
xhttp.open("GET", "/json/vet_list.json", true);
xhttp.send();

function getMemVerList(params, map, currentPos) {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.status === 200) {
            let data = JSON.parse(this.responseText);
            data.forEach(hospital => {
                let addr = hospital.address.replaceAll("//", " ");
                memVet[hospital.hospitalName] = {
					"id":hospital.id,
                    "address": addr,
                    "avgReview": hospital.avgReview,
                    "bookmarked": hospital.bookmarked,
                    "businessNumber": hospital.businessNumber,
                    "email": hospital.email,
                    "introduction": hospital.introduction,
                    "logo": hospital.logo,
                    "representative": hospital.representative,
                    "partnership": hospital.partnership,
                    "businessHours": hospital.businessHours
                };
            });
        }
        addHospitalToList(map, currentPos);
    };

    const url = "http://localhost:9001/api/v1/near-vet-list?" + params.toString();
    xhttp.open("GET", url, true);
    xhttp.send();
}

function addHospitalToList(map, currentPos) {
//    console.log(memVet)
    nearVet.forEach(hospital => {
        let x = parseFloat(hospital["좌표정보(x)"]);
        let y = parseFloat(hospital["좌표정보(y)"]);
        var wgs84 = proj4('EPSG:2097', 'EPSG:4326', [x, y]);
        let lat = wgs84[1];
        let lng = wgs84[0];
        let markerIcon = '/images/pin_nomal.svg'; // Default marker icon

        if (memVet[hospital["사업장명"]]) {
            if (memVet[hospital["사업장명"]]["partnership"]) {
                markerIcon = '/images/pin_p.svg'; // Partnership marker icon
            } else {
                markerIcon = '/images/pin.svg'; // MemVet marker icon
            }
        }

        var markedVet = new naver.maps.Marker({
            map: map,
            position: new naver.maps.LatLng(lat, lng),
            title: hospital["사업장명"],
            icon: {
                url: markerIcon,
                size: new naver.maps.Size(50, 50),
                anchor: new naver.maps.Point(12, 37),
            }
        });
        
        
	
         naver.maps.Event.addListener(markedVet, 'click', function() {
    	  map.panTo(markedVet.getPosition());
	    });


        var infoWindow = new naver.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:10px;" onclick=" showModal(event)" data-bs-toggle="modal" data-bs-target="#exampleModal">' + hospital["사업장명"] 
            + '</div><span class="address" style="display:none;">' + hospital["도로명전체주소"] 
            + '</span><span class="phone" style="display:none;">' + hospital["소재지전화"] + '</span>'
        });
        
        

        markers.push(markedVet);
        infoWindows.push(infoWindow);

        let phone = hospital["소재지전화"] ? hospital["소재지전화"] : '';
        let listItem = document.createElement("div");
      
        listItem.innerHTML = '<button type="button" onclick="showModal(event)" class="btn btn-hospital-sub" data-bs-toggle="modal" data-bs-target="#exampleModal">'
            + hospital["사업장명"] + '</button> <img style="width:35px; display:none;" src="/images/pin_p.svg"/><i class="bi bi-bookmark-heart fs-4"></i>, <span class="phone">' + phone
            + '</span> , <span class="address">' + hospital["소재지전체주소"] + '</span>';
        document.querySelector(".inner").appendChild(listItem);

        if (memVet[hospital["사업장명"]] != null && memVet[hospital["사업장명"]]["address"] == hospital["소재지전체주소"]) {
            listItem.querySelector("button").classList = "btn btn-user-sub"
            if (memVet[hospital["사업장명"]]["partnership"] == true) {
                listItem.querySelector("img").style.display="inline-block"
            }
        }
    });

    // 해당 마커의 인덱스를 seq라는 클로저 변수로 저장하는 이벤트 핸들러를 반환합니다.
    function getClickHandler(seq) {
        return function(e) {
            var marker = markers[seq],
                infoWindow = infoWindows[seq];

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        }
    }

    for (var i = 0, ii = markers.length; i < ii; i++) {
        naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
    }
}







document.querySelector(".inner").addEventListener("click", function(e){
	e.preventDefault();
	console.log(e.target)
})




// 모달에 해당 병원 상세정보 보여주기
function showModal(e) {
	console.log("Xxxx");
	let hospitalName;
	let address;
	let phone;
	
	if(e.target.parentElement.querySelector("button") == null){
		hospitalName = e.target.innerText;
		address= e.target.parentElement.querySelector(".address").innerText;
		phone = e.target.parentElement.querySelector(".phone").innerText;
		
		console.log(hospitalName)
		console.log(address)
		console.log(phone)
	}else{
		hospitalName = e.target.parentElement.querySelector("button").innerText;
		address = e.target.parentElement.querySelector(".address").innerText;
		phone = e.target.parentElement.querySelector(".phone").innerText;
	}
    
    if (memVet[hospitalName] != null && memVet[hospitalName]["address"] == address) {
		 document.querySelector("#working_hour").innerHTML="";
		let basicHours =JSON.parse(memVet[hospitalName]["businessHours"]);
	    //0:일, 1:월, 2:화 .... 6:토
	    let hoursArr = getBasicBusinessHours(basicHours);
		
		
		//병원 id 심어주기
		document.querySelector("#hospital_id").innerHTML = memVet[hospitalName]["id"];
        // 영업시간 보여주기
        document.querySelector("#working_hour").style.display = "block";
        showBusinessHour(hoursArr);
        
        // 리뷰 보여주기
        document.querySelector("#review").style.display = "block";
        document.querySelector("#review").innerHTML = memVet[hospitalName]["review"];
        // 예약하기 버튼 보여주기
        document.querySelector(".reservationBtn").style.display = "block";
        document.querySelector("#point").style.display = "block";
        // 북마크 버튼 보여주기
        document.querySelector("#bookmarked").style.display = "block";
        document.querySelector("#bookmarked").innerHTML = memVet[hospitalName]["bookmarked"];
        // 사업자번호 버튼 보여주기
        document.querySelector("#businessNumber").style.display = "block";
        document.querySelector("#businessNumber").innerHTML = memVet[hospitalName]["businessNumber"];
        // 이메일 버튼 보여주기
        document.querySelector("#email").style.display = "block";
        document.querySelector("#email").innerHTML = memVet[hospitalName]["email"];
        // 소개글 버튼 보여주기
        document.querySelector("#introduction").style.display = "block";
        document.querySelector("#introduction").innerHTML = memVet[hospitalName]["introduction"];
        // 로고 버튼 보여주기
        document.querySelector("#logo").style.display = "block";
        document.querySelector("#logo").innerHTML = memVet[hospitalName]["logo"];
        // 대표자 버튼 보여주기
        document.querySelector("#representative").style.display = "block";
        document.querySelector("#representative").innerHTML = memVet[hospitalName]["representative"];
        // 평균별점 버튼 보여주기
        document.querySelector("#avgReview").style.display = "block";
        document.querySelector("#avgReview").innerHTML = memVet[hospitalName]["avgReview"];
        if (memVet[hospitalName]["partnership"] == true) {
            // 포인트제휴여부 보여주기
            document.querySelector("#point").innerHTML = "포인트제휴병원 ⭕";
        } else {
            document.querySelector("#point").innerHTML = "포인트제휴병원 ❌";
        }
    }
    document.querySelector("#exampleModalLabel").innerText = hospitalName;
    document.querySelector("#phone").innerHTML = phone;
    document.querySelector("#address").innerHTML = address;
}

function makeReservation(e){
	let id = e.target.parentElement.querySelector("#hospital_id").innerText;
	location.href="/user/reserv_form?id="+id;
}

function convertingDate(basicHours, day){
	let startTime;
	let endingTime;
	let lunchStart;
	let lunchEnd;
	//영업하는 날 시간 구하기
	if(basicHours[day][0].slice(7) !="영업 안함"){
		startTime = basicHours[day][0].slice(7).split("//")[0];
		endingTime = basicHours[day][0].slice(7).split("//")[1];
		//영업하면서&점심시간있음
		if(basicHours[day][1].slice(7) !="점심시간 없음"){
			lunchStart = basicHours[day][1].slice(7).split("//")[0];
			lunchEnd = basicHours["mon"][1].slice(7).split("//")[1];
		}else{
			//영업하지만&점심시간없음
			lunchStart = 0;
			lunchEnd = 0;
		}
	}else{
		//영업 안 하는 날
		startTime = 0;
		endingTime = 0;
		lunchStart = 0;
		lunchEnd = 0;
	}
	return {day : [startTime, endingTime, lunchStart, lunchEnd]};
}

function getBasicBusinessHours(basicHours){
	let sun = convertingDate(basicHours, "sun");
	let mon = convertingDate(basicHours, "mon");
	let tue = convertingDate(basicHours, "tue");
	let wed = convertingDate(basicHours, "wed");
	let thu = convertingDate(basicHours, "thu");
	let fri = convertingDate(basicHours, "fri");
	let sat = convertingDate(basicHours, "sat");
	let hol = convertingDate(basicHours, "hol");
	return [sun, mon, tue, wed, thu, fri, sat, hol];
}

function showBusinessHour(hoursArr){
    let week = ["일", "월", "화", "수", "목", "금", "토", "공휴일"];
    //0:일, 1:월, 2:화 .... 6:토
    for(let i = 0; i < hoursArr.length; i++){
        let day = hoursArr[i];
        let startTime = day.day[0];
        let endTime = day.day[1];
        let lunchStart = day.day[2];
        let lunchEnd = day.day[3];
        
        let listItem = document.createElement("div");
        listItem.innerHTML = week[i] + " - 영업시간 : <span class='workHour'>" + startTime + " ~ " + endTime
                             +"</span> , 점심시간 : <span class='lunchHour'>" + lunchStart + " ~ " + lunchEnd +"</span>";

        if(startTime === 0){
            listItem.querySelector(".workHour").innerHTML = "휴무";
        }
        if(lunchStart === 0){
            listItem.querySelector(".lunchHour").innerHTML = "-";
        }
        
        document.querySelector("#working_hour").appendChild(listItem);
    }
}