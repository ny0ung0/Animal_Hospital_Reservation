// EPSG:2097 (Bessel 중부원점TM) 정의
proj4.defs("EPSG:2097","+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +towgs84=-146.43,507.89,681.46 +units=m +no_defs");
// EPSG:4326 (WGS84) 정의
proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

var markers = [];
var infoWindows = [];

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
	        url:'/images/cur_location.png',
	        size: new naver.maps.Size(32, 32),
	        origin: new naver.maps.Point(0, 0),
	        anchor: new naver.maps.Point(25, 26)
		    }
		};
		var marker = new naver.maps.Marker(markerOptions);

		
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
		if(distance <= 3000){
			let markedVet = new naver.maps.Marker({
            map: map,
            position: new naver.maps.LatLng(lat, lng),
            title: hospital["사업장명"],
            icon: {
                url:'/images/pin_nomal.svg',
                size: new naver.maps.Size(50, 50),
                anchor: new naver.maps.Point(12, 37),
            }
        });
        var infoWindow = new naver.maps.InfoWindow({
        content: '<div style="width:150px;text-align:center;padding:10px;">'+hospital["사업장명"]+'</div>'
   		});
   		
   		let listItem = document.createElement("div");
   		listItem.innerHTML= '<button type="button" class="btn btn-main" data-bs-toggle="modal" data-bs-target="#exampleModal">'
   							+hospital["사업장명"] + '</button> , ' + hospital["소재지전화"]
   							+ ' , ' + hospital["소재지전체주소"]
   		 
   		document.querySelector(".vet_list").appendChild(listItem);
   		
   		//geocoding(내 주변에 뜬 병원의 좌표를 사용해서 내 db에 존재하는 병원인지 체크)
   		naver.maps.Service.reverseGeocode({
        coords: new naver.maps.LatLng(lat, lng),
	    }, function(status, response) {
	        if (status !== naver.maps.Service.Status.OK) {
	            return alert('Something wrong!');
	        }
	
	        var result = response.v2, // 검색 결과의 컨테이너
	            items = result.results, // 검색 결과의 배열
	            address = result.address; // 검색 결과로 만든 주소
	
	   
	        let addrs = result.address.jibunAddress.split(" ")
	        let addr = addrs[0]+"//" + addrs[1]
	       
	        console.log(hospital["사업장명"])
	        console.log(addrs)
	        
	        
	        const xhttp = new XMLHttpRequest();
		    xhttp.onload = function () {
		        if (this.status === 200) {
			       	let data = JSON.parse(this.responseText);
			       	data.forEach(hospital =>{
		       		let addr = hospital.address.replaceAll("//", " ")
		       		memVet[hospital.hospitalName] = {"address" : addr, 
										       		"avgReview" : hospital.avgReview,
										       		"bookmarked" : hospital.bookmarked,
										       		"businessNumber" : hospital.businessNumber,
										       		"email" : hospital.email,
										       		"introduction" : hospital.introduction,
										       		"logo" : hospital.logo,
										       		"representative" : hospital.representative,
										       		"partnership" : hospital.partnership, 
										       		"businessHours" : hospital.businessHours};
			       	})
		        }
//		        fetchHospitalData(guMap, Array.from(citiesWithNoGu));
		    };
		      
		 // GET 요청 URL에 쿼리 매개 변수로 데이터 추가
		    const params = new URLSearchParams();
		   
	    	params.append(hospital["사업장명"], addr);
		   
		    
		    const url = "http://localhost:9001/api/v1/near-vet-list?" + params.toString();
		    xhttp.open("GET", url, true);
		//    xhttp.setRequestHeader("username", 1);
		    xhttp.send();
	        
	        
	        
	        
	    });
   		
   		
        //marker/infoWindows array에 넣어주기
		markers.push(markedVet);
    	infoWindows.push(infoWindow);
		}
       
    });
    console.log(markers)
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
	
	for (var i=0, ii=markers.length; i<ii; i++) {
	    naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
	}
});
}
xhttp.open("GET", "/json/vet_list.json", true);
xhttp.send();


