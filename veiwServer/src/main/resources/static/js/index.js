// EPSG:2097 (Bessel 중부원점TM) 정의
proj4.defs("EPSG:2097","+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +towgs84=-146.43,507.89,681.46 +units=m +no_defs");
// EPSG:4326 (WGS84) 정의
proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

if (!window.markers) window.markers = [];
if (!window.infoWindows) window.infoWindows = [];
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
	if (navigator.geolocation) {
	    navigator.geolocation.getCurrentPosition(
	        async function (position) {
	            try {
	                var currentPos = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
	                map.setCenter(currentPos);
	
	                var markerOptions = {
	                    position: currentPos,
	                    map: map,
	                    icon: {
	                        url: "/images/current_small.png",
	                        size: new naver.maps.Size(32, 32),
	                        origin: new naver.maps.Point(0, 0),
	                        anchor: new naver.maps.Point(25, 26),
	                    },
	                    animation: naver.maps.Animation.BOUNCE,
	                };
	                var marker = new naver.maps.Marker(markerOptions);
	
	                // 마커의 현재위치 클릭
	                naver.maps.Event.addListener(marker, 'click', function () {
	                    map.panTo(currentPos);
	                });
	
	                // 오른쪽 위 항상 내위치 찾기 버튼
	                document.querySelector("#curBtn").addEventListener('click', function (e) {
	                    e.preventDefault();
	                    map.setZoom(15);
	                    map.panTo(currentPos);
	                    
	                    
	                });
	
	                // 반경 2km 이내의 병원 필터링
	                var nearbyHospitals = hospitals.filter(function (hospital) {
	                    // JSON 내의 위치정보 위도 경도로 변경하기
	                    let x = parseFloat(hospital["좌표정보(x)"]);
	                    let y = parseFloat(hospital["좌표정보(y)"]);
	
	                    var wgs84 = proj4('EPSG:2097', 'EPSG:4326', [x, y]);
	
	                    let lat = wgs84[1];
	                    let lng = wgs84[0];
	                    var hospitalPos = new naver.maps.LatLng(lat, lng);
	
	                    // 현재 내 위치와 거리가 2km 미만인 병원 구하기
	                    const projection = map.getProjection();
	                    const distance = projection.getDistance(currentPos, hospitalPos);
	                    return distance <= 2000;
	                });
	
	                // 필터링된 병원 마커 추가 및 params 설정
	                let params = new URLSearchParams();
	                await Promise.all(nearbyHospitals.map(hospital => {
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
	                        }, function (status, response) {
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
	                    getMemVetList(params, map, currentPos);
	                }).catch(error => {
	                    console.error('Reverse geocoding error:', error);
	                });
	            } catch (error) {
	                console.error('Error during processing:', error);
	            }
	        },
	        function (error) {
	            switch (error.code) {
	                case error.PERMISSION_DENIED:
	                    console.error("사용자가 위치 정보 요청을 거부했습니다.");
	                   	document.querySelector(".inner").innerHTML = "<div class='h-100 d-flex align-items-center justify-content-center'><div class='error_msg'>"
	                   	+"사용자가 위치 정보 요청을 거부했습니다 📍 </br> 지도 서비스 이용을 위해서 위치 정보 설정을 허용해주세요 </div></div>"
	                    break;
	                case error.POSITION_UNAVAILABLE:
	                    console.error("위치 정보를 사용할 수 없습니다.");
	                    document.querySelector(".inner").innerHTML ="<div class='h-100 d-flex align-items-center justify-content-center'><div class='error_msg'>"
	                   	+"네트워크 문제로 현재 위치 정보를 사용할 수 없습니다. </br> 잠시 후 다시 시도해주세요 </div></div>"
	                    break;
	                case error.TIMEOUT:
	                    document.querySelector(".inner").innerHTML ="<div class='h-100 d-flex align-items-center justify-content-center'><div class='error_msg'>"
	                   	+"네트워크 문제로 현재 위치 정보를 사용할 수 없습니다. </br> 잠시 후 다시 시도해주세요 </div></div>"
	                    break;
	                case error.UNKNOWN_ERROR:
	                    document.querySelector(".inner").innerHTML ="<div class='h-100 d-flex align-items-center justify-content-center'><div class='error_msg'>"
	                   	+"알 수 없는 오류가 발생하여 현재 위치 정보를 사용할 수 없습니다. </br> 잠시 후 다시 시도해주세요 </div></div>"
	                    break;
	            }
	        }
	    );
	} else {
	    console.error("이 브라우저는 위치 정보를 지원하지 않습니다.");
	}
}
xhttp.open("GET", "/json/vet_list.json", true);
xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
xhttp.setRequestHeader("Authorization", localStorage.getItem("token"));
//xhttp.setRequestHeader("role", localStorage.getItem("role"));
xhttp.send();

