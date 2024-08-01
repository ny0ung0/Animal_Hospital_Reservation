 const date = document.querySelector("input[type=date]");
 const vet = document.querySelector("#vet");
 const timeSlot = document.querySelector("#time_slot");
 let today = new Date();
 let selectedDate;
 //0:일, 1:월, 2:화 .... 6:토
 let selectedDay;
 let selectedVet;
 let selectedTime;
 let selectedSlot = null; 



function setDateLimit(){
	//오늘 날짜보다 이전 날짜는 선택할 수 없도록  
	date.setAttribute("min", today.toISOString().substring(0,10))
	//max 날짜는 3개월 오늘 날짜 전일
	today.setMonth(today.getMonth()+2)
	date.setAttribute("max", today.toISOString().substring(0,10))
}
setDateLimit();


function loadBasicInfo(data){
	let userInfo = data[0];
	let vetAvailInfo = data[1];
	let vetInfo = data[2];
	let vetNamesNIds = Object.keys(vetAvailInfo);
	let basicHours = JSON.parse(vetInfo[Object.keys(vetInfo)[0]].businessHours);
	console.log(userInfo.petList)
	
	//등록된 반려동물이 없을 시에 알림창 띄워주기
	if(userInfo.petList.length == 0){
		alert("등록된 반려동물이 없습니다. 반려동물을 먼저 등록해주세요");
		location.href="/user/myPetForm";
	}
	
	  //병원 이름 넣기
	  document.querySelector("#vetName").setAttribute("value", Object.keys(vetInfo)[0]);
	  //로그인한 사용자 이름 넣어주기
	  document.querySelector("input[name=user_name]").value = userInfo.user.name;
	  //포인트정보넣기
	  document.querySelector("#point").innerHTML = userInfo.pointList[0] ? userInfo.pointList[0] : "0";
	  
	  if(!userInfo.pointList[0]){
		document.querySelector("input[name=points_used]").disabled="true";
	  }
	  
	  if(!vetInfo[Object.keys(vetInfo)[0]].partnership == true){
		  document.querySelector(".point_container").style.display="none";
	  }
	  
	  
	  //반려동물정보넣기
	  for(let i = 0; i < userInfo.petList.length; i++){
		  let pet = userInfo.petList[i];
		  let listItem = document.createElement("option");
		  listItem.setAttribute("value", pet.id);
		  listItem.innerHTML = pet.name + '';
		  document.querySelector("#pet").appendChild(listItem);
	  }
	  //수의사정보넣기
	  for(let i = 0; i < vetNamesNIds.length; i++){
		  console.log(vetNamesNIds[i])
		  let vet = vetNamesNIds[i].split("//")
		  let vetName = vet[1];
		  let vetId = vet[0];
		
		  let listItem = document.createElement("option");
		  listItem.setAttribute("value", vetId);
		  listItem.innerHTML = vetName + ' 수의사 선생님';
		  document.querySelector("#vet").appendChild(listItem);
	  }
	  
	//시간 뿌려주기(의사, 날짜 를 선택하는 순간 그에 맞춰서 시간 바꿔주기)
	// //기본적으로 영업시간에 기준해서 시간 뿌려주기
	  vet.addEventListener("change", function(e){
		  document.querySelector("#time_slot").innerHTML="";
			selectedVet = e.target.value
			if(selectedDate == null){
				alert("진료 예약을 원하는 날짜를 먼저 선택해주세요!")
			}
			loadTimeslot(basicHours,vetAvailInfo);
		})
}


