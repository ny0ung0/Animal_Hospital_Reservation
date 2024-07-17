const cityContainer = document.querySelector(".cityContainer");
const guContainer = document.querySelector(".guContainer");
const searchBtn = document.querySelector("#searchBtn");
const resetBtn = document.querySelector("#resetBtn");
const vetContainer = document.querySelector(".vet_list");
const resultsContainer = document.querySelector(".result_container");
const loadingOverlay = document.getElementById("loading");

let citiesWithNoGu = new Set();
let guMap = new Map();
let memVet = {};
let activeCity = null;
let cities = null;
let gus = null;
let searchResult = [];
let keywordSearchResult=[];
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
	document.querySelector("input[name=search_vet]").value="";
	vetContainer.innerHTML="";
	searchResult=[];
    //예약되는 병원 + 포인트 제휴병원 여부 보여주기
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (this.status === 200) {
	       	let data = JSON.parse(this.responseText);
	       	data.forEach(hospital =>{
       		let addr = hospital.address.replaceAll("//", " ")
       		memVet[hospital.hospitalName] = {
												"id":hospital.id,
												"phone":hospital.phone,
												"address" : addr, 
									       		"avgReview" : hospital.avgReview,
									       		"review" : hospital.review,
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
    xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
    xhttp.setRequestHeader("token", localStorage.getItem("token"));
    xhttp.setRequestHeader("role", localStorage.getItem("role"));
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
                console.log(datas)
                datas.forEach(hospital => {
                    if (hospital["소재지전체주소"] != null) {
                        let address = hospital["소재지전체주소"].split(" ");
                        
                        if (guMap.has(address[0])) {
						    const guSet = guMap.get(address[0]);
						    if (guSet.size === 0 || guSet.has(address[1])) {
						        searchResult.push(hospital);
						        addHospitalToList(hospital);
						    }
						} else if (noGuCities.includes(address[0])) {
						    searchResult.push(hospital);
						    addHospitalToList(hospital);
						}
                    }
                });
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
	listItem.innerHTML = '<div class="vet-header">' +
					        '<button type="button" onclick="showModal(event)" class="btn btn-hospital-sub" data-bs-toggle="modal" data-bs-target="#exampleModal">' +
					            hospital["사업장명"] +
					        '</button>' +
					        '<img class="pin" style="width:35px; display:none;" src="/images/pin_p.svg"/>' +
					        '<img onclick="return checkBookmark(event)" class="bookmark" style="display:none; width:35px;" src="/images/bookmark.png"/>' +
					     '</div>' +
					     '<div class="vet-body">' +
					       '<span class="phone">' + phone + '</span> </br> <span class="address">' + hospital["소재지전체주소"] + '</span>' +
					     '</div>';
	document.querySelector(".vet_list").appendChild(listItem);
	
	if(memVet[hospital["사업장명"]] != null && memVet[hospital["사업장명"]]["address"] == hospital["소재지전체주소"]){
		listItem.querySelector("button").classList="btn btn-user-sub"
		listItem.querySelector("button").setAttribute("data-id", memVet[hospital["사업장명"]]["id"])
        listItem.querySelector(".phone").innerText = memVet[hospital["사업장명"]]["phone"];
        listItem.querySelector(".bookmark").style.display="inline-block"
        listItem.querySelector(".bookmark").src = memVet[hospital["사업장명"]]["bookmarked"] ? "/images/bookmark_fill.png" : "/images/bookmark.png";
		
		if(memVet[hospital["사업장명"]]["partnership"] == true){
			listItem.querySelector("img").style.display="inline-block"
		}
	}
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
	console.log(memVet)
	 if(searchResult.length != 0){
        searchResult.sort((a, b) => {
            const aInMemVet = memVet[a["사업장명"]] && memVet[a["사업장명"]].address == a["소재지전체주소"]
            const bInMemVet = memVet[b["사업장명"]] && memVet[b["사업장명"]].address == b["소재지전체주소"]

            if (aInMemVet && !bInMemVet) {
                return -1; // a를 b보다 앞으로
            }
            if (!aInMemVet && bInMemVet) {
                return 1; // b를 a보다 앞으로
            }
            return 0; // 변화 없음
        });

        // 정렬된 결과를 콘솔에 출력
        document.querySelector(".vet_list").innerHTML="";
        searchResult.forEach(vetItem=>{
			addHospitalToList(vetItem);
		})
    }
}


function sortingPoint(e) {
     if (searchResult.length != 0) {
		sortingReserv(e);
        searchResult.sort((a, b) => {
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
        document.querySelector(".vet_list").innerHTML="";
        searchResult.forEach(vetItem=>{
			addHospitalToList(vetItem);
		})
    }
}


// 디바운스를 위한 타이머 변수
let debounceTimer;
let isResultContainerOpen;

document.addEventListener("click", function(e){
	if(e.target != document.querySelector(".result_container")&& isResultContainerOpen == true){
		resultsContainer.style.display = "none";
		isResultContainerOpen = false;
	}
})
document.querySelector("input[name=search_vet]").addEventListener("keydown", function(e){
    clearTimeout(debounceTimer);
    resultsContainer.style.display = "block";
    resultsContainer.innerHTML = '';
	keywordSearchResult=[];
	isResultContainerOpen = true;
	
    debounceTimer = setTimeout(() => {
        const xhttp = new XMLHttpRequest();
        xhttp.onload = function () {
            let jsonData = JSON.parse(this.responseText);
            let datas = jsonData["동물병원"];

            datas.forEach(hos => {
                const address = hos["소재지전체주소"] || "";
                const name = hos["사업장명"] || "";

                if (address.includes(e.target.value) || name.includes(e.target.value)) {
                    displayResults(hos);
                    keywordSearchResult.push(hos);
                }
            });
            if(keywordSearchResult.length == 0){
				resultsContainer.innerHTML="<div class='resultMsg'>검색결과가 없습니다😥</div>";
			}
        };

        xhttp.open("GET", "/json/vet_list.json", true);
        xhttp.send();
    }, 300); // 300ms의 디바운스 시간
});


function displayResults(hospital) {
    const item = document.createElement('div');
    item.classList.add('result-item');
    item.innerHTML = "<div>" + hospital["사업장명"] + "</div> 📍 " + hospital["소재지전체주소"];
    resultsContainer.appendChild(item);
    item.addEventListener("click", function(){
		document.querySelector(".vet_list").innerHTML="";
		addHospitalToList(hospital);
	})
}

document.querySelector("#keywordSearchBtn").addEventListener("click", function() {
     if(document.querySelector("input[name=search_vet]").value.trim() == ""){
		return false;
	}
    
    document.querySelector(".vet_list").innerHTML = "";
    searchResult = [];
    let params = new URLSearchParams();

    keywordSearchResult.forEach(hos => {
         params.append(hos["사업장명"], hos["도로명전체주소"].split(" ")[0] + "//" + hos["도로명전체주소"].split(" ")[1]);
    });

    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
           	let data = JSON.parse(this.responseText);
	       	data.forEach(hospital =>{
       		let addr = hospital.address.replaceAll("//", " ")
       		memVet[hospital.hospitalName] = {
												"id":hospital.id,
												"phone":hospital.phone,
												"address" : addr, 
									       		"avgReview" : hospital.avgReview,
									       		"review" : hospital.review,
									       		"bookmarked" : hospital.bookmarked,
									       		"businessNumber" : hospital.businessNumber,
									       		"email" : hospital.email,
									       		"introduction" : hospital.introduction,
									       		"logo" : hospital.logo,
									       		"representative" : hospital.representative,
									       		"partnership" : hospital.partnership, 
									       		"businessHours" : hospital.businessHours
									       		};
	       	})
	       	keywordSearchResult.forEach(hos=>{
				addHospitalToList(hos);
	       		searchResult.push(hos)
	       		});
    };
    xhttp.open("GET", "http://localhost:9001/api/v1/keyword-vet-list?"+ params.toString(), true); 
    xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
    xhttp.setRequestHeader("Authorization", localStorage.getItem("token"));
    xhttp.setRequestHeader("role", localStorage.getItem("role"));
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(); // vetInfo 배열을 전송
});




function showLoading() {
    loadingOverlay.style.display = "flex";
}

function hideLoading() {
    loadingOverlay.style.display = "none";
}

document.getElementById("keywordSearchBtn").addEventListener("click", function () {
    if(document.querySelector("input[name=search_vet]").value.trim() == ""){
		return false;
	}
    showLoading();
    setTimeout(() => {
        hideLoading(); 
    }, 1000);
});

document.getElementById("searchBtn").addEventListener("click", function () {
	
	 if (guMap.size == 0 && citiesWithNoGu.size == 0) {
		return false;
	  }
    showLoading();
    setTimeout(() => {
        hideLoading(); 
    }, 1000);
});

document.getElementById("sortingReserv").addEventListener("click", function () {
	
	 if (searchResult.length == 0 ) {
		return false;
	  }
    showLoading();
    setTimeout(() => {
        hideLoading(); 
    }, 1000);
});

document.getElementById("sortingPoint").addEventListener("click", function () {
	
	 if (searchResult.length == 0 ) {
		return false;
	  }
    showLoading();
    setTimeout(() => {
        hideLoading(); 
    }, 1000);
});