function getMemVetList(params, map, currentPos) {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.status === 200) {
            let data = JSON.parse(this.responseText);
            data.forEach(hospital => {
				
                let addr = hospital.address.replaceAll("//", " ");
                
                memVet[hospital.hospitalName] = {
					"id":hospital.id,
					"phone":hospital.phone,
                    "address": addr,
                    "avgReview": hospital.avgReview,
                    "review" : hospital.review,
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
    xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
    xhttp.setRequestHeader("Authorization", localStorage.getItem("token"));
    xhttp.setRequestHeader("role", localStorage.getItem("role"));
    xhttp.send();
}

function addHospitalToList(map, currentPos) {
    
    nearVet.forEach((hospital,index) => {
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
            content: '<div class="info-window-content">' 
             + '<div class="clickable-text" onclick="showModal(event)" data-bs-toggle="modal" data-bs-target="#exampleModal">' 
             + hospital["사업장명"] + '</div>' 
             + '<span class="address" style="display:none;">' + hospital["소재지전체주소"] + '</span>' 
             + '<span class="phone" style="display:none;">' + hospital["소재지전화"] + '</span>'
             + '</div>',
             borderColor: "#cb91ff",
             borderWidth :2,
        });

        markers.push(markedVet);
        infoWindows.push(infoWindow);

		loadList(hospital, index);
       
        
        //리스트에 div클릭시 해당 마커로 지도 자동이동 + 해당 infowindow열어주기
         document.querySelector(".inner").addEventListener("click", function(e) {
        	if (e.target.closest(".vet")) { // Ensure that the clicked element is within the vet div
	           	let markerIndex = e.target.closest(".vet").getAttribute("data-marker-index");
	            let marker = markers[markerIndex];
	            let infoWindow = infoWindows[markerIndex];
	            if (marker && infoWindow) {
	                map.panTo(marker.getPosition());
	                infoWindow.open(map, marker);
	            }
	         }
	     });
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

  function loadList(hospital, index){
		let phone = hospital["소재지전화"] ? hospital["소재지전화"] : '';
        let listItem = document.createElement("div");
        listItem.classList = "vet"
        listItem.setAttribute("data-marker-index", index); 
        listItem.innerHTML = '<div class="vet-header">' +
						        '<button type="button" onclick="showModal(event)" class="btn btn-hospital-sub" data-bs-toggle="modal" data-bs-target="#exampleModal">' +
						            hospital["사업장명"] +
						        '</button>' +
						        '<img class="pin" style="width:35px; display:none;" src="/images/pin_p.svg"/>' +
						        '<img onclick="return checkBookmark(event)" class="bookmark" style="display:none; width:35px;" src="/images/bookmark.png"/>' +
						      '</div>' +
						      '<div class="vet-body">' +
						        '<span class="phone">' + phone + '</span> <span class="address">' + hospital["소재지전체주소"] + '</span>' +
						      '</div>';
        document.querySelector(".inner").appendChild(listItem);

        if (memVet[hospital["사업장명"]] != null && memVet[hospital["사업장명"]]["address"] == hospital["소재지전체주소"]) {
            listItem.querySelector("button").classList = "btn btn-user-sub"
            listItem.querySelector("button").setAttribute("data-id", memVet[hospital["사업장명"]]["id"])
            listItem.querySelector(".phone").innerText = memVet[hospital["사업장명"]]["phone"];
            listItem.querySelector(".bookmark").style.display="inline-block"
            listItem.querySelector(".bookmark").src = memVet[hospital["사업장명"]]["bookmarked"] ? "/images/bookmark_fill.png" : "/images/bookmark.png";
            
            if (memVet[hospital["사업장명"]]["partnership"] == true) {
                listItem.querySelector("img").style.display="inline-block"
            }
        }
	}

function sortingReserv(e){
	 if(nearVet.length != 0){
        nearVet.sort((a, b) => {
            const aInMemVet = Object.keys(memVet).includes(a["사업장명"]);
            const bInMemVet = Object.keys(memVet).includes(b["사업장명"]);

            if (aInMemVet && !bInMemVet) {
                return -1; // a를 b보다 앞으로
            }
            if (!aInMemVet && bInMemVet) {
                return 1; // b를 a보다 앞으로
            }
            return 0; // 변화 없음
        });

        // 정렬된 결과를 콘솔에 출력
        document.querySelector(".inner").innerHTML="";
        nearVet.forEach((vetItem, index) =>{
			loadList(vetItem, index);
		})
    }
}

function sortingPoint(e) {
     if (nearVet.length != 0) {
		sortingReserv(e);
        nearVet.sort((a, b) => {
            const aPartnership = memVet[a["사업장명"]] && memVet[a["사업장명"]].partnership === true;
            const bPartnership = memVet[b["사업장명"]] && memVet[b["사업장명"]].partnership === true;

            if (aPartnership && !bPartnership) {
                return -1; // a를 b보다 앞으로
            }
            if (!aPartnership && bPartnership) {
                return 1; // b를 a보다 앞으로
            }
            return 0; // 변화 없음
        });

        // 정렬된 결과를 콘솔에 출력
        document.querySelector(".inner").innerHTML="";
       	nearVet.forEach((vetItem, index) =>{
			loadList(vetItem, index);
		})
    }
}