function convertToTimeZone(date, timeZone) {
	
    // 시간대를 변환한 날짜를 생성
    const dateInTimeZone = new Date(date.toLocaleString('en-US', { timeZone }));

    // 년, 월, 일을 가져옴
    const year = dateInTimeZone.getFullYear();
    const month = String(dateInTimeZone.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
    const day = String(dateInTimeZone.getDate()).padStart(2, '0');

    // yyyy-MM-dd 형식으로 반환
    return year+"-"+ month+"-"+day;
}

function convertTimestamp(timestamp) {
    // Unix 타임스탬프를 Date 객체로 변환
    const date = new Date(timestamp);

    // 서울 시간대(KST)로 변환한 날짜 객체를 문자열로 변환
    const options = {
        timeZone: 'Asia/Seoul',
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    };

    // 변환된 날짜 문자열을 가져옴 (MM/dd/yyyy 형식)
    const seoulDateString = new Intl.DateTimeFormat('en-US', options).format(date);

    // "yyyy-MM-dd" 형식으로 변환된 문자열 반환
    const [month, day, year] = seoulDateString.split(/\D/);

    return  year+"-"+ month+"-"+day;
}

function formattingDate(date){
	let newDate = date.slice(0,10);
	return newDate;
}


function showDates(startTime, endTime, lunchStart, lunchEnd){
	if(startTime == 0|| endTime == 0){
		document.querySelector("#time_slot").innerHTML="<div class='msg'>해당일은 예약가능한 시간이 없습니다😥</div>"
		return;
	}
    // 시간을 분 단위로 변환
    let [startHour, startMinute] = startTime.split(":").map(Number);
    let [endHour, endMinute] = endTime.split(":").map(Number);
    let [lunchStartHour, lunchStartMinute] = lunchStart != 0 ? lunchStart.split(":").map(Number) : [0,0];
    let [lunchEndHour, lunchEndMinute] = lunchEnd != 0 ? lunchEnd.split(":").map(Number) : [0,0];
    let startTotalMinutes = startHour * 60 + startMinute;
    let endTotalMinutes = endHour * 60 + endMinute;
    let lunchStartTotalMinutes = lunchStartHour * 60 + lunchStartMinute;
    let lunchEndTotalMinutes = lunchEndHour * 60 + lunchEndMinute;
    
    // 30분 간격으로 버튼 생성
    let currentMinutes = startTotalMinutes;
    while (currentMinutes < endTotalMinutes) {
        let hours = Math.floor(currentMinutes / 60);
        let minutes = currentMinutes % 60;
        
        let timeString = String(hours).padStart(2, '0') + ":" + String(minutes).padStart(2, '0');
        let listItem = document.createElement("span");
        listItem.setAttribute("value", timeString);
        listItem.classList = "time_slot"
        listItem.innerHTML = timeString;
        document.querySelector("#time_slot").appendChild(listItem);
        currentMinutes += 30;
        
        if (currentMinutes > lunchStartTotalMinutes && currentMinutes <= lunchEndTotalMinutes) {
            listItem.classList.add("disabled");
        } else {
            listItem.addEventListener("click", function() {
                // click 이벤트 핸들링
                console.log("예약 시간: " + timeString);
            });
        }
        listItem.addEventListener("click", function(e) {
        	//click이벤트 핸들링
            console.log("예약 시간: " + timeString);
        	selectedTime = timeString;
        	if (selectedSlot) {
                selectedSlot.style.backgroundColor = ""; 
                selectedSlot.style.color = "black"; 
            }
            e.target.style.backgroundColor = "#4C5CB3";
            e.target.style.color = "white";
            
            selectedSlot = e.target;
        });
    }
}

function loadTimeslot(basicHours, vetAvailInfo) {
    let selectedDayArr = ["sun", "mon", "tue", "wed", "thu", "fri", "sat", "hol"];
    let selected = selectedDayArr[selectedDay];
    let date = new Date();
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');

    // 현재 날짜
    let todayDate = year + "-" + month + "-" + day;

    // 기본 타임슬롯 보여주기
    if (basicHours[selected] != null) {
        let selectedBasicTime = basicHours[selected];
        showDates(selectedBasicTime[0], selectedBasicTime[1], selectedBasicTime[2], selectedBasicTime[3]);

        // 선택한 날짜가 오늘이라면 현재 시간 이전의 타임슬롯 비활성화
        if (selectedDate === todayDate) {
            let currentTime;
            if (Number(minutes) < 30) {
                minutes = "00";
                currentTime = hours + ":" + minutes;
            } else {
                minutes = "30";
                currentTime = hours + ":" + minutes;
            }

            // 기본 타임슬롯 범위 내의 타임슬롯을 순회하며 비활성화(예약이 당일일때 예약하려는 시간 전시간 타임슬롯은 모두 비활성화)
            let startHour = parseInt(selectedBasicTime[0].split(":")[0]);
            let startMinute = parseInt(selectedBasicTime[0].split(":")[1]);
            let endHour = parseInt(selectedBasicTime[1].split(":")[0]);
            let endMinute = parseInt(selectedBasicTime[1].split(":")[1]);

            for (let h = startHour; h <= endHour; h++) {
                for (let m = 0; m < 60; m += 30) {
                    let time = h.toString().padStart(2, '0') + ":" + m.toString().padStart(2, '0');
                    if (time <= currentTime) {
                        document.querySelector("span[value='" + time + "']").classList.add("disabled");
                    }
                    if (h === endHour && m === endMinute) break;
                }
            }
        }
        // 선생님의 availability 보여주기
        Object.keys(vetAvailInfo).forEach(key => {
				
            if (key.split("//")[0] == selectedVet) {
                for (let v of vetAvailInfo[key]) {
		
                    if (convertTimestamp(v.date) == selectedDate) {
                        let time = v.time[0].toString().padStart(2, '0') + ":" + v.time[1].toString().padStart(2, '0');
                        
                        console.log(time)
                        document.querySelector("span[value='" + time + "']").classList.add("disabled");
                    }
                }
            }
        });
    }
}


date.addEventListener("change", function(e){
	if(selectedVet != null){
		selectedVet="";
		vet.value= "default";
		document.querySelector("#time_slot").innerHTML="";
	}
	selectedDate = e.target.value
	selectedDay = new Date(selectedDate).getDay();
	
})