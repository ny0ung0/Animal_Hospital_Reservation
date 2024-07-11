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
        listItem.innerHTML = "<span class='modal-sub-title'>"+ week[i] + "</span> <span class='workHour'>" + startTime + " ~ " + endTime
                             +"</span> <span class='modal-sub-title del'>|| 점심시간 </span> <span class='lunchHour'>" + lunchStart + " ~ " + lunchEnd +"</span>";

        if(startTime === 0){
            listItem.querySelector(".workHour").innerHTML = "휴무";
            listItem.querySelector(".workHour").classList = 'workHour modal-sub-title';
            listItem.querySelector(".del").innerHTML = "";
            listItem.querySelector(".lunchHour").innerHTML = "";
            listItem.querySelector(".workHour").style.color="#cd362f";
		 	if(lunchStart === 0){
		       listItem.querySelector(".lunchHour").innerHTML = "";
		     }
        }else if(lunchStart === 0){
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
	document.querySelector("#phone").innerHTML = phone;
	document.querySelector("#address").innerHTML = address;

    console.log(memVet[hospitalName])


	// 조건문 충족 여부에 따른 정보 설정
	if (memVet[hospitalName] != null && memVet[hospitalName]["address"] == address) {
		document.querySelector("#exampleModalLabel").setAttribute("data-id", memVet[hospitalName]["id"]);
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
		// 평균별점 설정
		document.querySelector("#avgReview").style.display = "inline-block";
		document.querySelector("#avgReview").innerHTML = memVet[hospitalName]["avgReview"];
	} 
}

function checkBookmark(e){
	let hosId;
	if(!localStorage.getItem("MemberId")){
		alert("로그인한 회원만 이용 가능한 서비스입니다. 로그인 후 이용해주세요");
		return false;
	}
	if(!e.target.parentElement.parentElement.querySelector("button").getAttribute("data-id")){
		hosId = e.target.parentElement.querySelector("#exampleModalLabel").getAttribute("data-id");
	}else{
		hosId = e.target.parentElement.parentElement.querySelector("button").getAttribute("data-id");
	}
	let filled = "http://localhost:8093/images/bookmark_fill.png"
	let empty = "http://localhost:8093/images/bookmark.png"
	let isBookmarked;
	
	if(e.target.src == filled){
		e.target.src = empty;
		alert("이 병원을 즐겨찾기 목록에서 삭제하시겠습니까?")
		//북마크 취소 db에 업데이트해주기
		isBookmarked=false;
	}else{
		e.target.src = filled;
		alert("이 병원을 즐겨찾기 목록에 추가하시겠습니까?")
		//북마크 구독 db에 업데이트해주기
		isBookmarked=true;
	}
	 const xhttp = new XMLHttpRequest();
	  xhttp.onload = function() {
	     alert("성공적으로 즐겨찾기목록이 업데이트 되었습니다.")
	  }
	  xhttp.open("POST", "http://localhost:9001/api/v1/user/bookmark/"+isBookmarked+"/"+hosId, true);
	  xhttp.setRequestHeader("MemberId", localStorage.getItem("MemberId"));
	  xhttp.setRequestHeader("token", localStorage.getItem("token"));
	  xhttp.setRequestHeader("role", localStorage.getItem("role"));
	  xhttp.send();
}

function makeReservation(e){
	if(localStorage.getItem("username") == null || localStorage.getItem("role") != "ROLE_USER"){
		alert("로그인한 회원만 사용할 수 있는 기능입니다. 로그인 후 이용해주세요")
	}else{
		let id = e.target.parentElement.querySelector("#hospital_id").innerText;
		location.href="/user/reserv_form?id="+id;
	}
}