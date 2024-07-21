function showBusinessHour(basicHours) {
    let week = ["일", "월", "화", "수", "목", "금", "토", "공휴일"];
    let days = ["sun", "mon", "tue", "wed", "thu", "fri", "sat", "hol"]; // JSON의 키 순서에 맞춰 요일 배열을 정의합니다.

    for (let i = 0; i < days.length; i++) {
        let day = basicHours[days[i]];
        let startTime = day[0];
        let endTime = day[1];
        let lunchStart = day[2];
        let lunchEnd = day[3];

        let listItem = document.createElement("div");
        listItem.innerHTML = "<span class='modal-sub-title'>" + week[i] + "</span> <span class='workHour'>" + startTime + " ~ " + endTime
            + "</span> <span class='modal-sub-title del'>|| 점심시간 </span> <span class='lunchHour'>" + lunchStart + " ~ " + lunchEnd + "</span>";

        if (startTime === "0") {
            listItem.querySelector(".workHour").innerHTML = "휴무";
            listItem.querySelector(".workHour").classList = 'workHour modal-sub-title';
            listItem.querySelector(".del").innerHTML = "";
            listItem.querySelector(".lunchHour").innerHTML = "";
            listItem.querySelector(".workHour").style.color = "#cd362f";
            if (lunchStart === "0") {
                listItem.querySelector(".lunchHour").innerHTML = "";
            }
        } else if (lunchStart === "0") {
            listItem.querySelector(".lunchHour").innerHTML = "-";
        }

        document.querySelector("#working_hour").appendChild(listItem);
    }
}


