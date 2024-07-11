const cityContainer = document.querySelector(".cityContainer");
const guContainer = document.querySelector(".guContainer");
const searchBtn = document.querySelector("#searchBtn");
const resetBtn = document.querySelector("#resetBtn");
const vetContainer = document.querySelector(".vet_list");
let citiesWithNoGu = new Set();
let guMap = new Map();
let memVet = {};
let activeCity = null;
let cities = null;
let gus = null;
let searchResult = [];
//지역리스트
function loadRegionList() {
    fetch('/json/korea-administrative-district.json')
        .then(response => response.json())
        .then(data => {
            data.forEach((cityData, i) => {
                const city = Object.keys(cityData)[0];
                cityContainer.innerHTML += "<div class='cities' id='city" + i + "'>" + city + "</div>";
                cities = document.querySelectorAll(".cities")
            });

            document.querySelectorAll(".cities").forEach(city => {
                city.addEventListener("click", function (e) {
                	guContainer.innerHTML = "";
                    const cname = e.target.innerText;
                    const ccode = Number(e.target.id.slice(4));
                    const guList = Object.values(data[ccode])[0];
                    
                   if(activeCity === cname){
            			 activeCity = null;
            			 guMap.delete(cname);
            			 e.target.classList.remove('bg-primary');
            		}else{
            			 activeCity = cname;
            			 if (guMap.has(cname) || citiesWithNoGu.has(cname)) {
                         	//citiesWithNoGu에 있는 cname인지 체크 -> 맞다면 지우고 비활성화, 아니라면 해당 cname에 구가 몇개 있는지 체크 -> 구가 1개만 있을때 비활성화
                         	if(citiesWithNoGu.has(cname)){
                         		citiesWithNoGu.delete(cname);
                         		e.target.classList.remove('bg-primary');
                         	}else{
                        			//해당시에 이미 선택되어있는 구 리스트 보여주기
                        			 guList.forEach((guName) => {
                                         const guClass = guMap.get(cname).has(guName) ? ' bg-primary' : '';
                                         guContainer.innerHTML += "<span class='guEl "+ guClass+"' id='gu" + cname + "'>" + guName + "</span> ";
                                   });
                         	}
                         } else {
                             if (guList.length === 0) {
                                 citiesWithNoGu.add(cname);
                             } else {
                                 guMap.set(cname, new Set());
                                 guList.forEach((guName) => {
                                	 const guClass = guMap.get(cname).has(guName) ? ' bg-primary' : '';
                                     guContainer.innerHTML += "<span class='guEl "+ guClass+"' id='gu" + cname + "'>" + guName + "</span> ";
                                 });
                             }
                             e.target.classList.add('bg-primary');
                         }
            			 gus = document.querySelectorAll(".guEl")
            		}
                    
                });
            });

            document.addEventListener("click", function (e) {
                if (e.target.classList.contains('guEl')) {
                    const gname = e.target.innerText;
                    const cityName = e.target.getAttribute("id").substring(2);
                  
                   	if(guMap.get(cityName).has(gname)){
                   		guMap.get(cityName).delete(gname);
                   		e.target.classList.remove('bg-primary');
                   	}else{
                   		guMap.get(cityName).add(gname);
                   		e.target.classList.add('bg-primary');
                   	}
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}

searchBtn.addEventListener("click", function() {
	vetContainer.innerHTML="";
	searchResult=[];
    console.log(memVet)
    //예약되는 병원 + 포인트 제휴병원 여부 보여주기
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (this.status === 200) {
	       	let data = JSON.parse(this.responseText);
	       	data.forEach(hospital =>{
       		let addr = hospital.address.replaceAll("//", " ")
       		memVet[hospital.hospitalName] = {
											"id":hospital.id,
											"address" : addr, 
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
        fetchHospitalData(guMap, Array.from(citiesWithNoGu));
    };
      
 // GET 요청 URL에 쿼리 매개 변수로 데이터 추가
    const params = new URLSearchParams();
    guMap.keys().forEach(key=>{
    	params.append(key, Array.from(guMap.get(key)));
    })
    
    const url = "http://localhost:9001/api/v1/vet-list?" + params.toString();
    xhttp.open("GET", url, true);
//    xhttp.setRequestHeader("username", 1);
    xhttp.send();
});


loadRegionList();

function fetchHospitalData(guMap, noGuCities) {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (this.status === 200) {
            try {
                let jsonData = JSON.parse(this.responseText);
                let datas = jsonData["동물병원"];
                datas.forEach(hospital => {
                    if (hospital["소재지전체주소"] != null) {
                        let address = hospital["소재지전체주소"].split(" ");
                        if (guMap.has(address[0]) || noGuCities.includes(address[0])) {
                            if (guMap.get(address[0]).size === 0 || guMap.get(address[0]).has(address[1])) {
                                searchResult.push(hospital);
                                addHospitalToList(hospital);
                            }
                        }
                    }
                });
                console.log(searchResult)
            } catch (e) {
                console.log(e);
            }
        } else {
            alert("Failed to load hospital data");
        }
    };
    xhttp.onerror = function () {
        alert("Network error while fetching hospital data");
    };
    xhttp.ontimeout = function () {
        alert("Network timeout while fetching hospital data");
    };
    xhttp.open("GET", "/json/vet_list.json", true);
    xhttp.timeout = 5000; // 5초 후 타임아웃
    xhttp.send();
}

function addHospitalToList(hospital) {
	let phone = hospital["소재지전화"] ? hospital["소재지전화"] : '';
    let listItem = document.createElement("div");
    listItem.classList = "vet"
	listItem.innerHTML = '<button type="button" onclick="showModal(event)" class="btn btn-hospital-sub" data-bs-toggle="modal" data-bs-target="#exampleModal">'
	        + hospital["사업장명"] + '</button> , '
	        + '<img class="pin" style="width:35px; display:none;" src="/images/pin_p.svg"/>'
	        + '<img class="bookmark" style="width:35px;" src="/images/bookmark.png"/>'
	        + '<span class="phone">' + phone
	        + '</span> , <span class="address">' + hospital["소재지전체주소"] + '</span>';
	document.querySelector(".vet_list").appendChild(listItem);
	
	if(memVet[hospital["사업장명"]] != null && memVet[hospital["사업장명"]]["address"] == hospital["소재지전체주소"]){
		listItem.querySelector("button").classList="btn btn-user-sub"
		if(memVet[hospital["사업장명"]]["partnership"] == true){
			listItem.querySelector("img").style.display="inline-block"
		}
	}
}

//모달에 해당 병원 상세정보 보여주기
function showModal(e){
	//회원만 있는 병원 정보칸 가리기
	document.querySelector(".modal-memVetInfo").style.display = "none";
	
	
	let hospitalName = e.target.parentElement.querySelector("button").innerText;
	let address = e.target.parentElement.querySelector(".address").innerText;
	
	
	if(memVet[hospitalName] != null && memVet[hospitalName]["address"] == address){
		document.querySelector("#working_hour").innerHTML = "";
		let basicHours = JSON.parse(memVet[hospitalName]["businessHours"]);
		let hoursArr = getBasicBusinessHours(basicHours);

		//회원만 있는 병원 정보칸 보이게하기
		document.querySelector(".modal-memVetInfo").style.display = "block";

		// 병원 id 설정
		document.querySelector("#hospital_id").innerHTML = memVet[hospitalName]["id"];
		// 영업시간 설정
		document.querySelector("#working_hour").style.display = "block";
		showBusinessHour(hoursArr);
		// 리뷰 설정
		document.querySelector("#review").style.display = "inline-block";
		document.querySelector("#review").innerHTML = memVet[hospitalName]["review"];
		// 예약하기 버튼 설정
		document.querySelector(".reservationBtn").style.display = "block";
		// 채팅 버튼 설정
		document.querySelector(".chatBtn").style.display = "block";
		// 포인트제휴여부 설정
		document.querySelector("#point").style.display = "inline-block";
		document.querySelector("#point").innerHTML = memVet[hospitalName]["partnership"] ? "포인트제휴병원 ⭕" : "포인트제휴병원 ❌";
		// 북마크 설정
		document.querySelector("#bookmarked").style.display = "inline-block";
		document.querySelector("#bookmarked").innerHTML = memVet[hospitalName]["bookmarked"];
		// 사업자번호 설정
		document.querySelector("#businessNumber").style.display = "inline-block";
		document.querySelector("#businessNumber").innerHTML = memVet[hospitalName]["businessNumber"];
		// 이메일 설정
		document.querySelector("#email").style.display = "inline-block";
		document.querySelector("#email").innerHTML= memVet[hospitalName]["email"];
		// 소개글 설정
		document.querySelector("#introduction").style.display = "block";
		document.querySelector("#introduction").innerHTML = memVet[hospitalName]["introduction"];
		// 로고 설정
		document.querySelector("#logo").style.display = "block";
		document.querySelector("#logo").innerHTML = memVet[hospitalName]["logo"];
		// 대표자 설정
		document.querySelector("#representative").style.display = "inline-block";
		document.querySelector("#representative").innerHTML = memVet[hospitalName]["representative"];
		// 평균별점 설정
		document.querySelector("#avgReview").style.display = "inline-block";
		document.querySelector("#avgReview").innerHTML = memVet[hospitalName]["avgReview"];
	}
	document.querySelector("#exampleModalLabel").innerText = hospitalName;
	document.querySelector("#phone").innerHTML = e.target.parentElement.querySelector(".phone").innerText;
	document.querySelector("#address").innerHTML = address;
}

function resetFilter(){
	vetContainer.innerHTML="";
	guContainer.innerHTML="";
	guMap.clear();
	citiesWithNoGu.clear();
	cities.forEach(city=>{
		city.classList.remove('bg-primary');
	})
	gus.forEach(gu=>{
		gu.classList.remove('bg-primary');
	})
}

function sortingReserv(e){
	console.log("예약가능병원순")
	console.log(searchResult)
	console.log(memVet)
}


//function sortingPoint(e) {
//     console.log("포인트제휴병원순");
//    console.log("memVet:", memVet);
//    console.log("searchResult before sort:", searchResult);
//
//    searchResult.sort((a, b) => {
//        const aName = a["사업장명"];
//        const bName = b["사업장명"];
//        const aValue = memVet[aName];
//        const bValue = memVet[bName];
//
//        const aInMemVet = aValue ? 1 : 0;
//        const bInMemVet = bValue ? 1 : 0;
//
//        // memVet에 있는 요소를 먼저 위로 정렬
//        if (aInMemVet !== bInMemVet) {
//            return bInMemVet - aInMemVet;
//        }
//
//    });
//
//    // 정렬된 결과를 vetContainer에 다시 렌더링
//    vetContainer.innerHTML = "";
//    searchResult.forEach(hospital => addHospitalToList(hospital));
//
//    console.log("searchResult after sort:", searchResult);
//}

function makeReservation(){
	if(localStorage.getItem("username") == null || localStorage.getItem("role") != "ROLE_USER"){
		alert("로그인한 회원만 사용할 수 있는 기능입니다. 로그인 후 이용해주세요")
	}else{
		console.log("예약하기 페이지로 가기")
	}
}