// 모달에 해당 병원 상세정보 보여주기
function showModal(e) {
	
	//회원만 있는 병원 정보칸 가리기
	document.querySelector(".modal-memVetInfo").style.display = "none";
	
	let hospitalName;
	let address;
	let phone;
	
	//마커를 클릭하여 모달을 열 경우
	if (e.target.parentElement.querySelector("button") == null) {
		hospitalName = e.target.innerText;
		address = e.target.parentElement.querySelector(".address").innerText;
		phone = e.target.parentElement.querySelector(".phone").innerText;
	} else {
		//리스트의 버튼을 클릭하여 모달을 열 경우
		hospitalName = e.target.parentElement.querySelector("button").innerText;
		address = e.target.parentElement.parentElement.querySelector(".address").innerText;
		phone = e.target.parentElement.parentElement.querySelector(".phone").innerText;
	}

	// 기본 정보 설정
	document.querySelector("#exampleModalLabel").innerText = hospitalName;
	document.querySelector("#phone").innerHTML = phone ? phone : "-";
	document.querySelector("#address").innerHTML = address;

    console.log(memVet[hospitalName])


	// 조건문 충족 여부에 따른 정보 설정
	if (memVet[hospitalName] != null && memVet[hospitalName]["address"] == address) {
		document.querySelector("#exampleModalLabel").setAttribute("data-id", memVet[hospitalName]["id"]);
		document.querySelector("#working_hour").innerHTML = "";
		document.querySelector("#review").innerHTML ="";
		
		
		let basicHours = JSON.parse(memVet[hospitalName]["businessHours"]);


		//회원만 있는 병원 정보칸 보이게하기
		document.querySelector(".modal-memVetInfo").style.display = "block";

		// 병원 id 설정
		document.querySelector("#hospital_id").innerHTML = memVet[hospitalName]["id"];
		// 영업시간 설정
		document.querySelector("#working_hour").style.display = "block";
		showBusinessHour(basicHours);
		// 예약하기 버튼 설정
		document.querySelector(".reservationBtn").style.display = "block";
		// 채팅 버튼 설정
		document.querySelector(".chatBtn").style.display = "block";
		// 포인트제휴여부 설정
		document.querySelector("#point").style.display = "inline-block";
		document.querySelector("#point").innerHTML = memVet[hospitalName]["partnership"] ? "포인트제휴병원 ⭕" : "포인트제휴병원 ❌";
		// 북마크 설정
		document.querySelector("#bookmarkImg").style.display = "inline"; 
		document.querySelector("#bookmarkImg").src = memVet[hospitalName]["bookmarked"] ? "/images/bookmark_fill.png" : "/images/bookmark.png"; 
		// 사업자번호 설정
		document.querySelector("#businessNumber").style.display = "inline-block";
		document.querySelector("#businessNumber").innerHTML = memVet[hospitalName]["businessNumber"];
		// 이메일 설정
		document.querySelector("#email").style.display = "inline-block";
		document.querySelector("#email").innerHTML= memVet[hospitalName]["email"];
		// 소개글 설정
		document.querySelector("#introduction").style.display = "block";
		document.querySelector("#introduction").innerHTML = memVet[hospitalName]["introduction"];
		if(memVet[hospitalName]["logo"] != null){
			// 로고 설정
			document.querySelector("#logo").style.display = "inline-block";
			document.querySelector(".img_container").style.display = "inline-block";
			document.querySelector("#logo").src = "/images/user/" + memVet[hospitalName]["logo"];
		}
		// 대표자 설정
		document.querySelector("#representative").style.display = "inline-block";
		document.querySelector("#representative").innerHTML = memVet[hospitalName]["representative"];
		
		// 리뷰 설정
		// 1. 평균별점 설정
		document.querySelector("#avgReview").innerHTML = memVet[hospitalName]["avgReview"] ? 
		    memVet[hospitalName]["avgReview"] + "/5" + repeatCharacters("⭐", memVet[hospitalName]["avgReview"]) :
		    "<span style=\"font-size: 18px; font-family: 'Jua', sans-serif; font-weight: normal;\">아직 별점이 없어요😅 <br> 예약 후, 첫 별점을 남겨보세요 :)</span>";

	    const hasReviews = memVet[hospitalName]["review"].length > 0;
	
	    if (hasReviews) {
	      document.querySelector("#reviewCount").innerText = "리뷰 " + memVet[hospitalName]["review"].length + "개";
	      document.querySelector("#reviewDistribution").style.display = "block";
	      document.querySelector("#toggleReviews").style.display = "block";
	      document.querySelector("#reviewList").style.display = "none"; // 초기 상태는 숨김
	
	      // 3. 리뷰 분포 그래프 설정
	      let reviewDistributionHTML = '';
	      const reviewCounts = [0, 0, 0, 0, 0]; // 5점부터 1점까지의 리뷰 개수
	      const maxReviewCount = memVet[hospitalName]["review"].length || 1; // 최대 리뷰 개수
	
	      memVet[hospitalName]["review"].forEach(function(review) {
	        reviewCounts[review.rating - 1]++;
	      });
	
	      for (let i = 4; i >= 0; i--) {
	        const barWidth = (reviewCounts[i] / maxReviewCount) * 100 + "%";
	        reviewDistributionHTML += 
	          "<div class='review-bar'><span>" + (i + 1) + "점:</span>" + 
	          "<div style='width:" + barWidth + ";'></div>" + 
	          "</div>";
	      }
	
	      document.querySelector("#reviewDistribution").innerHTML = reviewDistributionHTML;
	
	      // 4. 리뷰 뿌려주기
	      document.querySelector("#review").innerHTML = ''; // 기존 리뷰 초기화
	
	      memVet[hospitalName]["review"].forEach(function(review) {
	        let listItem = document.createElement("div");
	        listItem.classList = "review-item";
	        listItem.innerHTML = 
	          "<span>" + review.review + "</span>" +
	          "<span><div>" + review.type + "</div> || <div>" + review.doctor.name + " 수의사</div> || " +
	          review.updatedAt[0] + "-" + review.updatedAt[1] + "-" + review.updatedAt[2] + "</span>";
	        document.querySelector("#review").appendChild(listItem);
	      });
	
	      // 리뷰 보기 버튼 이벤트 설정
	      document.querySelector("#toggleReviews").onclick = function() {
	        const reviewList = document.querySelector("#reviewList");
	        if (reviewList.style.display === "none") {
	          reviewList.style.display = "block";
	          this.innerText = "리뷰 숨기기";
	        } else {
	          reviewList.style.display = "none";
	          this.innerText = "리뷰 보기";
	        }
	      };
	    } else {
	      document.querySelector("#reviewCount").innerText = "";
	      document.querySelector("#reviewDistribution").style.display = "none";
	      document.querySelector("#toggleReviews").style.display = "none";
	    }
	  }
	}
	
	function repeatCharacters(str, rate) {
	    // 문자열의 길이만큼 반복
	    for (let i = 0; i < rate; i++) {
	        // 현재 문자를 문자열의 길이만큼 반복해서 출력
	        let repeatedChar = '';
	        for (let j = 0; j < rate; j++) {
	            repeatedChar += str[i];
	        }
		return repeatedChar;
	}
}

function checkBookmark(e){
	let hosId;
	let filled = "http://localhost:8093/images/bookmark_fill.png"
	let empty = "http://localhost:8093/images/bookmark.png"
	let isBookmarked;
	
	if(!localStorage.getItem("MemberId")){
		alert("로그인한 회원만 이용 가능한 서비스입니다. 로그인 후 이용해주세요");
		return false;
	}
	
	if(!e.target.parentElement.parentElement.querySelector("button").getAttribute("data-id") && e.target.parentElement.querySelector("#exampleModalLabel")){
		hosId = e.target.parentElement.querySelector("#exampleModalLabel").getAttribute("data-id");
	}else if(!e.target.parentElement.parentElement.querySelector("button").getAttribute("data-id") && e.target.parentElement.querySelector("#hospital_id").getAttribute("data-id")){
	    hosId = e.target.parentElement.querySelector("#hospital_id").getAttribute("data-id");
	}else{
		hosId = e.target.parentElement.parentElement.querySelector("button").getAttribute("data-id");
	}
	
	if(e.target.src == filled){
		if(confirm("이 병원을 즐겨찾기 목록에서 삭제하시겠습니까?")){
			e.target.src = empty;
			//북마크 취소 db에 업데이트해주기
			isBookmarked=false;
		}
	}else{
		if(confirm("이 병원을 즐겨찾기 목록에 추가하시겠습니까?")){
			e.target.src = filled;
			//북마크 구독 db에 업데이트해주기
			isBookmarked=true;
		}
	}
	 const xhttp = new XMLHttpRequest();
	  xhttp.onload = function() {
	     alert("성공적으로 즐겨찾기목록이 업데이트 되었습니다.")
	  }
	  xhttp.open("POST", "http://localhost:9001/api/v1/user/bookmark/"+isBookmarked+"/"+hosId, true);
	  xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
	  xhttp.setRequestHeader("Authorization", localStorage.getItem("token"));
	  xhttp.setRequestHeader("role", localStorage.getItem("role"));
	  xhttp.send();
}

function makeReservation(e){
	if(localStorage.getItem("MemberId") == null || localStorage.getItem("role") != "ROLE_USER"){
		alert("로그인한 회원만 사용할 수 있는 기능입니다. 로그인 후 이용해주세요")
	}else{
		let hosId = e.target.closest("#exampleModal").querySelector("#exampleModalLabel").getAttribute("data-id");
		location.href="/user/reserv_form?id="+hosId;
	}
}

function sortingReserv(e){
	
	let totalResult;
    let container;
    let disFunction;

    if (typeof searchResult !== 'undefined' && searchResult.length !== 0) {
        totalResult = searchResult;
        container = ".vet_list";
    } else{
        totalResult = nearVet;
        container = ".inner";
    } 
    
    
	 if(totalResult.length != 0){
         const sortedNearVet = totalResult.slice().sort((a, b) => {
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
        document.querySelector(container).innerHTML="";
        sortedNearVet.forEach((vetItem, index) =>{
			if(container == ".vet_list"){
				addHospitalToList(vetItem)
			}else{
				loadList(vetItem, index);
			}
		})
		return sortedNearVet;
    }
}


function sortingPoint(e) {
	
	let totalResult;
    let container;
    let disFunction;

    if (typeof searchResult !== 'undefined' && searchResult.length !== 0) {
        totalResult = searchResult;
        container = ".vet_list";
    } else{
        totalResult = nearVet;
        container = ".inner";
    } 
    
     if (totalResult.length != 0) {
        const sortedNearVet = sortingReserv(e).slice().sort((a, b) => {
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
        document.querySelector(container).innerHTML="";
        sortedNearVet.forEach((vetItem, index)=>{
			if(container == ".vet_list"){
				addHospitalToList(vetItem)
			}else{
				loadList(vetItem, index);
			}
		})
    }
}
